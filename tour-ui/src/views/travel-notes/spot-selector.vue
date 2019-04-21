<template>
  <div class="spot-selector">
    <el-select
      :size="size"
      :value="value"
      :placeholder="placeholder"
      filterable
      :remote="!spotsList"
      :remote-method="querySpots"
      :loading="searching"
      @change="$emit('select', $event); $emit('input', $event)"
    >
      <el-option v-for="item in searchResult" :key="item.id" :label="item.name" :value="item"/>
    </el-select>
  </div>
</template>
<script>
import { searchSpot } from '../../api/spot'

export default {
  name: 'SpotSelectorView',
  props: {
    value: Object,
    size: {
      type: String
    },
    spotsList: {
      type: Array
    },
    placeholder: String
  },
  watch: {
    spotsList: {
      immediate: true,
      handler () {
        this.searchResult = this.spotsList
      }
    }
  },
  data () {
    return {
      searchResult: [],
      searching: false
    }
  },
  methods: {
    querySpots (s) {
      s = s && s.trim() || ''
      if (!s) {
        this.searchResult = []
        return
      }
      this.searching = true
      searchSpot(s, 1, 50).then(spots => {
        this.searchResult = spots
      }, e => {
        this.$message.error(e.message)
        this.searchResult = []
      }).then(() => {
        this.searching = false
      })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
