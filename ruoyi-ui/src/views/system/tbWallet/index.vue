<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="帐号" prop="account">
        <el-input
          v-model="queryParams.account"
          placeholder="请输入帐号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          @click="handleAdd"
          v-hasPermi="['system:tbWallet:add']"
        >创建帐号</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-upload2"
          @click="importAccountShow"
          v-hasPermi="['system:tbWallet:edit']"
        >导入帐号</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:tbWallet:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:tbWallet:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          @click="handleExport"
          v-hasPermi="['system:tbWallet:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tbWalletList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="帐号名" align="center" prop="name" />
      <el-table-column label="所属用户" align="center" prop="userName" />
      <el-table-column label="帐号" align="center" prop="account" />
      <el-table-column label="类型" align="center" prop="isPrivate" >
        <template slot-scope="scope">
          <span v-if="scope.row.isPrivate == 0" style="color: indianred">助记词</span>
          <span v-if="scope.row.isPrivate == 1" style="color: green">密匙</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="gmtCreate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.gmtCreate, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:tbWallet:edit']"
          >修改</el-button>
          <el-button
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tbWallet:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <el-dialog v-loading="loading" title="导入帐号" :visible.sync="importAccountShowDialog" width="50%">
      <el-form ref="importForm" :model="importForm" :rules="importRules" label-width="80px" >
        <el-form-item label="帐号名" prop="name">
          <el-input v-model="importForm.name" placeholder="请输入帐号名，自定义填写，用于个人分辨帐号" />
        </el-form-item>
        <el-form-item label="选择类型" prop="isPrivate">
          <el-radio-group v-model="importForm.isPrivate">
            <el-radio label="1">私匙</el-radio>
            <el-radio label="0">助记词</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="importForm.isPrivate == '0'" label="助记词" prop="mnemonicStr">
          <el-input v-model="importForm.mnemonicStr" placeholder="请输入助记词，以空格隔开" />
        </el-form-item>
        <el-form-item v-if="importForm.isPrivate == '0'" label="类型" prop="type">
          <el-select v-model="importForm.type" placeholder="请选择类型">
            <el-option
              v-for="type in filTypes"
              :key="type"
              :label="type"
              :value="type">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item  v-if="importForm.isPrivate == '1'" label="私匙" prop="privateKey">
          <el-input type="textarea" v-model="importForm.privateKey" placeholder="请输入私匙" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="importAccount">确 定</el-button>
        <el-button @click="importAccountShowClose">取 消</el-button>
      </div>
    </el-dialog>


    <el-dialog v-loading="loading" title="帐号信息" :visible.sync="showDetailDialog" width="50%">
      <el-descriptions title="" border>
        <el-descriptions-item label="帐号名">{{ info.name }}</el-descriptions-item>
        <el-descriptions-item label="所属用户">{{ info.userName }}</el-descriptions-item>
        <el-descriptions-item label="帐号">{{ info.account }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ info.type }}</el-descriptions-item>
        <el-descriptions-item v-if="info.warningTip == null" label="余额">{{ info.balance }} {{ info.currency }}</el-descriptions-item>
        <el-descriptions-item v-else label="异常">{{ info.warningTip }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeShowDetail">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog v-loading="loading" title="修改帐号" :visible.sync="updateOpen" width="50%"  append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" >
        <el-form-item label="帐号名" prop="name">
          <el-input v-model="form.name" placeholder="请输入帐号名" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改钱包对话框 -->
    <el-dialog v-loading="loading" :title="title" :visible.sync="open" width="60%"  append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" >
        <el-steps simple :active="active" finish-status="success">
          <el-step title="提醒"></el-step>
          <el-step title="备份助记词"></el-step>
          <el-step title="创建帐号"></el-step>
        </el-steps>
        <div v-show="active == 0" style="padding: 30px" >
          助记词由英语单词组成，请抄写并妥善保管
          <br>
          一旦丢失助记词，将无法找回。请务必备份好助记词
          <br>
          如果您的设备丢失，可以使用助记词恢复，在开始之前，请准备好笔和纸
        </div>
        <div v-show="active == 1" style="padding: 30px">
          <el-tag style="margin: 10px; font-size: 16px" size="medium" v-for="m in this.form.mnemonic">{{ m }}</el-tag>
        </div>
        <div v-show="active == 2" style="padding: 30px">
          <el-form-item label="帐号名" prop="name">
            <el-input v-model="form.name" placeholder="请输入帐号名" />
          </el-form-item>
          <el-form-item label="类型" prop="type">
            <el-select v-model="form.type" placeholder="请选择类型">
              <el-option
                v-for="type in filTypes"
                :key="type"
                :label="type"
                :value="type">
              </el-option>
            </el-select>
          </el-form-item>
        </div>
        <el-button v-show="active > 0" style="margin-top: 12px;" @click="stepPrev">上一步</el-button>
        <el-button v-show="active < 2" style="margin-top: 12px;" @click="stepNext">下一步</el-button>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-show="active == 2" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listTbWallet,
  getTbWallet,
  delTbWallet,
  addTbWallet,
  updateTbWallet,
  exportTbWallet,
  genMnemonic,
  info,
  importAccount
} from "@/api/system/tbWallet";

