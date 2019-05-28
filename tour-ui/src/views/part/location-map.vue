<template>
  <div class="location-map-view">
    <el-amap
      class="map"
      :amap-manager="map.manager"
      ref="mapView"
      :plugin="map.plugins"
      :center="map.center"
      :zoom="zoom"
    >
      <el-amap-marker
        vid="___main___"
        v-if="lng !== undefined && lat !== undefined"
        :position="[lng, lat]"
        :events="map.markerEvents"
      >
        <el-tooltip :content="tip" v-if="tip">
          <i class="location-icon"/>
        </el-tooltip>
        <i v-else class="location-icon"/>
      </el-amap-marker>
      <el-amap-marker
        :title="m.tip"
        :vid="m.id"
        :events="map.markerEvents"
        v-for="(m, i) in markers"
        :key="i"
        :position="[m.lng, m.lat]"
      ></el-amap-marker>
    </el-amap>
  </div>
</template>
<script>
import { AMapManager } from 'vue-amap'
import { calcLocationCenter } from '../../util'

export default {
  name: 'LocationMapView',
  props: {
    lng: {
      type: Number
    },
    lat: {
      type: Number
    },
    tip: {
      type: String
    },
    zoom: {
      type: Number,
      default: 12
    },
    markers: {
      type: Array,
      default () {
        return []
      }
    }
  },
  watch: {
    markers: {
      immediate: true,
      handler () {
        if (this.markers.length) {
          const center = calcLocationCenter(this.markers)
          this.map.center = [center.lng, center.lat]
        } else {
          this.map.center = [this.lng, this.lat]
        }
      }
    }
  },
  data () {
    return {
      map: {
        center: [0, 0],
        manager: new AMapManager(),
        plugins: ['ToolBar', {
          pName: 'MapType',
          defaultType: 0
        }],
        markerEvents: {
          click: this.markerClick
        }
      }
    }
  },
  methods: {
    markerClick (e) {
      const vid = e.target.get('vid')
      if (vid === '___main___') {
        this.$emit('click', { lng: e.lnglat.lng, lat: e.lnglat.lat })
      } else {
        const item = this.markers.find(m => m.id === vid)
        this.$emit('m-click', item)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import "./common.scss";
.location-map-view {
  width: 100%;
  height: 100%;
}
</style>
