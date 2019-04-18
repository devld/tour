import Vue from 'vue'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(ElementUI)

import Directives from './directive'
for (const k in Directives) {
  Vue.directive(k, Directives[k])
}

import Auth from './views/auth'

import UpdateProfileView from './views/user/update-profile'
import SpotEditView from './views/spot/spot-edit'

window.$ = $

export default {
  Auth,
  Message: ElementUI.Message,
  Vue,
  Views: {
    UpdateProfileView,
    SpotEditView
  }
}
