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
      <div @click="info" class="icon-list-opera">
        <i class="el-icon-bottom-right"></i>
        <span>多签信息</span>
      </div>
      <div @click="proposal" class="icon-list-opera">
        <i class="el-icon-s-promotion"></i>
        <span>转帐</span>
      </div>
    </el-row>
    <el-row style="padding: 0px 10%;">
      <el-tabs :stretch="true" v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="全部" name="all">

        </el-tab-pane>
      </el-tabs>
    </el-row>

  </div>
</template>
<script>
export default {
  name: "wallMsigInfo",
  data(){
    return {
      activeName: "all",
      loading: false,
      walletInfo: {
        name: null,
        balance: null,
        account: null
      }
    }
  },
  created() {

  },
  methods: {
    init(id){
      console.info("wallet-msig-info:" + id);
    },
    info(){

    },
    proposal(){

    },
    onCopy(){
      this.msgSuccess('复制成功');
    },
    handleClick(tab, event) {
      this.tableLoading = true;
      console.info("tab:" + tab.name);
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
