<template>
  <div class="spot-mark">
    <div class="went">
      <i
        @click="toggleWent(false)"
        v-if="thisValue.went.state"
        class="iconfont yes"
        title="取消标记"
      >&#xe61a;</i>
      <i @click="toggleWent(true)" v-else class="iconfont no" title="标记为去过">&#xe637;</i>
      <span class="count">{{ thisValue.went.count }}</span>
    </div>
    <div class="collect">
      <i
        @click="toggleCollect(false)"
        v-if="thisValue.collect.state"
        class="iconfont yes"
        title="取消收藏"
      >&#xe69e;</i>
      <i @click="toggleCollect(true)" v-else class="iconfont no" title="收藏景点">&#xe600;</i>
      <span class="count">{{ thisValue.collect.count }}</span>
    </div>
  </div>
</template>
<script>
import { toggleSpotWent, toggleSpotCollect } from '../../api/spot'

export default {
  name: 'SpotMarkView',
  props: {
    spotId: {
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
      wentLoading: false,
      collectLoading: false,

      thisValue: {
        went: {
          state: false,
          count: 0
        },
        collect: {
          state: false,
          count: 0
        }
      }
    }
  },
  methods: {
    toggleWent (state) {
      if (this.wentLoading) {
        return
      }
      this.wentLoading = true
      toggleSpotWent(this.spotId, state).then(() => {
        this.thisValue.went.state = state
        this.thisValue.went.count += state ? 1 : -1
        this.$emit('input', this.copyValue(this.thisValue))
      }, e => {
        console.error(e.message)
      }).then(() => {
        this.wentLoading = false
      })
    },
    toggleCollect (state) {
      if (this.collectLoading) {
        return
      }
      this.collectLoading = true
      toggleSpotCollect(this.spotId, state).then(() => {
        this.thisValue.collect.state = state
        this.thisValue.collect.count += state ? 1 : -1
        this.$emit('input', this.copyValue(this.thisValue))
      }, e => {
        console.error(e.message)
      }).then(() => {
        this.collectLoading = false
      })
    },
    copyValue (src, dst) {
      dst = dst || {}
      dst.went = dst.went || {}
      dst.went.state = src.went.state
      dst.went.count = src.went.count
      dst.collect = dst.collect || {}
      dst.collect.state = src.collect.state
      dst.collect.count = src.collect.count
      return dst
    }
  }
}
</script>

<style lang="scss" scoped>
.spot-mark {
  .went,
  .collect {
    display: inline-block;
    & > i {
      font-size: 26px;
      cursor: pointer;
    }
  }
  .went {
    .yes {
      color: skyblue;
    }
  }
  .collect {
    .yes {
      color: gold;
    }
  }
}
</style>
