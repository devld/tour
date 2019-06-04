
export const COOKIE_KEY = 'hello'

const CONTEXT_PATH_COOKIE_KEY = '_tour_path'

const cookiestring = RegExp('' + CONTEXT_PATH_COOKIE_KEY + '[^;]+').exec(document.cookie)
export const APP_PATH = decodeURIComponent(!!cookiestring ? cookiestring.toString().replace(/^[^=]+./, '') : '')
console.log('context path is: ' + APP_PATH)

export const API_PATH_PREFIX = APP_PATH + '/api'

export const AMAP_API_KEY = 'c77d5c59ecc2968ef0974bb8f638c1d6'
