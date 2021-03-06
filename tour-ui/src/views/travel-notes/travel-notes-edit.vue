<template>
  <div class="travel-notes-edit" v-loading="loading">
    <div class="writing" v-show="step === 0">
      <el-form :model="notes" ref="notesForm" :rules="notesRules">
        <el-form-item prop="title">
          <el-input v-model="notes.title" placeholder="游记标题..."/>
        </el-form-item>
        <div class="spots-wrapper">
          <el-form-item ref="spotsFormItem" prop="spots" label="景点">
            <spot-selector-view size="small" @select="spotSelected" placeholder="景点名称..."/>
            <div class="spots">
              <div
                class="spot"
                v-for="(s, i) in notes.spots"
                :key="s.id"
                :style="{ 'background-image': `url(${s.coverUrl})` }"
              >
                <span>{{ s.name }}</span>
                <i class="spot-delete el-icon-delete" title="删除" @click="notes.spots.splice(i, 1)"/>
              </div>
            </div>
          </el-form-item>
        </div>
        <div class="meta">
          <div class="start-time">
            <span class="label">开始时间</span>
            <el-form-item prop="startTime">
              <el-date-picker type="datetime" v-model="notes.startTime" placeholder="开始时间"/>
            </el-form-item>
          </div>
          <div class="days-usage">
            <span class="label">用时(天)</span>
            <el-form-item prop="daysUsed">
              <el-input-number v-model="notes.daysUsed" :precision="1" :min="0.1"/>
            </el-form-item>
          </div>
          <div class="cost-average">
            <span class="label">人均花费(元)</span>
            <el-form-item prop="costAverage">
              <el-input-number v-model="notes.costAverage" :min="1"/>
            </el-form-item>
          </div>
        </div>
        <el-form-item prop="content">
          <rich-text-editor
            ref="contentEditor"
            class="content"
            :img-wm="true"
            v-model="notes.content"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="goMarkPhoto">下一步</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 标记照片 -->
    <div class="marking-photo" v-show="step === 1">
      <div>
        <div v-if="photos.length" class="photos">
          <div class="photo" v-for="(p, i) in photos" :key="p.id">
            <img :src="p.url">
            <div class="photo-meta">
              <el-input v-model="p.desc" size="mini" placeholder="照片描述..."/>
              <div class="photo-spot">
                <i class="el-icon-location-outline"/>
                <spot-selector-view
                  v-if="p.spotSelecting"
                  placeholder="选择景点"
                  size="mini"
                  v-model="p.spot"
                  :spots-list="notes.spots"
                  @select="p.spotSelecting = false"
                />
                <span v-else @click="p.spotSelecting = true">{{ p.spot ? p.spot.name : '点击选择景点' }}</span>
              </div>
            </div>
            <i class="photo-delete el-icon-delete" title="删除" @click.stop="photos.splice(i, 1)"/>
          </div>
        </div>
      </div>
      <div>
        <el-button @click="step = 0">返回上一步</el-button>
        <el-button type="primary" @click="publishNotes">{{ notesId ? '保存' : '发表' }}</el-button>
      </div>
    </div>
    <!-- 标记照片 -->
  </div>
</template>
<script>
import RichTextEditor from '../part/rich-text-editor'
import SpotSelectorView from './spot-selector'

import { createTravelNotes, getNotesDetails, editTravelNotes } from '../../api/travel-notes'

let id = 1

