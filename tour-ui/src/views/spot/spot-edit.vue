<template>
  <div class="spot-edit" v-loading="spotLoading">
    <el-form
      ref="spotForm"
      :model="spot"
      label-position="left"
      label-width="100px"
      :rules="spotRules"
    >
      <el-form-item label="景点名称" prop="name">
        <el-input v-model="spot.name"/>
      </el-form-item>
      <el-form-item label="景点介绍" prop="intro" ref="spotIntroFormItem">
        <div class="intro-wrapper">
          <rich-text-editor v-model="spot.intro" @input="$refs.spotIntroFormItem.clearValidate()"/>
        </div>
      </el-form-item>
      <el-form-item label="景点图片" prop="coverUrl" ref="spotCoverUrlFormItem">
        <div class="spot-photo" v-loading="photoUploading">
          <img v-if="spot.coverUrl" :src="spot.coverUrl">
          <el-upload
            action="fake"
            :http-request="uploadPhoto"
            :show-file-list="false"
            accept="image/png, image/jpeg, image/gif"
          >
            <el-button simple size="small">选择图片</el-button>
          </el-upload>
        </div>
      </el-form-item>
      <el-form-item label="景点位置" prop="location">
        <location-selector-view v-model="spot.location"/>
      </el-form-item>
      <el-form-item label="景点电话" prop="phone">
        <el-input v-model="spot.phone"/>
      </el-form-item>
      <el-form-item label="景点网站" prop="website">
        <el-input v-model="spot.website"/>
      </el-form-item>
      <el-form-item label="游览用时参考" prop="timeUsage">
        <el-input v-model="spot.timeUsage"/>
      </el-form-item>
      <el-form-item label="景点交通" prop="transport">
        <el-input v-model="spot.transport" type="textarea"/>
      </el-form-item>
      <el-form-item label="开放时间" prop="openTime">
        <el-input v-model="spot.openTime" type="textarea"/>
      </el-form-item>
      <el-form-item label="门票" prop="tickets">
        <div class="ticket" v-for="(t, i) in spot.tickets" :key="t.id">
          <el-input class="name" v-model="t.name" placeholder="门票描述" :maxlength="128"/>
          <el-input-number class="price" v-model="t.price" :min="0"/>
          <span>元</span>
          <i
            class="ticket-delete el-icon-delete"
            title="删除"
            @click.stop="spot.tickets.splice(i, 1)"
          />
        </div>
        <el-button icon="el-icon-plus" @click="addTicket"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveSpot">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import LocationSelectorView from '../part/location-selector'
import RichTextEditor from '../part/rich-text-editor'
import { uploadFile, FileType } from '../../api/file'

import { createSpot, getSpot, updateSpot } from '../../api/spot'

let id = 1

export default {
  name: 'SpotEditView',
  components: { RichTextEditor, LocationSelectorView },
  props: {
    spotId: Number
  },
  watch: {
    spotId: {
      immediate: true,
      handler () {
        this.loadSpot()
      }
    }
  },
  data () {
    return {
      spot: {
        name: '',
        intro: '',
        coverUrl: '',
        location: {
          location: '',
          locationId: 0,
          longitude: 0,
          latitude: 0
        },
        phone: '',
        website: '',
        timeUsage: '',
        transport: '',
        openTime: '',
        tickets: []
      },

      spotRules: {
        name: [{ required: true, message: '景点名称不能为空' }],
        intro: [{ required: true, message: '景点介绍不能为空' }],
        coverUrl: [{ required: true, message: '请上传景点图片' }]
      },

      photoUploading: false,
      spotLoading: false
    }
  },
  methods: {
    saveSpot () {
      this.$refs.spotForm.validate().then(v => {
        this.spotLoading = true
        if (this.spotId) {
          return updateSpot(this.spotId, this.spot)
        } else {
          return createSpot(this.spot)
        }
      }).then(res => {
        this.$message.success(`${this.spotId ? '编辑' : '创建'}成功`)
        if (!this.spotId) {
          this.$refs.spotForm.resetFields()
        }
      }).catch(e => {
        e && e.message && this.$message.error(e.message)
      }).then(() => {
        this.spotLoading = false
      })
    },
    loadSpot () {
      if (!this.spotId) {
        return
      }
      this.spotLoading = true
      getSpot(this.spotId).then(spot => {
        this.spot = spot
      }, e => {
        this.$message.error(e.message)
      }).then(() => {
        this.spotLoading = false
      })
    },
    uploadPhoto (upload) {
      this.photoUploading = true
      uploadFile(upload.file, FileType.IMAGE).then(res => {
        this.spot.coverUrl = res.url
        this.$refs.spotCoverUrlFormItem.clearValidate()
      }, e => {
        this.$message.error('上传失败: ' + e.message)
      }).then(() => {
        this.photoUploading = false
      })
    },
    addTicket () {
      this.spot.tickets.push({
        id: id++,
        name: '',
        price: 0
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.spot-edit {
  background-color: #fff;
  padding: 10px;

  .intro-wrapper {
    height: 480px;
    border: solid 1px rgba(0, 0, 0, 0.1);
    border-radius: 4px;
  }

  .spot-photo {
    display: inline-flex;
    align-items: center;
    img {
      margin-right: 10px;
      max-height: 200px;
    }
  }

  .ticket {
    display: flex;
    position: relative;
    padding: 10px 0;
    align-items: center;

    .ticket-delete {
      position: absolute;
      cursor: pointer;
      top: 6px;
      right: 6px;
      font-size: 14px;
    }

    & > * {
      margin-right: 10px;
    }

    .name {
      width: 68%;
    }
    .price {
      width: 140px;
    }
  }
}
</style>
