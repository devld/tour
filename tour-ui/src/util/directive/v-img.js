import { APP_PATH } from '../../config'

/**
 * 给 img 的 src 添加路径前缀
 */
export default function (el, binding) {
  if (el.tagName === 'IMG') {
    if (binding.oldValue !== binding.value) {
      let src = binding.value || ''
      if (!/^https?:\/\/.*$/.test(src)) {
        src = binding.value ? `${APP_PATH}${src}` : ''
      }
      el.src = src
    }
  }
}