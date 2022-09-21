<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="帐号名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入帐号名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
      <el-form-item label="验证用户" prop="requiredUserNum">
        <el-input
          v-model="queryParams.requiredUserNum"
          placeholder="请输入验证用户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="版本" prop="version">
        <el-input
          v-model="queryParams.version"
          placeholder="请输入版本"
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
          v-hasPermi="['system:tbWalletMsig:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:tbWalletMsig:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:tbWalletMsig:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          @click="handleExport"
          v-hasPermi="['system:tbWalletMsig:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tbWalletMsigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="帐号名" align="center" prop="name" />
      <el-table-column label="创建者" align="center" prop="userName" />
      <el-table-column label="地址ID" align="center" prop="accountCode" />
      <el-table-column label="地址" align="center" prop="account" />
      <el-table-column label="验证用户" align="center" prop="requiredUserNum" />
      <el-table-column label="创建时间" align="center" prop="gmtCreate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.gmtCreate, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            type="text"
            @click="showDetail(scope.row)"
            v-hasPermi="['system:tbWalletMsig:edit']"
          >详细信息</el-button>
          <el-button
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:tbWalletMsig:edit']"
          >修改</el-button>
          <el-button
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tbWalletMsig:remove']"
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

    <!-- 添加或修改多签钱包对话框 -->
    <el-dialog v-loading="loading" :element-loading-text="loadText" :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="帐号名" prop="name">
          <el-input v-model="form.name" placeholder="请输入帐号名" />
        </el-form-item>

        <el-form-item v-if="this.form.id == null" label="签署地址" >
          <el-row style="margin-bottom: 10px" v-for="signer in signerList">
            <el-col :span="16">
              <el-input v-model="signer.account" placeholder="请输入帐号名" />
            </el-col>
            <el-col v-if="signer.type == 'owner'" :span="4">
              (我的地址)
            </el-col>
            <el-col v-if="signer.type == 'default'" :span="8">
              <el-dropdown st @command="handleCommand" style="margin-left: 10px; cursor: pointer" >
                <span class="el-dropdown-link">
                  选择帐号
                </span>
                <el-dropdown-menu class="cus-dropdown-menu" slot="dropdown">
                  <el-dropdown-item
                    v-for="wallet in walletList"
                    :disabled="isDisabledAddMsig(wallet.account)"
                    :command="commandValue(wallet.account, signer.index)"
                  >
                    {{ wallet.name }} ( {{ showAccount(wallet.account) }} )
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
              <el-button @click="removeSignerAccount(signer.index)" style="margin-left: 10px" size="mini" type="danger">移除</el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-button @click="addSignerAccount" >新增地址</el-button>
          </el-row>
        </el-form-item>
        <el-form-item v-if="this.form.id == null" label="验证数量" prop="requiredUserNum">
          <el-input type="number" v-model="form.requiredUserNum" placeholder="请输入验证数量" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog v-loading="loading" :element-loading-text="loadText" title="多签帐号信息" :visible.sync="openDetail" width="70%" append-to-body>
      <el-descriptions title="" border>
        <el-descriptions-item label="帐号名">{{ detail.name }}</el-descriptions-item>
        <el-descriptions-item label="所属用户">{{ detail.userName }}</el-descriptions-item>
        <el-descriptions-item label="帐号ID">{{ detail.accountCode }}</el-descriptions-item>
        <el-descriptions-item label="帐号">{{ detail.account }}</el-descriptions-item>
        <el-descriptions-item label="余额">{{ detail.detail.balance }}</el-descriptions-item>
        <el-descriptions-item label="可用余额">{{ detail.detail.spendable }}</el-descriptions-item>
        <el-descriptions-item label="阈值">{{ detail.detail.threshold }}
          <el-button
            style="margin-left: 30px"
            size="mini"
            type="text"
            @click="showEditThresholdDialog(detail.id, detail.requiredUserNum )"
          >修改阈值</el-button>
        </el-descriptions-item>
      </el-descriptions>
      <h3>签署人:</h3>
      <el-table
        :data="detail.detail.signers"
        stripe
        style="width: 100%">
        <el-table-column label="ID" align="center" prop="id" />
        <el-table-column label="地址" align="center" prop="address" />
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              type="danger"
              @click="removeSigner(detail.id, scope.row.address)"
            >移 除</el-button>
            <el-button
              type="danger"
              @click="showSignerDialog(detail.id, true, scope.row.address)"
            >修改</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-button
        style="margin-top: 10px"
        @click="showSignerDialog(detail.id, false, null)"
      >新增签署人</el-button>

      <h3>提案:</h3>
      <el-table
        :data="detail.detail.transactions"
        stripe
        style="width: 100%">
        <el-table-column label="ID" align="center" prop="id" />
        <el-table-column label="状态" align="center" prop="state" />
        <el-table-column label="批准" align="center" prop="approvals" />
        <el-table-column label="To" align="center" prop="to" />
        <el-table-column label="金额" align="center" prop="value" />
        <el-table-column label="方法" align="center" prop="method" />
        <el-table-column label="参数" align="center" prop="params" />
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button style="color: green" v-if="scope.row.proposal"
                       type="text"
            >已允许</el-button>

            <el-button v-if="!scope.row.proposal"
              type="text"
              @click="approveTask(detail.id, scope.row.id, scope.row.method)"
            >允许</el-button>
            <el-button
              type="text"
              @click="cancelTask(detail.id, scope.row.id, scope.row.method)"
            >关闭</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelDetail">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog v-loading="loading" :element-loading-text="loadText" title="修改阈值" :visible.sync="showEditThreshold" width="70%" append-to-body>
      <el-form ref="form" :model="editThresholdForm" :rules="rules" label-width="80px">
        <el-form-item label="阈值" prop="threshold">
          <el-input v-model="editThresholdForm.threshold" placeholder="请输入阈值" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="editThreshold">确 定</el-button>
        <el-button @click="cancelEditThreshold">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog v-loading="loading" :element-loading-text="loadText" :title="signersDialogTitle" :visible.sync="showEditSigners" width="50%" append-to-body>
      <el-form ref="form" :model="signersForm" :rules="rules" label-width="80px">

        <el-form-item label="选择地址" prop="signer">
          <el-row>
            <el-col :span="16">
              <el-input v-model="signersForm.signer" placeholder="请输入地址" />
            </el-col>
            <el-col :span="8">
              <el-dropdown st @command="handleCommandEditSigner" style="margin-left: 10px; cursor: pointer" >
                <span class="el-dropdown-link">
                  选择帐号
                </span>
                <el-dropdown-menu class="cus-dropdown-menu" slot="dropdown">
                  <el-dropdown-item
                    v-for="wallet in walletList"
                    :command="wallet.account"
                    :disabled="isDisabled(wallet.account)"
                  >
                    {{ wallet.name }} ( {{ showAccount(wallet.account) }} )
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-col>
          </el-row>

        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="editSigners">确 定</el-button>
        <el-button @click="cancelSigners">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listTbWalletMsig,
  getTbWalletMsig,
  delTbWalletMsig,
  addTbWalletMsig,
  updateTbWalletMsig,
  exportTbWalletMsig,
  getTbWalletMsigDetail,
  removeSigner,
  cancelTask, editThreshold, addSigners, editSigners, cmdTimeInterVal,
  approveTask
} from "@/api/system/tbWalletMsig";
import {getBindWalletInfo, getWalletListByUser, showAccount} from "@/api/system/tbWallet";
import {getExecuteRet} from "@/api/system/LinuxCmd";
import {getWalletBindInfo} from "@/api/system/tbWalletBind";
import {isNotNull} from "@/api/default";
import walletMsigInfo from "@/views/system/tbWalletMsig/walletMsigInfo";

