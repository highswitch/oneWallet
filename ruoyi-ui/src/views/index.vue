<template>
  <div class="app-container home">
    <el-row :gutter="20" style="padding: 10px;">
      <el-card>
        <el-col v-if="isWallet" class="welecom-div" :span="12">
          <img :src="avatar" class="userAvatar">
          <div>
            <div class="welecom-text" >
              早安， Archer，祝你开心每一天!
            </div>
            <div class="welecom-time">
              今日晴，20℃ - 32℃！
            </div>
          </div>
        </el-col>
        <el-col v-else class="welecom-div" :span="12">
          <img :src="avatar" class="userAvatar">
          <div>
            <div class="welecom-text" >
              早安，管理员：Archer，祝你开心每一天!
            </div>
            <div class="welecom-time">
              今日晴，20℃ - 32℃！
            </div>
          </div>
        </el-col>
      </el-card>
    </el-row>

  </div>
</template>
<style>
.welecom-div{
  display: flex;
  align-items: center;
  padding: 10px;
}
.el-divider--vertical {
  display: inline-block;
  width: 1px;
  height: 1em;
  margin: 0 8px;
  vertical-align: middle;
  position: relative;
  border-left: 1px #0f1325 dashed;
}
.el-divider {
  position: relative;
}
.text-20px {
  font-size: 20px;
  line-height: 1;
}
.text-gray-400 {
  --tw-text-opacity: 1;
  color: rgba(156,163,175,var(--tw-text-opacity));
}
.mb-20px {
  margin-bottom: 20px;
}
.text-14px {
  font-size: 14px;
  line-height: 1;
}
.px-8px {
  padding-left: 8px;
  padding-right: 8px;
}
.text-right {
  text-align: right;
}
.welecom-rigit{
  height: 90px;
  justify-content: flex-end;
  align-items: center;
  display: flex;
}


.userAvatar{
  width: 70px;
  height: 70px;
  border-radius: 50%;
  margin-right: 20px;
}
.welecom-text{
  font-size: 20px;
  line-height: 1;
}
.welecom-time{
  color: rgba(107,114,128,1);
  margin-top: 10px;
  font-size: 14px;
  line-height: 1;
}
</style>

<script>
import PanelGroup from './dashboard/PanelGroup'
import Echarts from './index/Echarts'
import Withdrawal from './index/dialog/Withdrawal'
import store from "@/store";
import {isNotNull} from "@/api/default";
export default {
  name: "Index",
  components: {
    PanelGroup,
    Echarts,
    Withdrawal,
  },
  data() {
    return {
      isWallet: false,
      avatar: store.getters.avatar,
      // 版本号
      version: "3.8.1",
      dialogWithdrawalVisible: false,
      dialogHistoryVisible: false,
      withdrawalType: 0,

    };
  },
  created() {
    this.isWallet = this.isWalletUser();
  },
  methods: {
    isWalletUser(){
      console.info("iswalleter");
      let roles = store.getters.roles;
      console.info("roles:" + JSON.stringify(roles))
      if (!isNotNull(roles)){
        console.info("roles is null")
        return false;
      }
      if (roles.indexOf("wallet_user") != -1){
        console.info("roles is wallet")
        return true;
      } else {
        console.info("roles not wallet")
        return false;
      }
    },
    withdrawalTypeChange(event){
      this.withdrawalType = event;
    },
    goTarget(href) {
      window.open(href, "_blank");
    },
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    },
    onOpen() {
      this.dialogWithdrawalVisible = true
    },
    onOpenHistory() {
      this.dialogHistoryVisible = true
    },
    onClose() {
      this.$refs['elForm'].resetFields()
    },
    close() {
      this.dialogWithdrawalVisible = false
    },
    closeHistory() {
      this.dialogHistoryVisible = false
    },
    handleConfirm() {
      this.$refs['elForm'].validate(valid => {
        if (!valid) return
        this.close()
      })
    },
  }
};
</script>
