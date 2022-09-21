<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />

    <div class="right-menu">
      <div v-if="walletBindInfo != null" class="cus-dropdown">
        <el-dropdown @command="handleCommand" v-if="walletBindInfo.walletId != null">
          <span class="el-dropdown-link">
            {{ this.walletBindInfo.walletName }}({{ showAccount(this.walletBindInfo.walletAccount) }})<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu class="cus-dropdown-menu" slot="dropdown">
            <el-dropdown-item
              v-if="wallet.id != walletBindInfo.walletId"
              v-for="wallet in walletList"
              :command="wallet.id"
            >
              {{ wallet.name }} ( {{ showAccount(wallet.account) }} )
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>


      <template v-if="device!=='mobile'">

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>

      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import { getWalletBindInfo, switchWalletAccount } from "@/api/system/tbWalletBind";
import {getWalletListByUser, showAccount} from "@/api/system/tbWallet";

export default {
  data(){
    return {
      walletBindInfo: {
        walletId: null,
        walletName: null,
        walletAccount: null
      },
      walletList: [],
    }
  },
  components: {
    Breadcrumb,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    }
  },
  created() {
    this.getWalletBindInfo();
    this.getWalletList();
  },
  methods: {
    handleCommand(command){
      this.$confirm('确定切换帐号？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        switchWalletAccount(command).then(response => {
          window.location.reload();
        })
      }).catch(err => {

      });
    },
    showAccount(account){
      return showAccount(account);
    },
    getWalletList(){
      getWalletListByUser().then(response => {
        this.walletList = response.data;
      })
    },
    getWalletBindInfo(){
      getWalletBindInfo().then(response => {
        this.walletBindInfo = response.data;
      })
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.cus-dropdown-menu{
  margin-top: -15px;
  line-height: 38px;
  .el-dropdown-menu__item{
    font-size: 18px !important;
    line-height: 38px;
    padding: 0 25px;
  }
}
.navbar {
  height: 70px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 76px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }
  .left-menu {
    float: left;
    margin-left: 20px;
    line-height: 70px;
    font-weight: 400;
    vertical-align: bottom;

  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 76px;
    .cus-dropdown{
      display: inline-block;
      height: 76px;
      line-height: 76px;
      margin-right: 30px;
      vertical-align: top;
      .el-dropdown{
        color: #97a8be;
        font-size: 18px !important;
      }
    }
    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