export default {
  name: 'TravelNotesEditView',
  components: { RichTextEditor, SpotSelectorView },
  props: {
    notesId: Number
  },
  watch: {
    notesId: {
      immediate: true,
      handler () {
        this.loadNodes()
      }
    }
  },
  data () {
    return {
      step: 0,
      notes: {
        title: '',
        content: '',
        startTime: null,
        daysUsed: 1,
        costAverage: 1,
        spots: [],
        photos: []
      },
      notesRules: {
        title: [{ required: true, message: '请填写标题哦' }],
        content: [{ required: true, message: '请填写内容' }],
        startTime: [{ required: true, message: '请选择开始时间' }]
      },

      photos: [],

      loading: false
    }
  },
  methods: {
    publishNotes () {
      this.$refs.contentEditor.processImages((imgs) => {
        for (const img of imgs) {
          const photo = this.photos.find(e => e.id === +img.dataset.eid)
          if (photo) {
            img.alt = photo.desc
          }
        }
      })
      const notes = {
        title: this.notes.title,
        content: this.notes.content,
        startTime: this.notes.startTime.getTime(),
        daysUsed: this.notes.daysUsed,
        costAverage: this.notes.costAverage,
        spotIds: this.notes.spots.map(e => e.id),
        photos: this.photos.filter(e => !!e.spot).map(e => {
          return {
            spotId: e.spot.id,
            src: e.url,
            alt: e.desc
          }
        })
      }
      this.loading = true
      const resultPromise = this.notesId ? editTravelNotes(this.notesId, notes) : createTravelNotes(notes)
      resultPromise.then(res => {
        this.$message.success((this.notesId ? '保存' : '发表') + '成功')
        this.step = 0
        if (!this.notesId) {
          this.$refs.notesForm.resetFields()
          this.notes.spots = []
        }
      }, e => {
        this.$message.error(e.message)
      }).then(() => {
        this.loading = false
      })
    },
    loadNodes () {
      if (!this.notesId) {
        return
      }
      this.loading = true
      getNotesDetails(this.notesId).then(notes => {
        this.notes.title = notes.title
        this.notes.content = notes.content
        this.notes.startTime = new Date(notes.startTime)
        this.notes.daysUsed = notes.daysUsed
        this.notes.costAverage = notes.costAverage
        this.notes.spots = notes.spots
      }, e => {
        this.$message.error(e.message)
      }).then(() => {
        this.loading = false
      })
    },
    goMarkPhoto () {
      if (!this.notes.spots.length) {
        return this.$message('请至少选择一个景点')
      }
      this.$refs.notesForm.validate().then(v => {
        this.processContentImages()
        if (this.photos.length === 0) {
          this.$confirm('还没有添加照片哦，是否直接发布？', '发布游记', {
            confirmButtonText: '直接发布',
            cancelButtonText: '再等等'
          }).then(() => {
            this.publishNotes()
          }, () => { })
        } else {
          this.step = 1
        }
      }, () => { })
    },
    processContentImages () {
      const imgs = this.$refs.contentEditor.processImages((imgs) => {
        this.photos.splice(0)
        for (const img of imgs) {
          const url = img.src
          const spot = img.dataset.id ? this.notes.spots.find(e => e.id === +img.dataset.id) : null
          const eid = id++
          img.dataset.eid = eid
          this.photos.push({
            id: eid,
            url,
            spot,
            desc: img.alt || '',
            spotSelecting: false
          })
        }
      })
    },

    spotSelected (spot) {
      if (this.notes.spots.findIndex(e => e.id === spot.id) === -1) {
        this.notes.spots.push(spot)
      }
      this.$refs.spotsFormItem.clearValidate()
    }
  }
}
</script>
<style lang="scss" scoped>
.travel-notes-edit {
  background-color: #fff;
  padding: 10px;

  .label {
    display: block;
    padding: 10px 0;
  }

  .meta {
    @media screen and (min-width: 840px) {
      display: flex;
      .el-form-item {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
      }

      & > div {
        width: 33.33%;
      }
    }
  }

  .content {
    border: solid 1px rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    height: 600px;
  }

  .spots-wrapper {
    .el-form-item {
      margin-bottom: 0;
    }
    .spots {
      display: flex;
      flex-wrap: wrap;
      margin-bottom: 30px;
      padding-bottom: 20px;
      border-bottom: solid 1px rgba(0, 0, 0, 0.1);
      .spot {
        position: relative;
        user-select: none;
        width: 120px;
        height: 90px;
        background-size: 100% 100%;
        font-size: 12px;
        color: #fff;
        text-shadow: 0 0 2px rgba(0, 0, 0, 0.1);
        display: flex;
        justify-content: center;
        align-items: center;
        box-shadow: 0 0 6px rgba(0, 0, 0, 0.1);
        border-radius: 4px;
        margin: 6px;

        .spot-delete {
          cursor: pointer;
          color: red;
          font-size: 16px;
          position: absolute;
          top: 6px;
          right: 6px;
        }
      }
    }
  }

  .photos {
    .photo {
      display: inline-block;
      border: solid 1px rgba(0, 0, 0, 0.1);
      position: relative;
      width: 240px;
      margin: 20px;
      transition: all 0.3s;
      cursor: pointer;
      img {
        width: 100%;
        height: 120px;
        object-fit: cover;
      }
      &:hover {
        border: none;
        transform: scale(1.1);
        box-shadow: 0 0 8px rgba(0, 0, 0, 0.2);
        border-radius: 4px;

        img {
          border-radius: 4px;
        }
      }
      .photo-delete {
        position: absolute;
        font-size: 14px;
        color: red;
        top: 6px;
        right: 6px;
      }
      .photo-meta {
        margin: 6px;
        & > * {
          margin: 5px 0;
        }

        .photo-spot {
          font-size: 14px;
          .spot-selector {
            font-size: unset;
            display: inline-block;
            .el-select {
              width: 100%;
            }
          }
        }
      }
    }
  }
}

.dialog-footer {
  .el-button {
    margin: 10px 0;
  }
}
</style>
