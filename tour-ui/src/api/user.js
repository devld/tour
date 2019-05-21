import axios from './http/axios'

export function updateProfile ({
  username,
  nickname,
  avatar,
  selfIntro,
  gender,
  birth,
  regionId
}) {
  return axios.post('/user/profile', {
    username,
    nickname,
    avatar,
    selfIntro,
    gender,
    birth,
    regionId
  })
}

export function getUserProfile (username) {
  return axios.get(username ? `/user/${username}` : '/user')
}

export function toggleUserEnabled (userId, enabled) {
  return axios.put(`/user/${userId}`, null, {
    params: {
      enabled
    }
  })
}

export function deleteUserAccount (userId) {
  return axios.delete(`/user/${userId}`)
}
