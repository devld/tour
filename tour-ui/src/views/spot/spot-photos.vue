<template>
  <div>
    <div ref="list" v-waterfall class="photos-waterfall">
      <div @click="$emit('click', item)" class="item" v-for="item in photos" :key="item.id">
        <img :src="item.imgUrl" :alt="item.description" :title="item.description">
        <spot-photo-mark-view :photo-id="item.id" v-model="item.markModel"/>
      </div>
    </div>
    <div class="msg loading-msg" v-if="loading">加载中...</div>
    <div class="msg no-more" v-if="!hasMore">没有更多了</div>
    <div class="msg error" v-if="err">{{ err }}</div>
  </div>
</template>
<script>
import { getSpotPhotos } from '../../api/spot'
import SpotPhotoMarkView from './spot-photo-mark'

import { debounce } from '../../util'

let lastScrollY

export default {
  name: 'SpotPhotosView',
  components: { SpotPhotoMarkView },
  props: {
    spotId: {
      type: Number,
      required: true
    }
  },
  data () {
    return {
      page: 1,
      pageSize: 20,

      photos: [],

      hasMore: true,
      err: '',
      loading: false
    }
  },
  mounted () {
    this.loadPhotos()
    window.addEventListener('scroll', this.windowScroll)
  },
  beforeDestroy () {
    window.removeEventListener('scroll', this.windowScroll)
  },
  methods: {
    windowScroll () {
      if (lastScrollY < window.scrollY) {
        // 向下滚动
        const el = this.$refs.list
        const elBottom = el.offsetTop + el.offsetHeight
        const windowBottom = window.scrollY + window.innerHeight
        if (Math.abs(elBottom - windowBottom) < 50) {
          this.scrollBottom()
        }
      }
      lastScrollY = window.scrollY
    },
    scrollBottom: debounce(function () {
      if (this.hasMore) {
        this.loadPhotos(this.page + 1)
      }
    }, 300),
    loadPhotos (page) {
      if (this.loading) {
        return
      }
      page = page || 1
      this.loading = true
      getSpotPhotos(this.spotId, {
        page,
        pageSize: this.pageSize
      }).then(res => {
        this.page = res.page
        this.hasMore = res.page < Math.ceil(res.total / res.pageSize)
        this.photos.push(...res.data.map(e => {
          e.markModel = {
            like: {
              state: !!e.liked,
              count: e.likeCount
            }
          }
          return e
        }))
      }, e => {
        this.err = e.message
      }).then(() => {
        this.loading = false
      })
    },

  }
}
</script>
<style lang="scss" scoped>
.photos-waterfall {
  margin: 0 auto;
  .item {
    display: inline-block;
    position: relative;

    padding: 8px;

    img {
      max-width: 300px;
      min-width: 100px;
      min-height: 60px;
    }
    .spot-photo-mark {
      position: absolute;
      top: 10px;
      right: 10px;
    }
  }
}
.msg {
  width: 100%;
  text-align: center;
  padding: 20px 0;
}
</style>
