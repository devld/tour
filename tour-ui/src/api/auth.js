import axios from './http/axios'
import TokenStore from './http/token'

export function login (user, password) {
  return axios.post('/auth/token', {
    user, password
  }).then(resp => {
    TokenStore.setToken(resp)
    return resp.user
  })
}

export function logout () {
  return new Promise((resolve, reject) => {
    TokenStore.setToken(null)
    resolve()
  })
}

export function checkRegister ({ username, email }) {
  return axios.post('/auth/check_register', null, {
    params: {
      username, email
    }
  })
}

export function register ({
  username,
  nickname,
  email,
  password
}) {
  return axios.post('/auth/register', {
    username, nickname: nickname || undefined, email: email || undefined, password
  })
}
