import Vue from 'vue'
import Vuex from 'vuex'

import * as config from '../config';

Vue.use(Vuex)

const defConfig = config.def

/**
 * 提示
 */
const msg = (title, duration = 1500, mask = false, icon = 'none') => {
	//统一提示方便全局修改
	if (Boolean(title) === false) {
		return;
	}
	uni.showToast({
		title,
		duration,
		mask,
		icon
	});
}

let userInfo = undefined;

/**
 * 退出登录
 */
const logout = () => {
	userInfo = undefined
	uni.removeStorage({
		key: 'userInfo'
	});
}

/**
 * 设置用户
 */
const setUserInfo = (user) => {
	userInfo = user;
	uni.setStorageSync('userInfo', user)
}

/**
 * 获取用户
 */
const getUserInfo = (i) => {
	if (!userInfo)
		userInfo = uni.getStorageSync('userInfo')
	return userInfo;
}

//登陆锁
let loginLock = false;

const request = (_group, _method, data = {}, failCallback) => {
	//异步请求数据
	return new Promise(resolve => {
		//是否登录
		if (!userInfo || !userInfo.accessToken) {
			userInfo = uni.getStorageSync('userInfo')
		}
		//认证token
		let accessToken = userInfo ? userInfo.accessToken : ''
		let baseUrl = config.def().baseUrl //请求路径

		uni.request({
			url: baseUrl + '/api-service',
			data: {
				...data,
				_group,
				_method
			},
			method: 'POST',
			header: {
				'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
				'ACCESSTOKEN': accessToken
			},
			success: (res) => {
				console.log('请求' + baseUrl + '/m.api' + ',结果返回' + JSON.stringify(res));
				if (res.statusCode === 200) {
					if (res.data.statusCode === 200) {
						resolve(res.data.data);
					} else if (res.data.statusCode === 1001) {
						if (failCallback) {
							failCallback(res.data.data)
						}
						if (!loginLock) {
							loginLock = true
							uni.showModal({
								title: '登录提示',
								content: '您尚未登录，是否立即登录？',
								showCancel: true,
								confirmText: '登录',
								success: (e) => {
									if (e.confirm) {
										uni.navigateTo({
											url: '/pages/public/login'
										})
									}
								},
								fail: () => {},
								complete: () => {
									loginLock = false
								}
							})
						}

					} else {
						if (failCallback) {
							failCallback(res.data)
						} else {
							msg(res.data.msg);
						}
					}
				}
			}
		})
	})
};

var api = {
	request,
	msg,
	logout,
	setUserInfo,
	getUserInfo
};


export default api
