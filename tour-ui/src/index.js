import Vue from 'vue'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(ElementUI)

import Directives from './directive'
for (const k in Directives) {
  Vue.directive(k, Directives[k])
}

import VueAMap from 'vue-amap'
import { AMAP_API_KEY } from './config'
Vue.use(VueAMap)
VueAMap.initAMapApiLoader({
  key: AMAP_API_KEY,
  plugin: [
    'AMap.Autocomplete', 'AMap.PlaceSearch', 'AMap.Scale',
    'AMap.OverView', 'AMap.ToolBar', 'AMap.MapType',
    'AMap.PolyEditor', 'AMap.CircleEditor'
  ],
  v: '1.4.4'
})

import Auth from './views/auth'

import UpdateProfileView from './views/user/update-profile'
import SpotEditView from './views/spot/spot-edit'
import TravelNotesEditView from './views/travel-notes/travel-notes-edit'

window.$ = $

export default {
  Auth,
  Message: ElementUI.Message,
  Vue,
  Views: {
    UpdateProfileView,
    SpotEditView,
    TravelNotesEditView
  }
}
