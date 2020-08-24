import Vue from 'vue'
import App from './App'

import store from './store'

import api from './common/api.js'

Vue.config.productionTip = false

Vue.prototype.$store = store

App.mpType = 'app'

Vue.prototype.$api = api

const app = new Vue({
	store,
	...App
})


app.$mount()
