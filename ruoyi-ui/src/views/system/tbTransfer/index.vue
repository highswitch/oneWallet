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
      <el-form-item label="钱包ID" prop="walletId">
        <el-input
          v-model="queryParams.walletId"
          placeholder="请输入钱包ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发起人" prop="form">
        <el-input
          v-model="queryParams.form"
          placeholder="请输入发起人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接收人" prop="to">
        <el-input
          v-model="queryParams.to"
          placeholder="请输入接收人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="转帐信息" prop="amount">
        <el-input
          v-model="queryParams.amount"
          placeholder="请输入转帐信息"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="gmtCreate">
        <el-date-picker clearable
          v-model="queryParams.gmtCreate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="修改时间" prop="gmtModity">
        <el-date-picker clearable
          v-model="queryParams.gmtModity"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择修改时间">
        </el-date-picker>
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
          v-hasPermi="['system:tbTransfer:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:tbTransfer:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:tbTransfer:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          @click="handleExport"
          v-hasPermi="['system:tbTransfer:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tbTransferList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户" align="center" prop="userName" />
      <el-table-column label="钱包" align="center" prop="walletName" />
      <el-table-column label="发起帐号" align="center" prop="formAccount" />
      <el-table-column label="接收帐号" align="center" prop="toAccount" />
      <el-table-column label="转帐金额" align="center" prop="amount" />
      <el-table-column label="创建时间" align="center" prop="gmtCreate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.gmtCreate, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作人" align="center" prop="operationUserName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            type="text"
            @click="goToDetail(scope.row.messageId)"
            v-hasPermi="['system:tbTransfer:edit']"
          >详细信息</el-button>

          <el-button
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tbTransfer:remove']"
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

    <!-- 添加或修改转帐对话框 -->
    <el-dialog v-loading="loading" :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="选择帐号" prop="walletId">
          <el-select style="width: 100%;" v-model="form.walletId" placeholder="请选择帐号">
            <el-option v-for="wallet in walletList"
                       :key="wallet.id"
                       :label="wallet.name + ' ( ' + wallet.account + ' ) '"
                       :value="wallet.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="接收人" prop="to">
          <el-input v-model="form.toAccount" placeholder="请输入接收人，接收人帐号信息" />
        </el-form-item>
        <el-form-item label="转帐金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入转帐金额" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTbTransfer, getTbTransfer, delTbTransfer, addTbTransfer, updateTbTransfer, exportTbTransfer } from "@/api/system/tbTransfer";
import { getWalletListByUser } from "@/api/system/tbWallet";
export default {
  name: "TbTransfer",
  components: {
  },
  data() {
    return {
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
      // 转帐表格数据
      tbTransferList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        walletId: null,
        form: null,
        to: null,
        amount: null,
        gmtCreate: null,
        gmtModity: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      walletList: [],
    };
  },
  created() {
    this.getList();
    this.getWalletList();
  },
  methods: {
    goToDetail(url){
      window.open(url, "_blank");
    },
    getWalletList(){
      getWalletListByUser().then(response => {
        this.walletList = response.data;
      });
    },
    /** 查询转帐列表 */
    getList() {
      this.loading = true;
      listTbTransfer(this.queryParams).then(response => {
        this.tbTransferList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        userId: null,
        walletId: null,
        form: null,
        to: null,
        amount: null,
        gmtCreate: null,
        gmtModity: null
      };
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
      this.title = "添加转帐";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTbTransfer(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改转帐";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTbTransfer(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.loading = true;
            addTbTransfer(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.loading = false;
              this.open = false;
              this.getList();
            }).catch(error => {
              this.loading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除转帐编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTbTransfer(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有转帐数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTbTransfer(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
