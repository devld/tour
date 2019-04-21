<template>
  <div>
    <district-selector-view v-model="locationId"/>
  </div>
</template>
<script>
import DistrictSelectorView from './district-selector'

export default {
  name: 'LocationSelector',
  components: { DistrictSelectorView },
  props: {
    value: {
      type: Object
    }
  },
  watch: {
    value: {
      immediate: true,
      deep: true,
      handler () {
        this.copyValue(this.value, this)
      }
    }
  },
  data () {
    return {
      locationId: 0,
      location: '',
      longitude: 0,
      latitude: 0
    }
  },
  methods: {
    valueChanged () {
      this.$emit('input', this.copyValue(this))
    },
    copyValue (src, dst) {
      if (!dst) {
        dst = {}
      }
      if (src) {
        dst.locationId = src.locationId
        dst.location = src.location
        dst.longitude = src.longitude
        dst.latitude = src.latitude
      }
      return dst
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
