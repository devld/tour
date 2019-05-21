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

export function searchSpot (query, { page, pageSize }) {
  return axios.get('/spot/search', {
    params: {
      q: query,
      page,
      pageSize
    }
  })
}

export function getSpotComments (spotId, { page, pageSize, sort }) {
  return axios.get(`/spot/${spotId}/comment`, {
    params: {
      page,
      pageSize,
      sort
    }
  })
}

export function commentSpot (spotId, content) {
  return axios.post(`/spot/${spotId}/comment`, {
    content
  })
}

export function toggleSpotCommentLike (commentId, like) {
  return axios.post(`/spot/comment/${commentId}/like`, null, {
    params: {
      s: like
    }
  })
}

export function toggleSpotWent (spotId, went) {
  return axios.post(`/spot/${spotId}/went`, null, {
    params: {
      s: went
    }
  })
}

export function toggleSpotCollect (spotId, collect) {
  return axios.post(`/spot/${spotId}/collect`, null, {
    params: {
      s: collect
    }
  })
}

export function deleteSpot (spotId) {
  return axios.delete(`/spot/${spotId}`)
}

export function deleteSpotComment (commentId) {
  return axios.delete(`/spot/comment/${commentId}`)
}
