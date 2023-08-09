import axios from 'axios'

const instance = axios.create({
    // API接口的base_url
    // baseURL: 'http://youbili2303.nat300.top',
    baseURL: 'http://localhost:10001',
    // 超时时间
    timeout: 20000000,
    // headers配置
    headers: {
        // 'Content-Type': 'application/json;charset=utf-8',
        'Content-Type': 'application/x-www-form-urlencoded',
        // 'X-Requested-With': 'XMLHttpRequest'
    }
})

instance.interceptors.request.use(
    config => {
        // 可以在发送请求前做一些对config的处理，例如给headers添加token
        const token = JSON.parse(localStorage.getItem('youbiliToken'))
        if (token) {
            config.headers['Authorization'] = token
        }
        return config
    },
    error => {
        // 发送请求错误时做一些处理，例如抛出错误
        return Promise.reject(error)
    }
)

instance.interceptors.response.use(
    response => {
        // 对响应数据做些处理，例如验证token是否过期等
        return response.data
    },
    error => {
        // 对响应Error做些处理，例如判断响应状态码返回对应错误信息
        // if (error.response) {
        //     switch (error.response.status) {
        //         case 400:
        //             error.message = '请求错误(400)'
        //             break
        //         case 401:
        //             error.message = '未授权，请重新登录(401)'
        //             break
        //         case 403:
        //             error.message = '拒绝访问(403)'
        //             break
        //         case 404:
        //             error.message = '请求出错(404)'
        //             break
        //         case 408:
        //             error.message = '请求超时(408)'
        //             break
        //         case 500:
        //             error.message = '服务器错误(500)'
        //             break
        //         case 501:
        //             error.message = '服务未实现(501)'
        //             break
        //         case 502:
        //             error.message = '网络错误(502)'
        //             break
        //         case 503:
        //             error.message = '服务不可用(503)'
        //             break
        //         case 504:
        //             error.message = '网络超时(504)'
        //             break
        //         case 505:
        //             error.message = 'HTTP版本不受支持(505)'
        //             break
        //         default:
        //             error.message = `连接出错(${error.response.status})!`
        //     }
        // } else {
        //     error.message = '连接服务器失败!'
        // }
        return Promise.reject(error)
    }
)

/**
 *
 * @param {*} method 请求方法
 * @param {*} url 请求地址
 * @param {*} data post、put 请求的参数
 * @param {*} params get、delete 请求的参数
 * @param {*} config 请求配置，如headers等
 */
function request({method, url, data = {}, params = {}, headers = {}, onUploadProgress}) {
    return instance({
        method,
        url,
        data,
        params,
        headers,
        onUploadProgress,
    })
}

export default request
