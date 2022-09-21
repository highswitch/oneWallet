<template>
  <div>
    <el-dialog v-bind="$attrs" v-on="$listeners" :visible.sync="dialogVisible" @open="onOpen" @close="onClose" title="Dialog Title">
      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
        <el-form-item label="选择币种" prop="currency">
          <el-input v-model="formData.currency" placeholder="请输入选择币种" clearable :style="{width: '100%'}">
          </el-input>
        </el-form-item>
        <el-form-item label="选择金额" prop="amount">
          <el-input v-model="formData.amount" placeholder="请输入选择金额" clearable :style="{width: '100%'}">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="close">取消</el-button>
        <el-button type="primary" @click="handleConfirm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    inheritAttrs: false,
    components: {},
    props: [],
    data() {
      return {
        formData: {
          currency: undefined,
          amount: undefined,
        },
        rules: {
          currency: [{
            required: true,
            message: '请输入选择币种',
            trigger: 'blur'
          }],
          amount: [{
            required: true,
            message: '请输入选择金额',
            trigger: 'blur'
          }],
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      onOpen() {},
      onClose() {
        this.$refs['elForm'].resetFields()
      },
      close() {
        this.$emit('update:visible', false)
      },
      handleConfirm() {
        this.$refs['elForm'].validate(valid => {
          if (!valid) return
          this.close()
        })
      },
    }
  }

</script>
<style>
</style>
