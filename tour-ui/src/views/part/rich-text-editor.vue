<template>
  <div class="editor-wrapper" v-loading="imageUploading">
    <div ref="toolbar" class="toolbar"></div>
    <div ref="editor" class="editor"></div>
  </div>
</template>
<script>
import WangEditor from 'wangeditor'

import { uploadFile, FileType } from '../../api/file'

export default {
  name: 'rich-text-editor',
  props: {
    value: String
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
      imageUploading: false
    }
  },
  mounted () {
    this.editor = new WangEditor(this.$refs.toolbar, this.$refs.editor)
    this.editor.customConfig.zIndex = 0
    this.editor.customConfig.onchange = this.contentChanged
    this.editor.customConfig.customUploadImg = this.uploadImage
    this.editor.customConfig.uploadImgMaxLength = 1

    this.editor.create()

    this.editor.txt.html(this.value)
  },
  methods: {
    uploadImage (files, insert) {
      this.imageUploading = true
      const file = files[0]
      uploadFile(file, FileType.IMAGE).then(res => {
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
    overflow: auto;
    border-bottom: solid 1px rgba(0, 0, 0, 0.1);
  }
  .editor {
    overflow: auto;
    flex-grow: 1;
  }
}
</style>
