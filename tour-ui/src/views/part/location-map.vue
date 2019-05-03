<template>
  <div class="location-map-view">
    <el-amap
      class="map"
      :amap-manager="map.manager"
      ref="mapView"
      :plugin="map.plugins"
      :center="[lng, lat]"
      :zoom="zoom"
    >
      <el-amap-marker v-if="lng !== undefined && lat !== undefined" :position="[lng, lat]">
        <el-tooltip :content="tip" v-if="tip">
          <i class="location-icon"/>
        </el-tooltip>
        <i v-else class="location-icon"/>
      </el-amap-marker>
    </el-amap>
  </div>
</template>
<script>
import { AMapManager } from 'vue-amap'

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
    }
  },
  data () {
    return {
      map: {
        manager: new AMapManager(),
        plugins: ['ToolBar', {
          pName: 'MapType',
          defaultType: 0
        }]
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
