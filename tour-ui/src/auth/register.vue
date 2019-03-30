<template>
  <div class="register">
    <el-form
      ref="registerForm"
      :model="registerForm"
      size="medium"
      label-width="100px"
      label-position="left"
      :rules="registerFormRules"
    >
      <el-form-item label="用户名" prop="username">
        <el-input v-model.trim="registerForm.username" maxlength="16"/>
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input
          v-model="registerForm.nickname"
          :placeholder="registerForm.username"
          maxlength="32"
        />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model.trim="registerForm.email" type="email"/>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model.trim="registerForm.password" type="password" maxlength="16"/>
      </el-form-item>
      <el-form-item label="重复密码" prop="passwordRepeat">
        <el-input v-model.trim="registerForm.passwordRepeat" type="password" maxlength="16"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doRegister" :loading="ing">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { isEmail } from '../util'
import { checkRegister, register } from '../api/auth'

export default {
  name: 'RegisterView',
  data () {
    return {
      registerForm: {
        username: '',
        nickname: '',
        email: '',
        password: '',
        passwordRepeat: ''
      },
      registerFormRules: {
        username: [{ required: true, message: '用户名不能为空', trigger: 'blur' },
        { pattern: /^[A-z0-9_\-]{5,16}$/, message: '用户名应为 5 ~ 16 个字母, 数字, _, -' }, {
          trigger: 'blur',
          validator: (rule, value, callback) => {
            if (!this.registerForm.username) {
              return callback()
            }
            checkRegister({ username: this.registerForm.username }).then(res => {
              callback(res.username ? new Error('用户名已被注册') : undefined)
            }, () => { })
          }
        }],
        email: [{ type: 'email', message: '邮箱格式不正确' }, {
          trigger: 'blur',
          validator: (rule, value, callback) => {
            if (!this.registerForm.email) {
              return callback()
            }
            checkRegister({ email: this.registerForm.email }).then(res => {
              callback(res.email ? new Error('邮箱已被使用') : undefined)
            }, () => { })
          }
        }],
        password: [{ required: true, message: '密码不能为空' }, { min: 6, max: 16, message: '密码应为 6 ~ 16 位' }],
        passwordRepeat: [{ required: true, message: '请再输入一次密码' }, {
          trigger: 'change',
          validator: (rule, val, callback) => {
            callback(this.registerForm.password !== this.registerForm.passwordRepeat ? new Error('密码不一致') : undefined)
          }
        }]
      },
      ing: false
    }
  },
  methods: {
    doRegister () {
      this.ing = true
      this.$refs.registerForm.validate().then(v => {
        return register(this.registerForm)
      }).then(res => {
        this.$message.success('注册成功')
        this.$emit('registered', this.registerForm.username)
        this.$refs.registerForm.resetFields()
      }).catch(e => {
        e && e.message && this.$message.error(e.message)
      }).then(() => { this.ing = false })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
