import axios from './http/axios'

export function uploadImage (type, file) {
  const data = new FormData()
  data.append('file', file)
  return axios.post('/image/upload', data, {
    params: {
      type
    }
  })
}
