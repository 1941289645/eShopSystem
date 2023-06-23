import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from "./store"
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css';
import request from "@/utils/request";
import {Message} from 'element-ui'

Vue.config.productionTip = false

Vue.use(ElementUI, {size:"mini"});

Vue.prototype.request=request

//注意：这里使用的$message,所以在使用时候也是this.$message
Vue.prototype.$message = function(msg){
  return Message({
    message: msg,
    duration: 1000
  })
}
//分别对success、warning和error等样式进行设置
Vue.prototype.$message.success = function (msg) {
  return Message.success({
    message: msg,
    duration: 1000
  })
}
Vue.prototype.$message.warning = function (msg) {
  return Message.warning({
    message: msg,
    duration: 1000
  })
}
Vue.prototype.$message.error = function (msg) {
  return Message.error({
    message: msg,
    duration: 1000
  })
}

// Vue.prototype.$message = function(msg){
//     //根据msg对象中的type类型判断消息提示的类型
//     let msgObj = {
//       message:msg.message?msg.message:msg,
//       duration: 3000
//     }
//     let msgType = msg.type || ""
//     switch(msgType) {
//       case 'success':
//         return Message.success(msgObj);
//
//       case 'warning':
//         return Message.warning(msgObj);
//
//       case 'error':
//         return Message.error(msgObj);
//
//       default:
//         return Message(msgObj);
//   }
// }

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
