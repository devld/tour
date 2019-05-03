import axios from './http/axios'

export function getMarks (objType, rels, ids) {
  return axios.get('/userMark', {
    params: {
      type: objType,
      rel: (rels || []).join(','),
      ids: (ids || []).join(',')
    }
  })
}

