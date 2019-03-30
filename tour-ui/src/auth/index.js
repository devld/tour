import Vue from 'vue'
import AuthPanel from './auth'

import { logout } from '../api/auth'

let maskView
let rootView

import './auth.scss'

function initView () {
  maskView = document.createElement('div')
  maskView.classList.add('auth-panel-mask')

  document.body.appendChild(maskView)

  const auth = new Vue({
    render: h => h(AuthPanel)
  })

  rootView = document.createElement('div')
  maskView.appendChild(rootView)

  auth.$mount(rootView)
}

export default {

  showAuthDialog () {
    if (!maskView) {
      initView()
    }
    $(maskView).show().css('opacity', 0).animate({
      opacity: 1
    }, () => {
      $(maskView).css('opacity', '')
    })
  },

  hideAuthDialog () {
    $(maskView).animate({
      opacity: 0
    }, () => {
      $(maskView).hide()
    })
  },

  logout () {
    logout().then(() => {
      window.location.reload()
    })
  }
}
