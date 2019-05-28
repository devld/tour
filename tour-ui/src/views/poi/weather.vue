<template>
  <div v-loading="loading">
    <div class="load-error" v-if="err">{{err}}</div>
    <div class="weather-list" v-else>
      <div class="weather" v-for="(w, i) in weathers" :key="i">
        <div class="date">{{ w.date | weatherDateFormat }}</div>
        <div class="day">
          <div
            class="weather-icon"
            :style="{ 'background-position': iconPosition('day', w.day.weather) }"
          ></div>
          <div class="weather-name">{{ w.day.weather }}</div>
          <div class="weather-info">
            <span class="temp">{{ w.day.temperature }} ℃</span>
            <span class="wind">{{ w.day.wind }}风</span>
            <span class="wind-power">{{ w.day.windPower }}</span>
          </div>
        </div>
        <div class="night">
          <div
            class="weather-icon"
            :style="{ 'background-position': iconPosition('night', w.night.weather) }"
          ></div>
          <div class="weather-name">{{ w.night.weather }}</div>
          <div class="weather-info">
            <span class="temp">{{ w.night.temperature }} ℃</span>
            <span class="wind">{{ w.night.wind }}风</span>
            <span class="wind-power">{{ w.night.windPower }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { queryWeatherForecast } from '../../api/poi'

import WEATHER_LIST from './weather-map'

export default {
  name: 'WeatherView',
  props: {
    locationId: {
      type: Number,
      required: true
    }
  },
  data () {
    return {
      district: null,
      weathers: [],
      err: '',

      loading: false
    }
  },
  mounted () {
    this.loadWeather()
  },
  methods: {
    loadWeather () {
      this.loading = true
      queryWeatherForecast(this.locationId).then(res => {
        this.district = res.district
        this.weathers = res.weathers.slice(0, 4)
      }, e => {
        this.err = e.message
      }).then(() => {
        this.loading = false
      })
    },
    iconPosition (t, weather) {
      const i = this.resolveIconIndex(t, weather)
      return `center -${i * 65}px`
    },
    resolveIconIndex (t, weather) {
      const w = WEATHER_LIST[weather.trim()]
      if (typeof (w) === 'number') {
        return w
      } else if (typeof (w) === 'object') {
        return w[t] !== undefined ? w[t] : -1
      }
      return -1
    }
  },
  filters: {
    weatherDateFormat (t) {
      t += 8 * 3600000
      let now = new Date().getTime()
      const days = parseInt(t / 86400000) - parseInt(now / 86400000)
      return ['今天', '明天', '后天', '大后天'][days]
    }
  }
}
</script>
<style lang="scss" scoped>
.load-error {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.weather-list {
  user-select: none;
  display: flex;
}

.weather {
  margin-right: 30px;
  &:last-child {
    margin-right: 0;
  }
  text-align: center;
  font-size: 14px;
  .day {
    margin-bottom: 10px;
  }
  .weather-icon {
    $size: 65px;
    width: 100%;
    height: $size;
    background-image: url("/static/img/weather.png");
    background-repeat: no-repeat;
  }
}
</style>
