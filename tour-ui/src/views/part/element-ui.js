
import {
  Form,
  FormItem,
  Input,
  Button,
  Select,
  Option,
  Tooltip,
  Pagination,
  Upload,
  DatePicker,
  InputNumber,
  Loading,
  MessageBox,
  Message
} from 'element-ui'

import 'element-ui/lib/theme-chalk/index.css'

export const msg = Message

export function install (Vue) {
  Vue.use(Form)
  Vue.use(FormItem)
  Vue.use(Input)
  Vue.use(Button)
  Vue.use(Select)
  Vue.use(Option)
  Vue.use(Tooltip)
  Vue.use(Pagination)
  Vue.use(Upload)
  Vue.use(DatePicker)
  Vue.use(InputNumber)

  Vue.use(Loading.directive)

  Vue.prototype.$message = Message
  Vue.prototype.$confirm = MessageBox.confirm
}

