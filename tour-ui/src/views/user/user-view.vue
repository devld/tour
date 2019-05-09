<template>
  <div class="user-view">
    <img class="avatar" @click="gotoUserProfile" v-img="user.avatar">
    <div class="user-info">
      <span class="nickname">{{ user.nickname }}</span>
      <div class="extra" v-if="$slots.default">
        <slot/>
      </div>
    </div>
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
  $size: 42px;

  user-select: none;
  display: inline-flex;
  align-items: center;

  .user-info {
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: $size;

    .nickname {
      font-size: 14px;
    }

    .extra {
      font-size: 12px;
    }

  }

  .avatar {
    width: $size;
    height: $size;
    border-radius: $size;
    margin-right: 10px;
    cursor: pointer;
  }
}
</style>
