<template>
  <div
    class="editor-wrapper"
    :element-loading-text="loadingProgress + '%'"
    v-loading="imageUploading"
  >
    <div ref="toolbar" class="toolbar"></div>
    <div ref="editor" class="editor"></div>
  </div>
</template>
<script>
import WangEditor from 'wangeditor'

import { uploadFile, FileType } from '../../api/file'

const editorMenus = [
  'head',  // 标题
  'bold',  // 粗体
  'list',  // 列表
  'image',  // 插入图片
  'table',  // 表格
  'video',  // 插入视频
  'undo',  // 撤销
  'redo'  // 重复
]

export default {
  name: 'rich-text-editor',
  props: {
    value: String,
    imgWm: Boolean
  },
  watch: {
    value () {
      this.modelValueChanged()
    }
  },
  data () {
    return {
      editor: null,
      lastContent: '',
      imageUploading: false,
      loadingProgress: 0
    }
  },
  mounted () {
    this.editor = new WangEditor(this.$refs.toolbar, this.$refs.editor)
    this.editor.customConfig.zIndex = 0
    this.editor.customConfig.onchange = this.contentChanged
    this.editor.customConfig.customUploadImg = this.uploadImage
    this.editor.customConfig.uploadImgMaxLength = 1
    this.editor.customConfig.menus = editorMenus

    this.editor.create()

    this.editor.txt.html(this.value)
  },
  methods: {
    uploadImage (files, insert) {
      this.imageUploading = true
      const file = files[0]
      const options = {}
      if (this.imgWm) { options.wm = '1' }
      this.loadingProgress = 0
      uploadFile(file, FileType.IMAGE, options, ({ percent }) => {
        this.loadingProgress = Math.round(percent)
      }).then(res => {
        insert(res.url)
      }, e => {
        this.$message.error('上传失败: ' + e.message)
      }).then(() => {
        this.imageUploading = false
      })
    },
    contentChanged (html) {
      this.lastContent = html
      this.$emit('input', html)
    },
    modelValueChanged () {
      if (this.value !== this.lastContent) {
        this.editor.txt.html(this.value)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.editor-wrapper {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  .toolbar {
    border-bottom: solid 1px rgba(0, 0, 0, 0.1);
  }
  .editor {
    overflow: auto;
    flex-grow: 1;
  }
}
</style>
