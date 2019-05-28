import Vue from 'vue'

import * as ElementUI from './views/part/element-ui'
Vue.use(ElementUI)

import Directives from './util/directive'
for (const k in Directives) {
  Vue.directive(k, Directives[k])
}

import * as Filters from './util/filters'
for (const k in Filters) {
  Vue.filter(k, Filters[k])
}

// 高德地图初始化
import VueAMap from 'vue-amap'
import { AMAP_API_KEY } from './config'
Vue.use(VueAMap)
VueAMap.initAMapApiLoader({
  key: AMAP_API_KEY,
  plugin: [
    'Autocomplete', 'PlaceSearch', 'Scale',
    'OverView', 'ToolBar', 'MapType',
    'PolyEditor', 'CircleEditor',
    'Geocoder'
  ],
  v: '1.4.4'
})
// 高德地图初始化

import Auth from './views/auth'

import Views from './views'

// exported http api
import api from './api'

window.$ = $

export default {
  msg: ElementUI.msg,
  confirm: ElementUI.confirm,
  Auth,
  Vue,
  Views,
  api
}
