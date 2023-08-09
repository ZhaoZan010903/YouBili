import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue'
import router from './router'
import store from './store'
import axios from "axios";
import qs from "qs"


Vue.use(qs)
Vue.prototype.axios = axios;
Vue.use(ElementUI);
Vue.prototype.$bus = new Vue();


Vue.config.productionTip = false
axios.defaults.withCredentials = true;  //允许携带session


new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
