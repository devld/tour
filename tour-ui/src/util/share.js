export default function (via, data) {
  const func = shares[via]
  if (!func) {
    return console.warn('unsupported share method')
  }
  return func(data)
}

const shares = {
  'qq': ({ title, desc, url, image }) => {
    return new Promise((resolve, reject) => {
      window.open(`https://connect.qq.com/widget/shareqq/index.html?url=${encodeURIComponent(url)}&title=${encodeURIComponent(title)}&summary=${desc}&pics=${image}`)
      resolve()
    })
  },
  'weixin': ({ title, desc, url, image }) => {
    return new Promise((resolve, reject) => {
      window.open(`https://cli.im/api/qrcode/code?text=${encodeURIComponent(url)}&mhid=sELPDFnok80gPHovKdI`)
      resolve()
    })
  },
  'weibo': ({ title, desc, url, image }) => {
    return new Promise((resolve, reject) => {
      reject('暂未实现')
    })
  }
}
