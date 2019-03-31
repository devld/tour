import { IMAGE_PATH_PREFIX } from '../config'

/**
 * 给 img 的 src 添加路径前缀
 */
export default function (el, binding) {
  if (el.tagName === 'IMG') {
    if (binding.oldValue !== binding.value)
      el.src = binding.value ? `${IMAGE_PATH_PREFIX}${binding.value}` : ''
  }
}
