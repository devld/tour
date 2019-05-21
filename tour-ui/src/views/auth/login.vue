<template>
  <div class="login">
    <el-form
      ref="loginForm"
      :model="loginForm"
      size="medium"
      label-width="100px"
      label-position="left"
      :rules="loginFormRules"
    >
      <el-form-item label="用户名/邮箱" prop="user">
        <el-input v-model="loginForm.user" ref="user" @keyup.enter.native="doLogin"/>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          maxlength="16"
          ref="password"
          @keyup.enter.native="doLogin"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doLogin" :loading="ing">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { login } from '../../api/auth'

export default {
  name: 'LoginView',
  data () {
    return {
      loginForm: {
        user: '',
        password: ''
      },
      loginFormRules: {
        user: [{ required: true, message: '用户名/邮箱不能为空' }],
        password: [{ required: true, message: '密码为 6 ~ 16 位' }]
      },
      ing: false
    }
  },
  mounted () {
    this.$refs.user.focus()
  },
  methods: {
    doLogin () {
      this.$refs.loginForm.validate().then(v => {
        this.ing = true
        return login(this.loginForm.user, this.loginForm.password)
      }).then(res => {
        this.$refs.loginForm.resetFields()
        this.$emit('login', res.user)
      }).catch(e => {
        e && e.message && this.$message.error(e.message)
      }).then(() => { this.ing = false })
    },
    setUsername (username) {
      this.loginForm.user = username
    },
    focusePassword () {
      this.$refs.password.focus()
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
