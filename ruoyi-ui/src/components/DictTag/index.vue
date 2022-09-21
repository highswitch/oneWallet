<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values(item.dictValue)">
        <span
          :class="item.cssClass"
          :key="item.dictValue"
          :index="index"
        >{{ item.dictLabel }}</span>
      </template>
    </template>
  </div>
</template>

<script>
import $ from "jquery"
export default {
  name: "DictTag",
  props: {
    options: {
      type: Array,
      default: null,
    },
    value: [Number, String, Array],
  },
  created() {
    this.init();
  },
  methods: {
    init(){

    },
    values(value1) {
      console.info(value1 + "," + this.value)
      if (value1 == String(this.value)){
        return true;
      } else {
        return false;
      }
    },
  }
};
</script>
<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>
