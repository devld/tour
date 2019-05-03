<template>
  <div class="root" :class="page">
    <div class="heading">
      <span class="login" :class="{ active: page === 'login' }" @click="page = 'login'">登录</span>
      <span>/</span>
      <span class="register" :class="{ active: page === 'register' }" @click="page = 'register'">注册</span>
    </div>
    <div class="auth-panel">
      <keep-alive>
        <component
          :is="page + '-view'"
          ref="view"
          @registered="registered"
          @login="$emit('login', $event)"
        />
      </keep-alive>
    </div>
    <div class="close-button" @click="hideDialog">×</div>
  </div>
</template>
<script>
import LoginView from './login'
import RegisterView from './register'

export default {
  name: 'AuthPanel',
  components: { LoginView, RegisterView },
  data () {
    return {
      page: 'login'
    }
  },
  methods: {
    hideDialog () {
      Tour.Auth.hideAuthDialog()
    },
    registered (username) {
      this.page = 'login'
      this.$nextTick(() => {
        this.$refs.view.setUsername(username)
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.pinned {
  .root {
    .close-button {
      display: none;
    }
  }
}

.root {
  user-select: none;
  width: 430px;
  margin: 0 auto;
  margin-top: 140px;
  padding: 40px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
  position: relative;
  background-color: #fff;
  transition: all 0.2s;
  box-sizing: border-box;
  overflow: hidden;

  &.login {
    height: 300px;
  }
  &.register {
    height: 470px;
  }

  @media screen and (max-width: 640px) {
    margin-top: 100px;
    padding: 20px;
    width: 96%;
  }

  @media screen and (max-height: 640px) {
    margin-top: 10px;
    margin-bottom: 10px;
  }

  .heading {
    font-size: 32px;
    margin-bottom: 20px;
    color: #787878;

    span.login,
    span.register {
      cursor: pointer;
    }

    span.active {
      cursor: auto;
      color: #000;
    }
  }

  .auth-panel {
    width: 100%;
    margin: 0 auto;
  }

  .close-button {
    $size: 40px;
    font-size: 28px;

    position: absolute;
    top: 0;
    right: 0;
    width: $size;
    height: $size;
    line-height: $size;
    text-align: center;
    cursor: pointer;
  }
}
</style>
