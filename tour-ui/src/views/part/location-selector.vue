<template>
  <div v-loading="loading" class="location-selector">
    <div class="location-id">
      <district-selector-view v-model="locationId" @input="valueChanged"/>
    </div>
    <div class="location">
      <el-input v-model="location" :maxlength="128" placeholder="地址" @input="valueChanged"/>
    </div>
    <div class="map-wrapper">
      <el-amap-search-box
        class="search-box"
        :search-option="map.searchOptions"
        :on-search-result="onMapSearchResult"
      />
      <el-amap
        class="map"
        :amap-manager="map.manager"
        ref="mapView"
        :plugin="map.plugins"
        :events="map.events"
        :center="map.center"
        :zoom="map.zoom"
      >
        <el-amap-marker
          v-for="(m, i) in map.markers"
          :events="map.markerEvents"
          :key="i"
          :position="m"
        />
        <el-amap-marker
          :z-index="1000"
          v-if="longitude !== undefined && latitude !== undefined"
          :position="[longitude, latitude]"
        >
          <i class="location-icon"/>
        </el-amap-marker>
      </el-amap>
    </div>
  </div>
</template>
<script>
import DistrictSelectorView from './district-selector'

import { debounce, calcLocationCenter } from '../../util'
import { getDistrictByCode } from '../../api/district'

import { AMapManager } from 'vue-amap'
import VueAMap from 'vue-amap'

export default {
  name: 'LocationSelector',
  components: { DistrictSelectorView },
  props: {
    value: {
      type: Object
    }
  },
  watch: {
    value: {
      immediate: true,
      deep: true,
      handler () {
        this.copyValue(this.value, this)
        this.setMapCenter()
      }
    }
  },
  data () {
    return {
      locationId: 0,
      location: '',
      longitude: 0,
      latitude: 0,

      map: {
        manager: new AMapManager(),
        geocoder: null,
        searchOptions: {},
        center: [110.1485, 34.0908],
        zoom: 12,
        markers: [],
        plugins: [{
          pName: 'Geocoder',
          extensions: 'base',
          events: {
            init: (geocoder) => {
              this.map.geocoder = geocoder
            }
          }
        }],
        events: {
          click: this.onMapClicked
        },
        markerEvents: {
          click: this.onMarkerClicked
        }
      },

      loading: false
    }
  },
  methods: {
    onLocationSelected (lng, lat) {
      this.longitude = lng
      this.latitude = lat
      this.loadLocationAddress(lng, lat)
    },
    loadLocationAddress: debounce(function (lng, lat) {
      this.map.geocoder.getAddress([lng, lat], (status, result) => {
        if (status === 'complete' && result.info === 'OK') {
          this.location = result.regeocode.formattedAddress
          const adcode = result.regeocode.addressComponent.adcode
          this.valueChanged()
          getDistrictByCode(adcode).then(district => {
            if (!district) {
              return Promise.reject('result is null')
            }
            this.locationId = district.id
            this.valueChanged()
          }).catch(e => {
            console.error('resolve district code ' + adcode + 'failed', e)
          })
        }
        console.log(status, result)
      })
    }, 1000),
    onMapClicked (e) {
      const { lng, lat } = e.lnglat
      this.onLocationSelected(lng, lat)
    },
    onMarkerClicked (e) {
      const position = e.target.getPosition()
      this.onLocationSelected(position.lng, position.lat)
    },
    onMapSearchResult (pois) {
      this.map.markers.splice(0)
      if (pois.length > 0) {
        pois.forEach(poi => {
          this.map.markers.push([poi.lng, poi.lat])
        })
        const center = calcLocationCenter(pois)
        this.map.center = [center.lng, center.lat]
      }
    },
    valueChanged () {
      this.$emit('input', this.copyValue(this))
    },
    setMapCenter () {
      if (this.longitude) {
        this.map.center[0] = this.longitude
      }
      if (this.latitude) {
        this.map.center[1] = this.latitude
      }
    },
    copyValue (src, dst) {
      if (!dst) {
        dst = {}
      }
      if (src) {
        dst.locationId = src.locationId
        dst.location = src.location
        dst.longitude = src.longitude
        dst.latitude = src.latitude
      }
      return dst
    }
  }
}
</script>
<style lang="scss" scoped>
@import "./common.scss";

.location-selector {
  .map-wrapper {
    height: 300px;
    position: relative;
    .search-box {
      position: absolute;
      top: 20px;
      left: 20px;
    }
  }

  & > div {
    margin-top: 10px;
    &:first-child {
      margin-top: 0;
    }
  }
}
</style>
