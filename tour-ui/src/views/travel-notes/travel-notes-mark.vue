<template>
  <div class="spot-mark">
    <div class="like">
      <i
        @click="toggleLike(false)"
        v-if="thisValue.like.state"
        class="iconfont yes"
        title="取消点赞"
      >&#xe606;</i>
      <i @click="toggleLike(true)" v-else class="iconfont no" title="点赞">&#xe8c4;</i>
      <span class="count">{{ thisValue.like.count }}</span>
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
import { toggleNotesLike, toggleNotesCollect, shareNotes } from '../../api/travel-notes'

export default {
  name: 'TravelNotesMarkView',
  props: {
    notesId: {
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
      collectLoading: false,

      thisValue: {
        like: {
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
    toggleLike (state) {
      if (this.likeLoading) {
        return
      }
      this.likeLoading = true
      toggleNotesLike(this.notesId, state).then(() => {
        this.thisValue.like.state = state
        this.thisValue.like.count += state ? 1 : -1
        this.$emit('input', this.copyValue(this.thisValue))
      }, e => {
        console.error(e.message)
      }).then(() => {
        this.likeLoading = false
      })
    },
    toggleCollect (state) {
      if (this.collectLoading) {
        return
      }
      this.collectLoading = true
      toggleNotesCollect(this.notesId, state).then(() => {
        this.thisValue.collect.state = state
        this.thisValue.collect.count += state ? 1 : -1
        this.$emit('input', this.copyValue(this.thisValue))
      }, e => {
        console.error(e.message)
      }).then(() => {
        this.collectLoading = false
      })
    },
    share () {
      shareNotes(this.notesId)
    },
    copyValue (src, dst) {
      dst = dst || {}
      dst.like = dst.like || {}
      dst.like.state = src.like.state
      dst.like.count = src.like.count
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
  .like,
  .collect {
    display: inline-block;
    & > i {
      font-size: 26px;
      cursor: pointer;
    }
  }
  .like {
    .yes {
      color: rgb(255, 64, 0);
    }
  }
  .collect {
    .yes {
      color: gold;
    }
  }
}
</style>
