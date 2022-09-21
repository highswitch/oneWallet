<template>
  <div :id="myChart" :style="{width: '100%', height: '300px'}"></div>
</template>

<script>
import customed from "@/api/tool/echats/customed"
export default {
  name: 'echarts',
  props: ["id"],
  data () {
    return {
      myChart: this.id,
      msg: 'Welcome to Your Vue.js App'
    }
  },
  created() {
    // TODO 修改ID没有生效
    this.init();
  },
  mounted(){

  },
  methods: {
    init(){
      console.info("id:" + this.id)
      this.myChart = this.id;
      console.info("myChart:" + this.myChart);
      this.$nextTick(() => {
        this.drawLine();
      });

    },
    drawLine(){
      // 基于准备好的dom，初始化echarts实例
      console.info("myChart:" + this.myChart)
      var documentDiv = document.getElementById(this.myChart);
      let myChart = this.$echarts.init(documentDiv, "customed");
      var option = {
        title: {
          text: '折线图'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['Email', 'Union Ads', 'Video Ads', 'Direct', 'Search Engine']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: 'Email',
            type: 'line',
            stack: 'Total',
            data: [120, 132, 101, 134, 90, 230, 210]
          },
          {
            name: 'Union Ads',
            type: 'line',
            stack: 'Total',
            data: [220, 182, 191, 234, 290, 330, 310]
          },
          {
            name: 'Video Ads',
            type: 'line',
            stack: 'Total',
            data: [150, 232, 201, 154, 190, 330, 410]
          },
          {
            name: 'Direct',
            type: 'line',
            stack: 'Total',
            data: [320, 332, 301, 334, 390, 330, 320]
          },
          {
            name: 'Search Engine',
            type: 'line',
            stack: 'Total',
            data: [820, 932, 901, 934, 1290, 1330, 1320]
          }
        ]
      };
      // 绘制图表
      myChart.setOption(option);
    }
  }
}
</script>
