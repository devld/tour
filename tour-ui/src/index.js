import Vue from 'vue'

import Auth from './auth'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI)

window.$ = $

export default {
  Auth,
  Message: ElementUI.Message,
  Vue
}
