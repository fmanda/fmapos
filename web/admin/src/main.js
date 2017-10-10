import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
import VueRouter from 'vue-router'
import routes from './routes'
import App from './App.vue'
import 'font-awesome/css/font-awesome.min.css'
import locale from 'element-ui/lib/locale/lang/en'

Vue.use(ElementUI,  { locale })
Vue.use(VueRouter)

const router = new VueRouter({
  routes
})

new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
