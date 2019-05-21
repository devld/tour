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
          if (loginCallback) {
            loginCallback(user)
            Tour.Auth.hideAuthDialog()
          } else {
            Tour.Auth.hideAuthDialog()
            setTimeout(() => {
              window.location.reload()
            }, 1000)
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
    $(document.body).css('overflow', 'hidden')
    $('.dialog-back').addClass('auth-background-blur')
    $(maskView).show().css('opacity', 0).animate({
      opacity: 1
    }, () => {
      $(maskView).css('opacity', '')
    })
  },

  hideAuthDialog () {
    loginCallback = null
    $(document.body).css('overflow', '')
    $('.dialog-back').removeClass('auth-background-blur')
    $(maskView).animate({
      opacity: 0
    }, () => {
      $(maskView).hide()
    })
  },

  logout (cb) {
    logout().then(() => {
      if (cb) {
        cb()
      } else {
        window.location.reload()
      }
    })
  }
}
