/**
 * 本机环境
 */
const dev = {
	baseUrl: 'http://127.0.0.1:9526',
	h5Appid: 'wxb66b599f7f61b46f',
	debug: false
}
/**
 * 生产环境
 */
const prod = {
	baseUrl: 'http://203.195.133.161:8123',
	h5Appid: 'wxb66b599f7f61b46f',
	debug: false
}
/**
 * 环境选择
 */
export function def() {
	return dev
}
