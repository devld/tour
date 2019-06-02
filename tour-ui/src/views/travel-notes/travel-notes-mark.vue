<template>
  <div class="spot-mark">
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
    <div class="collect">
      <i
        @click.stop="toggleCollect(false)"
        v-if="thisValue.collect.state"
        class="iconfont yes"
        title="取消收藏"
      >&#xe69e;</i>
      <i @click.stop="toggleCollect(true)" v-else class="iconfont no" title="收藏景点">&#xe600;</i>
      <span class="count">{{ thisValue.collect.count }}</span>
    </div>
    <div class="share">
      <i class="iconfont" title="分享">&#xe60f;</i>
      <span class="count">{{ thisValue.share.count }}</span>
      <share-view @click="shareNotes"/>
    </div>
  </div>
</template>
<script>
import { toggleNotesLike, toggleNotesCollect, shareNotes } from '../../api/travel-notes'

import share from '../../util/share'
import ShareView from '../part/share-view'

export default {
  name: 'TravelNotesMarkView',
  components: { ShareView },
  props: {
    notesId: {
      type: Number,
      required: true
    },
    value: {
      type: Object
    },
    shareMeta: {
      type: Object,
      required: true
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
        },
        share: {
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
    shareNotes (via) {
      share(via, {
        title: this.shareMeta.title,
        desc: this.shareMeta.content,
        url: window.location.href,
        image: this.shareMeta.image
      }).then(() => {
        shareNotes(this.notesId, via)
      }, e => {
        this.$message.error(e)
      })
    },
    copyValue (src, dst) {
      dst = dst || {}
      dst.like = dst.like || {}
      dst.like.state = src.like.state
      dst.like.count = src.like.count
      dst.collect = dst.collect || {}
      dst.collect.state = src.collect.state
      dst.collect.count = src.collect.count
      dst.share = dst.share || {}
      dst.share.count = src.share.count
      return dst
    }
  }
}
</script>

<style lang="scss" scoped>
@keyframes share-anim {
  from {
    opacity: 0;
    transform: translateY(-100%);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.spot-mark {
  .like,
  .collect,
  .share {
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
  .share {
    position: relative;
    .share-view {
      right: 0;
      display: none;
      position: absolute;
      animation: share-anim 0.4s;
    }
    &:hover {
      .share-view {
        display: block;
      }
    }
  }
}
</style>
