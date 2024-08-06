import axios from 'axios'
import router from "@/router";

// 创建一个新的 axios 实例
const request = axios.create({
    baseURL: process.env.VUE_APP_BASEURL,   // 后端的接口地址，格式为 ip:port
    timeout: 30000                          // 请求超时时间设置为30秒
})

// request 拦截器
// 可以在请求发送前对请求做一些处理，例如统一添加 token 或对请求参数进行加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8'; // 设置请求头的内容类型为 JSON
    let user = JSON.parse(localStorage.getItem("xm-user") || '{}')  // 从本地存储中获取缓存的用户信息
    config.headers['token'] = user.token  // 将用户 token 添加到请求头中

    return config  // 返回修改后的请求配置
}, error => {
    console.error('request error: ' + error) // 打印请求错误信息
    return Promise.reject(error)  // 返回拒绝的 Promise
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;  // 获取响应数据

        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res  // 如果是字符串则解析为 JSON 对象
        }
        if (res.code === '401') {  // 检查响应状态码是否为 401（未授权）
            router.push('/login')  // 重定向到登录页面
        }
        return res;  // 返回处理后的响应数据
    },
    error => {
        console.error('response error: ' + error) // 打印响应错误信息
        return Promise.reject(error)  // 返回拒绝的 Promise
    }
)

export default request  // 导出 axios 实例
