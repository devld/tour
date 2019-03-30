import Axios from 'axios'

import { API_PATH_PREFIX } from '../../config'

import TokenStore from './token'

const axios = Axios.create({
  baseURL: API_PATH_PREFIX
})

axios.interceptors.request.use(config => {
  const token = TokenStore.getToken()
  config.headers['Authorization'] = `token ${token}`
  return config
})

axios.interceptors.response.use(resp => {
  if (resp.status !== 200) {
    return Promise.reject({ code: resp.status, message: resp.statusText, data: resp.data })
  }
  if (resp.data) {
    const data = resp.data
    if (data.code === 0) {
      return data.data
    }
    return Promise.reject(data)
  }
  return Promise.reject({ code: -1, message: 'Unknown Error' })
}, e => {
  console.error('request failed', e)
  return Promise.reject({ code: -1, message: e.message })
})

export default axios
