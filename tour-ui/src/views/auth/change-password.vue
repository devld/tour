<template>
  <div v-loading="ing">
    <el-form
      ref="passwordForm"
      :model="passwordForm"
      label-width="100px"
      label-position="left"
      size="meidum"
      :rules="passwordFormRules"
    >
      <el-form-item label="旧密码" prop="old">
        <el-input v-model.trim="passwordForm.old" type="password"/>
      </el-form-item>
      <el-form-item label="新密码" prop="password">
        <el-input v-model.trim="passwordForm.password" type="password"/>
      </el-form-item>
      <el-form-item label="重复密码" prop="passwordRepeat">
        <el-input v-model.trim="passwordForm.passwordRepeat" type="password"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="changePassword">修改密码</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { changePassword } from '../../api/auth'

export default {
  name: 'ChangePasswordView',
  data () {
    return {
      passwordForm: {
        old: '',
        password: '',
        passwordRepeat: ''
      },
      passwordFormRules: {
        old: [{ required: true, message: '请输入旧密码' }],
        password: [{ required: true, message: '请输入新密码' }, { min: 6, max: 16, message: '密码应为 6 ~ 16 位' }],
        passwordRepeat: [{ required: true, message: '请再输入一次新密码' }, {
          trigger: 'change',
          validator: (rule, val, callback) => {
            callback(this.passwordForm.password !== this.passwordForm.passwordRepeat ? new Error('密码不一致') : undefined)
          }
        }]
      },
      ing: false
    }
  },
  methods: {
    changePassword () {
      this.ing = true
      this.$refs.passwordForm.validate().then(() => {
        return changePassword(this.passwordForm.old, this.passwordForm.password)
      }).then(res => {
        this.$message.success('修改密码成功')
        this.$emit('change-password')
        this.$refs.passwordForm.resetFields()
      }).catch(e => {
        e && e.message && this.$message.error(e.message)
      }).then(() => {
        this.ing = false
      })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
