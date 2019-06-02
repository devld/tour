<template>
  <div class="spot-photo-mark">
    <div class="like">
      <i
        @click.stop="toggleLike(false)"
        v-if="thisValue.like.state"
        class="iconfont yes"
        title="取消点赞"
      >&#xe606;</i>
      <i @click.stop="toggleLike(true)" v-else class="iconfont no" title="点赞">&#xe8c4;</i>
      <span class="count">{{ thisValue.like.count }}</span>
    </div>
  </div>
</template>
<script>
import { toggleSpotPhotoLike } from '../../api/spot'

export default {
  name: 'SpotPhotoMarkView',
  props: {
    photoId: {
      type: Number,
      required: true
    },
    value: {
      type: Object
    }
  },
  watch: {
    value: {
      immediate: true,
      deep: true,
      handler () {
        if (this.value) {
          this.copyValue(this.value, this.thisValue)
        }
      }
    }
  },
  data () {
    return {
      likeLoading: false,

      thisValue: {
        like: {
          state: false,
          count: 0
        }
      }
    }
  },
  methods: {
    toggleLike (state) {
      if (this.likeLoading) {
        return
      }
      this.likeLoading = true
      toggleSpotPhotoLike(this.photoId, state).then(() => {
        this.thisValue.like.state = state
        this.thisValue.like.count += state ? 1 : -1
        this.$emit('input', this.copyValue(this.thisValue))
      }, e => {
        console.error(e.message)
      }).then(() => {
        this.likeLoading = false
      })
    },
    copyValue (src, dst) {
      dst = dst || {}
      dst.like = dst.like || {}
      dst.like.state = src.like.state
      dst.like.count = src.like.count
      return dst
    }
  }
}
</script>
<style lang="scss" scoped>
.spot-photo-mark {
  .like {
    display: inline-block;
    & > i {
      font-size: 26px;
      cursor: pointer;
    }
    .yes {
      color: rgb(255, 64, 0);
    }
  }
}
</style>
