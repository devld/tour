import { COOKIE_KEY, APP_PATH } from '../../config'

export default {

  /**
   * @typedef Token
   * @property {string} token token key
   * @property {number} expireIn 有效期
   */

  /**
   * 设置 token
   * @param {Token} token 
   */
  setToken (token) {
    if (token) {
      document.cookie = `${COOKIE_KEY}=${encodeURIComponent(token.token)}; max-age=${token.expireIn}; path=${APP_PATH}`
    } else {
      // 删除 cookie
      document.cookie = `${COOKIE_KEY}=; expires=Thu, 01 Jan 1970 00:00:01 GMT; path=${APP_PATH}`
    }
  },

  /**
   * @returns {string} token
   */
  getToken () {
    const cookiestring = RegExp('' + COOKIE_KEY + '[^;]+').exec(document.cookie)
    return decodeURIComponent(!!cookiestring ? cookiestring.toString().replace(/^[^=]+./, '') : '')
  }

}
