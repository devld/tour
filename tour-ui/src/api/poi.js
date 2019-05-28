import axios from './http/axios'

export function finaPoiAround (lng, lat, radius, type, { page, pageSize }) {
  return axios.get('/poi/around', {
    params: {
      lng,
      lat,
      radius,
      type,
      page,
      pageSize
    }
  })
}

export function queryWeatherForecast (locationId) {
  return axios.get('/poi/weather/forecast', {
    params: {
      lid: locationId
    }
  })
}

export function queryWeatherLive (locationId) {
  return axios.get('/poi/weather/live', {
    params: {
      lid: locationId
    }
  })
}
