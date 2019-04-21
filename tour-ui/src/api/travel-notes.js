import axios from './http/axios'

export function createTravelNotes (notes) {
  return axios.post('/travel', notes)
}
