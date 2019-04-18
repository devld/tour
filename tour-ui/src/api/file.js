import axios from './http/axios'

import * as qiniu from 'qiniu-js'

import { FILE_PATH_PREFIX } from '../config'

function uploadPrepare (file, type) {
  return axios.post('/file/upload/prepare', null, {
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

export function uploadFile (file, type, onprogress) {
  return uploadPrepare(file, type).then(({ token, key }) => {
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
