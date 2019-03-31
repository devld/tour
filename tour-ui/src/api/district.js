import axios from './http/axios'

export function resolveDistrict (id) {
  return axios.get(`/district/${id}`)
}

export function getDistricts (id) {
  return axios.get(`/district/${id || 0}/children`)
}