export default {
  name: "TbWalletMsig",
  components: {
    walletMsigInfo
  },
  data() {
    return {
      walletBindInfo: {
        walletId: null,
        account: null,
      },
      signerList: [],
      signersDialogTitle: "",
      showEditSigners: false,
      showEditThreshold: false,
      openDetail: false,
      // 遮罩层
      loading: true,
      loadText: "加载中",
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
      // 多签钱包表格数据
      tbWalletMsigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        userId: null,
        address: null,
        account: null,
        requiredUserNum: null,
        signers: null,
        gmtCreate: null,
        gmtModity: null,
        version: null
      },
      walletList: [],
      signers: [],
      // 表单参数
      form: {
        formWalletId: null,
        signerList: [],
        requiredUserNum: null
      },
      signersForm: {
        id: null,
        signer: null,
        isEdit: false,
        oldSigner: null,
        walletId: null,
      },
      editThresholdForm: {
        id: 0,
        threshold: 0,
        walletId: null,
      },
      detail: {
        detail: {}
      },
      taskForm: {
        id: null,
        taskId: null,
        walletId: null,
        method: null
      },
      // 表单校验
      rules: {
        name: [
          {  required: true, message: '请输入名称', trigger: 'blur' }
        ],
        requiredUserNum: [
          { required: true, message: '请输入验证数量', trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getWalletList();
    this.getWalletBindInfo();
  },
  methods: {
    addSignerAccount(){
      let signer = {
        index: this.signerList.length,
        account: "",
        type: "default"
      }
      this.signerList.push(signer);
    },
    removeSignerAccount(index){
      this.signerList.splice(index, 1);
    },
    commandValue(account, index){
      return {
        account: account,
        index: index
      }
    },
    handleCommand(command){
      this.signerList[command.index].account = command.account;
    },
    handleCommandEditSigner(command){
      this.signersForm.signer = command;
    },
    getWalletBindInfo() {
      getWalletBindInfo().then(response => {
        this.walletBindInfo = response.data;
      });
    },
    showSignerDialog(id, isEdit, oldSigner){
      this.signers = [];
      for (let i = 0; i < this.detail.detail.signers.length; i++) {
        console.info("address:" + this.detail.detail.signers[i].address);
        this.signers.push(this.detail.detail.signers[i].address)
      }
      console.info("signers:" + JSON.stringify(this.detail.detail.signers));
      console.info("signers:" + JSON.stringify(this.signers));
      this.signersForm.id = id;
      this.signersForm.isEdit = isEdit;
      if (isEdit){
        this.signersForm.oldSigner = oldSigner;
      }
      this.showEditSigners = true;
    },
    editSigners(){
      this.loading = true;
      const that = this;
      let id = this.signersForm.id;
      this.signersForm.walletId = this.walletBindInfo.walletId;
      if (this.signersForm.isEdit){
        editSigners(this.signersForm).then(response => {
          const uuid = response.msg;
          cmdTimeInterVal(uuid, that, function () {
            that.$message.success("已提交提案");
            that.cancelSigners();
            that.reloadDetail(id);
          }, function (msg){
            that.$message.error("提交失败：" + msg);
            that.loading = false;
          });

        });
      } else {
        addSigners(this.signersForm).then(response => {
          const uuid = response.msg;
          cmdTimeInterVal(uuid, that, function () {
            that.$message.success("已提交提案");
            that.cancelSigners();
            that.reloadDetail(id);
          }, function (msg){
            that.$message.error("提交失败：" + msg);
            that.loading = false;
          });
        });
      }

    },
    cancelSigners(){
      this.showEditSigners = false;
      this.resetSigner();
    },
    isDisabled(account){
      if (this.signers.indexOf(account) != -1){
        return true;
      } else {
        return false;
      }
    },
    isDisabledAddMsig(account){
      let signerList = JSON.stringify(this.signerList);
      if (signerList.indexOf(account) != -1){
        return true;
      } else {
        return false;
      }
    },
    showEditThresholdDialog(id, threshold){
      this.editThresholdForm.id = id;
      this.editThresholdForm.threshold = threshold;
      this.showEditThreshold = true;
    },
    editThreshold(){
      const that = this;
      this.loading = true;
      let id = this.editThresholdForm.id;
      this.editThresholdForm.walletId = this.walletBindInfo.walletId;
      editThreshold(this.editThresholdForm).then(response => {
        const uuid = response.msg;
        cmdTimeInterVal(uuid, that, function () {
          that.$message.success("已提交提案");
          that.cancelEditThreshold();
          that.reloadDetail(id);
        }, function (msg){
          that.$message.error("提交失败：" + msg);
          that.loading = false;
        });
      }).catch(error => {
        this.loading = false;
      });
    },
    cancelEditThreshold(){
      this.resetThreshold();
      this.showEditThreshold = false;
    },
    approveTask(id, taskId, method){
      const that = this;
      this.$confirm('是否确认执行该操作?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        that.loading = true;
        that.taskForm.id = id;
        that.taskForm.taskId = taskId;
        that.taskForm.walletId = that.walletBindInfo.walletId;
        that.taskForm.method = method;

        approveTask(that.taskForm).then(response => {
          const uuid = response.msg;
          cmdTimeInterVal(uuid, that, function () {
            that.$message.success("处理完成");
            that.resetTaskForm();
            that.reloadDetail(id);
          }, function (msg){
            that.$message.error("处理失败：" + msg);
            that.loading = false;
          });
        });
      }).catch(err => {

      })
    },
    cancelTask(id, taskId, method){
      const that = this;
      this.$confirm('是否确认执行该操作?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        that.loading = true;
        that.taskForm.id = id;
        that.taskForm.taskId = taskId;
        that.taskForm.walletId = that.walletBindInfo.walletId;
        that.taskForm.method = method;
        cancelTask(that.taskForm).then(response => {
          const uuid = response.msg;
          cmdTimeInterVal(uuid, that, function () {
            that.$message.success("处理完成");
            that.resetTaskForm();
            that.reloadDetail(id);
          }, function (msg){
            that.$message.error("处理失败：" + msg);
            that.loading = false;
          });
        });
      }).catch(err => {

      })
    },
    removeSigner(id, account){
      const that = this;
      this.$confirm('是否确认执行该操作?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        that.loading = true;
        removeSigner(id, account, that.walletBindInfo.walletId).then(response => {
          const uuid = response.msg;
          cmdTimeInterVal(uuid, that, function () {
            that.$message.success("已提交提案");
            that.reloadDetail(id);
          }, function (msg){
            that.$message.error("提交失败：" + msg);
            that.loading = false;
          });
        });
      }).catch(err => {
        that.loading = false;
      })

    },
    getWalletList(){
      getWalletListByUser().then(resposne => {
        this.walletList = resposne.data;
      });
    },
    /** 查询多签钱包列表 */
    getList() {
      this.loading = true;
      listTbWalletMsig(this.queryParams).then(response => {
        this.tbWalletMsigList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancelDetail(){
      this.reset();
      this.openDetail = false;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    resetTaskForm(){
      this.taskForm = {
        id: null,
        walletId: null,
        taskId: null,
        method: null
      }
    },
    resetSigner(){
      this.signersForm = {
        id: null,
        signer: null,
        isEdit: false,
        oldSigner: null,
        walletId: null
      }
    },
    resetThreshold(){
      this.editThresholdForm = {
        id: 0,
        threshold: 0,
        walletId: null
      }
    },
    // 表单重置
    reset() {

      this.detail = {
        detail: {}
      },
      this.form = {
        id: null,
        name: null,
        userId: null,
        address: null,
        account: null,
        requiredUserNum: null,
        signers: null,
        gmtCreate: null,
        gmtModity: null,
        version: null
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
      this.signerList = [];
      let signer = {
        index: this.signerList.length,
        account: this.walletBindInfo.walletAccount,
        type: "owner"
      }
      this.signerList.push(signer);
      this.open = true;
      this.title = "添加多签钱包";
    },
    showDetail(row){
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      let walletId = this.walletBindInfo.walletId;
      getTbWalletMsigDetail(id, walletId).then(response => {
        this.detail = response.data;
        this.openDetail = true;
        this.loading = false;
      }).catch(error => {
        this.loading = false;
      });
    },
    reloadDetail(id){
      this.loadText = "获取多签信息"
      let walletId = this.walletBindInfo.walletId;
      getTbWalletMsigDetail(id, walletId).then(response => {
        this.detail = response.data;
        this.openDetail = true;
        this.loading = false;
      }).catch(error => {
        this.loading = false;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTbWalletMsig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改多签钱包";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTbWalletMsig(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.loading = true;
            const that = this;
            this.form.signerList = [];
            for (let i = 0; i < this.signerList.length; i++) {
              let account = this.signerList[i].account;
              if (isNotNull(account)){
                this.form.signerList.push(account);
              }
            }
            if (this.form.signerList == null || this.form.signerList.length == 0){
              this.msgError("请选择签署地址");
              this.loading = false;
              return;
            }
            if (this.form.requiredUserNum > this.form.signerList.length){
              this.msgError("验证数量不可大于签署地址数量");
              this.loading = false;
              return;
            }
            this.form.formWalletId = this.walletBindInfo.walletId;
            addTbWalletMsig(this.form).then(response => {
              const uuid = response.msg;
              cmdTimeInterVal(uuid, that, function () {
                that.$message.success("新增多签帐号成功");
                that.loading = false;
                that.open = false;
                that.getList();
              }, function (msg){
                that.$message.error("新增多签帐号失败：" + msg);
                that.loading = false;
              });
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除多签钱包编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTbWalletMsig(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有多签钱包数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTbWalletMsig(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    showAccount(account){
      return showAccount(account);
    },
  }
};
</script>
<style>
.el-loading-mask{
  position: fixed !important;
  height: 100%;
}
</style>
