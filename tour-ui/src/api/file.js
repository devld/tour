import axios from './http/axios'

import * as qiniu from 'qiniu-js'

import { FILE_PATH_PREFIX } from '../config'

function uploadPrepare (file, type, options) {
  return axios.post('/file/upload/prepare', options || {}, {
    params: {
      type,
      name: file.name,
      size: file.size
    }
  })
}

export const FileType = {
  IMAGE: 'IMAGE'
}

/**
 * @typedef UploadResult
 * @property {string} url 图片链接
 */

/**
 * 上传文件
 * @param {File} file 
 * @param {string} type 
 * @param {Function} onprogress 
 * @param {Object} options
 * @returns {Promise<UploadResult>}
 */
export function uploadFile (file, type, options, onprogress) {
  return uploadPrepare(file, type, options).then(({ token, key }) => {
    return new Promise((resolve, reject) => {
      qiniu.upload(file, key, token, {}, {}).subscribe({
        complete: ({ key }) => {
          resolve({
            url: FILE_PATH_PREFIX + key
          })
        },
        error: reject,
        next: onprogress
      })
    })
  })
}
