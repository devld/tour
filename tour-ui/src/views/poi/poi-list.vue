<template>
  <div v-loading="loading">
    <div class="load-error" v-if="err">{{err}}</div>
    <div class="data-list" v-else>
      <div
        class="item"
        :class="{ 'active': item.expanded }"
        :title="item.expanded ? '' : '查看详情'"
        v-for="item in dataList"
        :key="item.id"
        @click="itemClicked(item)"
      >
        <div class="gallery" v-if="item.expanded">
          <img :src="url" v-for="url in item.photos" :key="url" @click.stop="imgClicked(url)">
        </div>
        <span class="name">{{ item.name }}</span>
        <span class="location">
          <i class="el-icon-location-outline"/>
          {{ item.location.location }}
        </span>
        <div v-if="item.expanded && item.phone.length" class="tels">
          <span v-for="p in item.phone" :key="p">
            <i class="el-icon-phone"/>
            {{ p }}
          </span>
        </div>
        <div class="biz">
          <div class="distance">{{ item.distance | formatDistance }}</div>
          <el-rate disabled :show-score="item.bizExt.rating > 0" v-model="item.bizExt.rating"/>
          <div>
            <div class="price-wrapper" v-if="item.bizExt.lowestPrice">
              <span class="price">￥{{ item.bizExt.lowestPrice }} 起</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { finaPoiAround } from '../../api/poi'

export default {
  name: 'PoiListView',
  props: {
    lng: {
      type: Number,
      required: true
    },
    lat: {
      type: Number,
      required: true
    },
    radius: {
      type: Number
    },
    type: {
      type: String,
      required: true,
      validator (val) {
        return ['FOOD', 'HOTEL'].indexOf(val) >= 0
      }
    }
  },
  data () {
    return {
      dataList: [],
      page: 1,
      pageSize: 20,
      total: 0,

      err: '',

      loading: false
    }
  },
  mounted () {
    this.loadData()
  },
  methods: {
    itemClicked (item) {
      for (const i of this.dataList) {
        i.expanded = false
      }
      item.expanded = true
      this.$emit('click', item)
    },
    loadData () {
      this.loading = true
      finaPoiAround(this.lng, this.lat, this.radius, this.type, {
        page: this.page,
        pageSize: this.pageSize
      }).then(res => {
        this.page = res.page
        this.total = res.total
        this.dataList = res.data.map(e => {
          e.bizExt.rating = +e.bizExt.rating || 0
          e.expanded = false
          return e
        })
      }, e => {
        this.err = e.message
      }).then(() => {
        this.loading = false
      })
    },
    imgClicked (url) {
      window.open(url)
    }
  }
}
</script>
<style lang="scss" scoped>
.load-error {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.data-list {
  .item {
    background-color: #fff;
    padding: 10px;
    cursor: pointer;

    border-bottom: solid 1px rgba(0, 0, 0, 0.1);
    &:last-child {
      border-bottom: none;
    }

    &.active {
      background-color: rgba(160, 207, 255, 0.2);
      cursor: unset;
      border-bottom: none;
    }

    .gallery {
      white-space: nowrap;
      overflow-x: auto;

      & > img {
        cursor: pointer;
        height: 200px;
        margin-left: 10px;
        &:first-child {
          margin-left: 0;
        }
      }

      &::-webkit-scrollbar {
        background-color: #fff;
        height: 6px;
      }

      &::-webkit-scrollbar-track {
        border-radius: 3px;
      }

      &::-webkit-scrollbar-thumb {
        background-color: rgba(0, 0, 0, 0.3);
        border-radius: 3px;
      }
    }

    & > * {
      margin: 8px 0;
    }

    .name {
      display: block;
    }

    .location {
      display: block;
      font-size: 12px;
    }

    .tels {
      font-size: 14px;
      & > span {
        margin-left: 10px;
        &:first-child {
          margin-left: 0;
        }
      }
    }

    .biz {
      display: flex;
      font-size: 14px;
      user-select: none;

      & > * {
        margin-left: 20px;
        &:first-child {
          margin-left: 0;
        }
      }
    }
  }
}
</style>
