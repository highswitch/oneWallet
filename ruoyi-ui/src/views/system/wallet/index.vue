<template>
  <div v-loading="loading" class="app-container">
    <el-row class="name">
      <span>{{ walletInfo.name }}</span>
    </el-row>
    <el-row class="balance">
      <span>{{ walletInfo.balance }} FIL</span>
    </el-row>
    <el-row class="account">
      <span>帐号：{{ walletInfo.account }} <i v-clipboard:copy="walletInfo.account" v-clipboard:success="onCopy" class="el-icon-document-copy"></i></span>
    </el-row>
    <el-row class="operationBtns">
      <div @click="into" class="icon-list-opera">
        <i class="el-icon-bottom-right"></i>
        <span>收款</span>
      </div>
      <div @click="showOut" class="icon-list-opera">
        <i class="el-icon-s-promotion"></i>
        <span>转帐</span>
      </div>
    </el-row>
    <el-row style="padding: 0px 10%;">
      <el-tabs :stretch="true" v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="全部" name="all">
          <el-table v-loading="tableLoading" :data="tableData">
            <el-table-column label="操作" align="center" >
              <template slot-scope="scope">
                <span v-if="scope.row.formAccount == walletInfo.account">转出</span>
                <span v-if="scope.row.toAccount == walletInfo.account">转如</span>
              </template>
            </el-table-column>
            <el-table-column label="金额" align="center" prop="amount" >
              <template slot-scope="scope">
                {{ scope.row.amount }} FIL
              </template>
            </el-table-column>
            <el-table-column label="时间" align="center" prop="gmtCreate" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="转入" name="into">
          <el-table v-loading="tableLoading" :data="tableData">
            <el-table-column label="操作" align="center" >
              <template slot-scope="scope">
                <span v-if="scope.row.formAccount == walletInfo.account">转出</span>
                <span v-if="scope.row.toAccount == walletInfo.account">转如</span>
              </template>
            </el-table-column>
            <el-table-column label="金额" align="center" prop="amount" >
              <template slot-scope="scope">
                {{ scope.row.amount }} FIL
              </template>
            </el-table-column>
            <el-table-column label="时间" align="center" prop="gmtCreate" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="转帐" name="out">
          <el-table v-loading="tableLoading" :data="tableData">
            <el-table-column label="操作" align="center" >
              <template slot-scope="scope">
                <span v-if="scope.row.formAccount == walletInfo.account">转出</span>
                <span v-if="scope.row.toAccount == walletInfo.account">转如</span>
              </template>
            </el-table-column>
            <el-table-column label="金额" align="center" prop="amount" >
              <template slot-scope="scope">
                {{ scope.row.amount }} FIL
              </template>
            </el-table-column>
            <el-table-column label="时间" align="center" prop="gmtCreate" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-row>
    <el-dialog
    title="帐号二维码"
    :visible.sync="showAccountQrCode"
    width="400px"
    >
    <div>
      <el-row style="text-align: center">
        <vue-qr :size="qrCodeSize" :text="walletInfo.account" :callback="qrCallBack" qid="qrId"></vue-qr>
      </el-row>
    </div>
    </el-dialog>

    <el-dialog
      title="转帐"
      :visible.sync="showOutDialog"
      width="50%"
    >
      <el-form ref="outForm" v-loading="outLoading" :model="outForm" :rules="rules" label-width="80px">
        <el-form-item label="接收人" prop="to">
          <el-row>
            <el-col :span="20">
              <el-input v-model="outForm.toAccount" placeholder="请输入接收人，接收人帐号信息" />
            </el-col>
            <el-col :span="4">
              <el-dropdown @command="handleCommand" style="margin-left: 10px; cursor: pointer" >
                <span class="el-dropdown-link">
                  选择帐号
                </span>
                <el-dropdown-menu class="cus-dropdown-menu" slot="dropdown">
                  <el-dropdown-item
                    v-for="wallet in walletList"
                    v-if="walletInfo.account != wallet.account"
                    :command="wallet.account"
                  >
                    {{ wallet.name }} ( {{ showAccount(wallet.account) }} )
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="转帐金额" prop="amount">
          <el-input v-model="outForm.amount" placeholder="请输入转帐金额" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitOut">确 定</el-button>
        <el-button @click="cancelOut">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {getBindWalletInfo, getWalletListByUser, showAccount} from "@/api/system/tbWallet";
import VueQr from 'vue-qr'
import {addTbTransfer, listByAccount} from "@/api/system/tbTransfer";
export default {
  name: "wallet",
  components: {VueQr},
  data(){
    return {
      outLoading: false,
      walletList: [],
      showOutDialog: false,
      qrCodeSize: 300,
      qrCodeText: "",
      showAccountQrCode: false,
      loading: true,
      tableLoading: false,
      stretch: true,
      activeName: "all",
      test: "测试",
      walletInfo: {
        name: null,
        account: null,
        type: null,
        balance: null
      },
      outForm: {
        walletId: null,
        toAccount: null,
        amount: null,
      },
      transferForm: {
        type: "all",
        account: null,
      },
      tableData: [],
      rules: {
      },
    }
  },
  created() {
    this.getWalletList();
    this.getBindWalletInfo();
    this.qrCodeText = this.walletInfo.account;
  },
  methods: {
    getTransferList(){
      this.tableLoading = true;
      this.transferForm.account = this.walletInfo.account;
      listByAccount(this.transferForm).then(response => {
        this.tableData = response.data;
        this.tableLoading = false;
      }).catch(err => {
        this.tableLoading = false;
      });
    },
    submitOut(){
      this.outLoading = true;
      addTbTransfer(this.outForm).then(response => {
        this.msgSuccess("新增成功");
        this.outLoading = false;
        this.showOutDialog = false;
        this.getList();
      }).catch(error => {
        this.outLoading = false;
      });
    },
    cancelOut(){
      this.showOutDialog = false;
      this.outForm = {
        walletId: null,
        toAccount: null,
        amount: null,
      }
    },
    showOut(){
      this.outForm.walletId = this.walletInfo.id;
      this.showOutDialog = true;
    },
    handleCommand(command){
      this.outForm.toAccount = command;
    },
    getWalletList(){
      getWalletListByUser().then(response => {
        this.walletList = response.data;
      })
    },
    qrCallBack(url, id){

    },
    into(){
      console.info("into~")
      this.showAccountQrCode = true;
    },
    onCopy: function (e){
      this.msgSuccess('复制成功');
    },

    handleClick(tab, event) {
      this.tableLoading = true;
      this.transferForm.type = tab.name;
      this.getTransferList();
    },
    getBindWalletInfo(){
      this.loading = true;
      getBindWalletInfo().then(response => {
        this.walletInfo = response.data;
        this.loading = false;
        this.getTransferList();
      });
    },
    showAccount(account){
      return showAccount(account);
    },
  }
}
</script>
<style lang="scss">
  .name{
    text-align: center;
    margin-top: 10px;
    font-size: 18px;
    font-weight: bold;
  }
  .balance{
    text-align: center;
    margin-top: 10px;
    font-size: 34px;
    font-weight: bold;
  }
  .account{
    text-align: center;
    margin-top: 10px;
    font-size: 16px;
    font-weight: bold;
    i {
      cursor: pointer;
    }
  }
  .operationBtns{
    text-align: center;
  }
  .icon-list-opera{
    display: inline-block;
    cursor: pointer;
    color: #606266;
    margin: 20px;
    i{
      margin: 10px;
      font-size: 32px;
      display: block;
    }
    display: inline-block;
    width: 150px;
    height: 100px;
  }
  .el-tabs__header{
    margin: 0px;
  }
</style>
