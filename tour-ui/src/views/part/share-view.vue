<template>
  <div class="share-view">
    <div class="item" title="分享到 QQ" @click="$emit('click', 'qq')">
      <svg class="iconfont-svg" aria-hidden="true">
        <use xlink:href="#iconQQ"></use>
      </svg>
    </div>
    <div class="item" title="分享到微信" @click="$emit('click', 'weixin')">
      <svg class="iconfont-svg" aria-hidden="true">
        <use xlink:href="#iconwei-xin-dao-hang"></use>
      </svg>
    </div>
    <div class="item" title="分享到微博" @click="$emit('click', 'weibo')">
      <svg class="iconfont-svg" aria-hidden="true">
        <use xlink:href="#iconicons-weibo"></use>
      </svg>
    </div>
  </div>
</template>
<script>
export default {
  name: 'ShareView'
}
</script>
<style lang="scss" scoped>
.share-view {
  white-space: nowrap;
  .item {
    display: inline-block;
    cursor: pointer;
    svg {
      width: 40px;
      height: 40px;
    }
  }
}
</style>
