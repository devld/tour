import axios from './http/axios'

export function createTravelNotes (notes) {
  return axios.post('/travel', notes)
}

export function toggleNotesLike (notesId, state) {
  return axios.post(`/travel/${notesId}/like`, null, {
    params: {
      s: state
    }
  })
}

export function toggleNotesCollect (notesId, state) {
  return axios.post(`/travel/${notesId}/collect`, null, {
    params: {
      s: state
    }
  })
}

export function shareNotes (notesId, via) {
  return axios.post(`/travel/${notesId}/share`, null, {
    params: {
      via
    }
  })
}

export function deleteNotes (notesId) {
  return axios.delete(`/travel/${notesId}`)
}
