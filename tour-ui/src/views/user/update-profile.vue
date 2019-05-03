<template>
  <div class="user-profile-edit" v-loading="profileLoading">
    <el-form
      ref="profileForm"
      :model="profile"
      size="medium"
      label-width="100px"
      label-position="left"
      :rules="profileRules"
    >
      <el-form-item label="头像">
        <div class="avatar" v-loading="avatarUploading">
          <img v-img="profile.avatar">
          <el-upload
            action="fake"
            :http-request="uploadAvatar"
            :show-file-list="false"
            accept="image/png, image/jpeg, image/gif"
          >
            <el-button simple size="small">选择新头像</el-button>
          </el-upload>
        </div>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model.trim="profile.username" disabled maxlength="16"/>
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="profile.nickname" maxlength="32"/>
      </el-form-item>
      <el-form-item label="自我介绍">
        <el-input v-model="profile.selfIntro" type="textarea" maxlength="128"/>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="profile.gender">
          <el-option :value="0" label="不填写"/>
          <el-option :value="1" label="男"/>
          <el-option :value="2" label="女"/>
        </el-select>
      </el-form-item>
      <el-form-item label="出生日期">
        <el-date-picker v-model="profile.birth" type="date"/>
      </el-form-item>
      <el-form-item label="地区">
        <district-selector-view v-model="profile.regionId"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveProfile">保存</el-button>
        <el-button @click="$emit('cancel')">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { getDistricts } from '../../api/district'
import DistrictSelectorView from '../part/district-selector'
import { getUserProfile, updateProfile } from '../../api/user'
import { uploadFile, FileType } from '../../api/file'

import { API_PATH_PREFIX } from '../../config'

export default {
  name: 'UpdateProfileView',
  components: { DistrictSelectorView },
  data () {
    return {
      profile: {
        username: '',
        nickname: '',
        avatar: '',
        selfIntro: '',
        gender: 0,
        birth: null,
        regionId: 0
      },

      profileRules: {
        nickname: [{ required: true, message: '昵称不能为空' }]
      },

      avatarUploading: false,
      profileLoading: false
    }
  },
  mounted () {
    this.profileLoading = true
    getUserProfile().then(res => {
      this.setProfileValues(res)
    }, e => {
      this.$message.error('获取用户资料失败')
    }).then(() => {
      this.profileLoading = false
    })
  },
  methods: {
    saveProfile () {
      this.$refs.profileForm.validate().then(v => {
        this.profileLoading = true
        return updateProfile({
          nickname: this.profile.nickname,
          avatar: this.profile.avatar,
          selfIntro: this.profile.selfIntro,
          gender: this.profile.gender,
          birth: this.profile.birth,
          regionId: this.profile.regionId
        })
      }).then((res) => {
        this.setProfileValues(res)
        this.$message.success('更新成功')
        this.$emit('save', res)
      }).catch(e => {
        e && e.message && this.$message.error(e.message)
      }).then(() => {
        this.profileLoading = false
      })
    },
    uploadAvatar (upload) {
      this.avatarUploading = true
      uploadFile(upload.file, FileType.IMAGE).then(res => {
        this.profile.avatar = res.url
      }, e => {
        this.$message.error('上传失败: ' + e.message)
      }).then(() => {
        this.avatarUploading = false
      })
    },
    setProfileValues (profile) {
      this.profile.username = profile.username
      this.profile.nickname = profile.nickname
      this.profile.avatar = profile.avatar
      this.profile.selfIntro = profile.selfIntro
      this.profile.gender = profile.gender
      this.profile.birth = profile.birth
      this.profile.regionId = profile.regionId
    }
  }
}
</script>
<style lang="scss" scoped>
.user-profile-edit {
  .el-date-editor.el-input {
    width: unset;
  }

  .avatar {
    display: inline-flex;
    align-items: center;
    img {
      margin-right: 10px;
      width: 80px;
      height: 80px;
    }
  }
}
</style>
