import Vue from 'vue'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(ElementUI)

import Directives from './directive'
for (const k in Directives) {
  Vue.directive(k, Directives[k])
}

import Auth from './auth'

import UpdateProfileView from './user/update-profile'

window.$ = $

export default {
  Auth,
  Message: ElementUI.Message,
  Vue,
  Views: {
    UpdateProfileView
  }
}
