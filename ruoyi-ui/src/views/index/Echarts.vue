<template>
  <el-row>
    <el-col :span="12" class="margin-top-small">
      <el-card>
        <div slot="header" class="clearfix">
          <span>近期数据</span>
        </div>
        <el-row>
          <el-col :span="1"><h2 style="margin: 10px;">0</h2></el-col>
          <el-col :span="22">
            <el-form :inline="true" :model="recentData" class="demo-form-inline">
              <el-col :span="4">
                <el-form-item>
                  <el-select @change="onSubmit" v-model="recentData.type" placeholder="类型">
                    <el-option label="注册" value="0"></el-option>
                    <el-option label="首次入金" value="1"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="时间范围">
                  <el-select @change="onSubmit" v-model="recentData.timeRang" placeholder="时间范围">
                    <el-option label="最近7天" value="0"></el-option>
                    <el-option label="最近30天" value="1"></el-option>
                    <el-option label="本月" value="2"></el-option>
                    <el-option label="上月" value="3"></el-option>
                    <el-option label="本年度" value="4"></el-option>
                    <el-option label="去年度" value="5"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-form>
          </el-col>
        </el-row>
        <el-row>
            <RecentDataLine></RecentDataLine>
        </el-row>
      </el-card>
    </el-col>

    <el-col :span="12" class="margin-top-small">
      <el-card>
        <div slot="header" class="clearfix">
          <span>直客佣金</span>
        </div>
        <el-row>
          <el-col :span="1"><h2 style="margin: 10px;">0</h2></el-col>
          <el-col :span="22">
            <el-form :inline="true" :model="straightGuestData" class="demo-form-inline">
              <el-col :span="4">
                <el-form-item>
                  <el-select @change="onSubmit" v-model="straightGuestData.type" placeholder="类型">
                    <el-option label="USDT" value="USDT"></el-option>
                    <el-option label="BTC" value="BTC"></el-option>
                    <el-option label="XRP" value="XRP"></el-option>
                    <el-option label="EOS" value="EOS"></el-option>
                    <el-option label="ETH" value="ETH"></el-option>
                    <el-option label="DOT" value="DOT"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="时间范围">
                  <el-select @change="onSubmit" v-model="straightGuestData.timeRang" placeholder="时间范围">
                    <el-option label="最近7天" value="0"></el-option>
                    <el-option label="最近30天" value="1"></el-option>
                    <el-option label="本月" value="2"></el-option>
                    <el-option label="上月" value="3"></el-option>
                    <el-option label="本年度" value="4"></el-option>
                    <el-option label="去年度" value="5"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-form>
          </el-col>
        </el-row>
        <el-row>
          <StraightGuest></StraightGuest>
        </el-row>
      </el-card>
    </el-col>

    <el-col :span="12" class="margin-top-small">
      <el-card>
        <div slot="header" class="clearfix">
          <span>子代理佣金</span>
        </div>
        <el-row>
          <el-col :span="1"><h2 style="margin: 10px;">0</h2></el-col>
          <el-col :span="22">
            <el-form :inline="true" :model="childAgentData" class="demo-form-inline">
              <el-col :span="4">
                <el-form-item>
                  <el-select @change="onSubmit" v-model="childAgentData.type" placeholder="类型">
                    <el-option label="USDT" value="USDT"></el-option>
                    <el-option label="BTC" value="BTC"></el-option>
                    <el-option label="XRP" value="XRP"></el-option>
                    <el-option label="EOS" value="EOS"></el-option>
                    <el-option label="ETH" value="ETH"></el-option>
                    <el-option label="DOT" value="DOT"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="时间范围">
                  <el-select @change="onSubmit" v-model="childAgentData.timeRang" placeholder="时间范围">
                    <el-option label="最近7天" value="0"></el-option>
                    <el-option label="最近30天" value="1"></el-option>
                    <el-option label="本月" value="2"></el-option>
                    <el-option label="上月" value="3"></el-option>
                    <el-option label="本年度" value="4"></el-option>
                    <el-option label="去年度" value="5"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-form>
          </el-col>
        </el-row>
        <el-row>
          <GuestTransLine></GuestTransLine>
        </el-row>
      </el-card>
    </el-col>

    <el-col :span="12" class="margin-top-small">
      <el-card>
        <div slot="header" class="clearfix">
          <span>直客交易量</span>
        </div>
        <el-row>
          <el-col :span="1"><h2 style="margin: 10px;">0</h2></el-col>
          <el-col :span="22">
            <el-form :inline="true" :model="guestTransData" class="demo-form-inline">
              <el-col :span="4">
                <el-form-item>
                  <el-select @change="onSubmit" v-model="guestTransData.type" placeholder="类型">
                    <el-option label="USDT" value="USDT"></el-option>
                    <el-option label="BTC" value="BTC"></el-option>
                    <el-option label="XRP" value="XRP"></el-option>
                    <el-option label="EOS" value="EOS"></el-option>
                    <el-option label="ETH" value="ETH"></el-option>
                    <el-option label="DOT" value="DOT"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="时间范围">
                  <el-select @change="onSubmit" v-model="guestTransData.timeRang" placeholder="时间范围">
                    <el-option label="最近7天" value="0"></el-option>
                    <el-option label="最近30天" value="1"></el-option>
                    <el-option label="本月" value="2"></el-option>
                    <el-option label="上月" value="3"></el-option>
                    <el-option label="本年度" value="4"></el-option>
                    <el-option label="去年度" value="5"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-form>
          </el-col>
        </el-row>
        <el-row>
          <ChildAgentLine></ChildAgentLine>
        </el-row>
      </el-card>
    </el-col>
  </el-row>


</template>
<script>
  import RecentDataLine from './echarts/RecentDataLine'
  import StraightGuest from './echarts/StraightGuest'
  import GuestTransLine from './echarts/GuestTransLine'
  import ChildAgentLine from './echarts/ChildAgentLine'
  export default {
    components: {
      RecentDataLine,
      StraightGuest,
      GuestTransLine,
      ChildAgentLine
    },
    data() {
      return {
        recentData: {
          type: '0',
          timeRang: '0'
        },
        straightGuestData: {
          type: 'USDT',
          timeRang: '0'
        },
        childAgentData: {
          type: 'USDT',
          timeRang: '0'
        },
        guestTransData: {
          type: 'USDT',
          timeRang: '0'
        }
      }
    },
    methods: {
      onSubmit() {
        console.log('submit!');
      },
    }
  }
</script>
