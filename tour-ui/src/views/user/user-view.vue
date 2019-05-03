<template>
  <div class="user-view">
    <img class="avatar" @click="gotoUserProfile" v-img="user.avatar">
    <span class="nickname">{{ user.nickname }}</span>
  </div>
</template>
<script>
import { APP_PATH } from '../../config'

export default {
  name: 'UserView',
  props: {
    user: {
      type: Object,
      required: true
    }
  },
  methods: {
    gotoUserProfile () {
      window.open(`${APP_PATH}/user/${this.user.username}`)
    }
  }
}
</script>
<style lang="scss" scoped>
.user-view {
  user-select: none;
  .avatar {
    $size: 48px;
    width: $size;
    height: $size;
    border-radius: $size;
    cursor: pointer;
  }
}
</style>
