export function timeFormatter (timestamp) {
  //获取js 时间戳
  let time = parseInt((new Date().getTime() - timestamp) / 1000)
  let s
  if (time < 20) {
    return '刚刚'
  } else if (time < 60 * 1) {
    return time + '秒前'
  } else if ((time < 60 * 60) && (time >= 60 * 1)) {
    s = Math.floor(time / 60)
    return s + '分钟前'
  } else if ((time < 60 * 60 * 24) && (time >= 60 * 60)) {
    s = Math.floor(time / 60 / 60)
    return s + '小时前'
  } else if ((time < 60 * 60 * 24 * 3) && (time >= 60 * 60 * 24)) {
    s = Math.floor(time / 60 / 60 / 24)
    return s + '天前'
  } else {
    const date = new Date(timestamp)
    let month = date.getMonth() + 1
    month = (month > 9 ? '' : '0') + month
    let day = date.getDate()
    day = (day > 9 ? '' : '0') + day
    let hour = date.getHours()
    hour = (hour > 9 ? '' : '0') + hour
    let minute = date.getMinutes()
    minute = (minute > 9 ? '' : '0') + minute
    let second = date.getSeconds()
    second = (second > 9 ? '' : '0') + second
    return date.getFullYear() + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second
  }
}
