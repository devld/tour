<template>
  <div v-loading="loading" class="district-selector">
    <div class="district-item" v-for="(item, index) in root" :key="index">
      <el-select v-model="item.value" @change="selectChanged($event, index)">
        <el-option
          v-for="d in item.children"
          :key="d.id"
          :label="d.name + d.extra + d.suffix"
          :value="d.id"
        />
      </el-select>
    </div>
  </div>
</template>
<script>
import { getDistricts, resolveDistrict } from '../../api/district'

export default {
  name: 'DistrictSelectorView',
  props: {
    value: {
      type: Number
    }
  },
  data () {
    return {
      loading: false,
      root: [{
        value: null,
        children: []
      }],
      oldValue: null
    }
  },
  watch: {
    value: {
      immediate: true,
      handler () {
        if (this.value > 0) {
          this.setDistrictValue(this.value)
        }
      }
    }
  },
  mounted () {
    this.getDistrictChildren().then(res => {
      this.root[0].children = res
    })
  },
  methods: {
    selectChanged (val, index) {
      this.oldValue = val
      this.$emit('input', val)
      this.getDistrictChildren(val).then(res => {
        this.root.splice(index + 1)
        if (res.length) {
          this.root.push({
            value: null,
            children: res
          })
        }
      })
    },
    setDistrictValue (val) {
      if (this.oldValue === val) {
        return;
      }
      this.loading = true
      resolveDistrict(val).then(res => {
        if (!res || !res.length) {
          return Promise.reject()
        }
        const r = [res]
        for (let i = 1; i < res.length; i++) {
          r.push(getDistricts(res[i - 1].id))
        }
        return Promise.all(r)
      }).then(full => {
        const res = full[0]
        this.root[0].value = res[0].id
        for (let i = 1; i < full.length; i++) {
          this.$set(this.root, i, {
            value: res[i].id,
            children: full[i]
          })
        }
        this.oldValue = val
      }).catch(e => {
        console.error(e)
      }).then(() => {
        this.loading = false
      })
    },
    getDistrictChildren (id) {
      this.loading = true
      return getDistricts(id).then(res => {
        return res
      }, (e => {
        console.error('load districts failed', e)
        return []
      })).then((r) => {
        this.loading = false
        return r
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.district-selector {
  .district-item {
    display: inline-block;
    width: 120px;
    margin-right: 6px;

    &:last-child {
      margin-right: 0;
    }
  }
}
</style>
