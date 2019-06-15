import axios from './http/axios'

import * as qiniu from 'qiniu-js'

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

const UploadServices = {
  'qiniu': function ({ token, key, baseUrl }, file, onprogress) {
    return new Promise((resolve, reject) => {
      qiniu.upload(file, key, token, {}, {}).subscribe({
        next ({ total }) {
          onprogress && onprogress(total)
        },
        complete ({ key }) {
          resolve({
            url: baseUrl + key
          })
        },
        error (e) {
          console.error('upload file failed', e)
          reject({ code: -1, message: e.message })
        }
      })
    })
  }
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
  return uploadPrepare(file, type, options).then(({ service, opts }) => {
    const uploadService = UploadServices[service]
    if (!uploadService) {
      return Promise.reject({ code: 500, message: 'unknown upload service' })
    }
    return uploadService(opts, file, onprogress)
  })
}
