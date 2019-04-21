import axios from './http/axios'

export function createSpot (spot) {
  return axios.post('/spot', spot)
}

export function getSpot (spotId) {
  return axios.get(`/spot/${spotId}`)
}

export function updateSpot (spotId, spot) {
  return axios.put(`/spot/${spotId}`, spot)
}

export function searchSpot (query, page, pageSize) {
  return axios.get('/spot/search', {
    params: {
      q: query,
      page: page,
      pageSize: pageSize
    }
  })
}
