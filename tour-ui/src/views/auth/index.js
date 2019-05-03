import Vue from 'vue'
import AuthPanel from './auth'

import { logout } from '../../api/auth'

let maskView
let rootView

import './auth.scss'

let loginCallback

function initView () {
  maskView = document.createElement('div')
  maskView.classList.add('auth-panel-mask')

  document.body.appendChild(maskView)

  const auth = new Vue({
    render: h => h(AuthPanel, {
      on: {
        login (user) {
          Tour.Auth.hideAuthDialog()
          if (loginCallback) {
            loginCallback(user)
          } else {
            window.location.reload()
          }
        }
      }
    })
  })

  rootView = document.createElement('div')
  maskView.appendChild(rootView)

  auth.$mount(rootView)
}

export default {

  showAuthDialog (callback, closable = true) {
    loginCallback = callback
    if (!maskView) {
      initView()
    }
    $(maskView).toggleClass('pinned', !closable)
    $(maskView).show().css('opacity', 0).animate({
      opacity: 1
    }, () => {
      $(maskView).css('opacity', '')
    })
  },

  hideAuthDialog () {
    loginCallback = null
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