export default {
  name: "TbWallet",
  components: {
  },
  data() {
    return {
      active: 0,
      importAccountShowDialog: false,
      showDetailDialog: false,
      updateOpen: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 钱包表格数据
      tbWalletList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        conf: null,
        index: null,
        address: null,
        account: null,
        gmtCreate: null,
        gmtModity: null,
        version: null
      },
      // 表单参数
      form: {
        id: null,
        name: null,
        type: "secp256k1",
        mnemonic: null,
      },
      // 表单校验
      rules: {
      },
      info: {
        warningTip: null,
        balance: 0,
        currency: "FIL"
      },
      filTypes:["secp256k1", "bls"],
      importForm: {
        name: null,
        isPrivate: "0",
        mnemonicStr: null,
        mnemonic: [],
        privateKey: null,
        type: "secp256k1",
      },

      importRules:{
        name: [
          { required: true, message: '请填写账户名', trigger: 'blur' }
        ],
        isPrivate: [
          { required: true, message: '请选择类型', trigger: 'blur' }
        ],
        mnemonicStr: [
          { required: true, message: '请填写助记词', trigger: 'blur' }
        ],
        privateKey: [
          { required: true, message: '请填写私匙', trigger: 'blur' }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    importAccountShow(){
      this.importAccountShowDialog = true;
    },
    importAccountShowClose(){
      this.reset();
      this.importAccountShowDialog = false;
    },
    importAccount(){
      this.$refs["importForm"].validate(valid => {
        if (valid) {
          if (this.importForm.isPrivate == 0){
            this.importForm.mnemonic = this.importForm.mnemonicStr.trim().split(" ");
          }
          importAccount(this.importForm).then(response => {
            this.$message.success("导入成功");
            this.getList();
            this.reset();
            this.importAccountShowDialog = false;
            window.location.reload();
          }).catch(error => {
            this.importAccountShowDialog = false;
          });
        }
      });
    },
    showDetail(row){
      this.loading = true;
      const id = row.id;
      info(id).then(response => {
        this.info = response.data;
        this.showDetailDialog = true;
        this.loading = false;
      });
    },
    closeShowDetail(){
      this.reset();
      this.showDetailDialog = false;
    },
    stepNext() {
      if (this.active == 0 && this.form.mnemonic == null){
        console.info("创建助记词");
        this.loading = true;
        genMnemonic().then(response => {
          console.info("data:" + response.data);
          this.form.mnemonic = response.data.split(" ");
          this.active++;
          this.loading = false;
        });
        return;
      }
      if (this.active++ > 2){
        this.active = 0;
      }
    },

    stepPrev(){
      if (this.active-- < 0){
        this.active = 0;
      }
    },
    /** 查询钱包列表 */
    getList() {
      this.loading = true;
      listTbWallet(this.queryParams).then(response => {
        this.tbWalletList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.updateOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        name: null,
        type: "secp256k1"
      };
      this.info = {
        warningTip: null,
        balance: 0,
        currency: "FIL"
      }
      this.importForm = {
        isPrivateKey: "false",
        mnemonic: null,
        privateKey: null,
        type: "secp256k1",
      }
      this.active = 0;
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加钱包";
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTbWallet(id).then(response => {
        this.form = response.data;
        this.updateOpen = true;
        this.title = "修改钱包";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTbWallet(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.updateOpen = false;
              this.getList();
              window.location.reload();
            });
          } else {
            this.loading = true;
            addTbWallet(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              this.loading = false;
              window.location.reload();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除钱包编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTbWallet(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
          window.location.reload();
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有钱包数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTbWallet(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
