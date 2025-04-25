import axios from "axios";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 30000  // Backend API timeout
})

// Request interceptor
// Can be used to process the request before it's sent
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = JSON.parse(localStorage.getItem("xm-user") || '{}')
    config.headers['token'] = user.token || ''
    return config
}, error => {
    return Promise.reject(error)
});

// Response interceptor
// Can be used to handle the response results
request.interceptors.response.use(
    response => {
        let res = response.data;
        // If the response is a file
        if (response.config.responseType === 'blob') {
            return res
        }
        // Show message when validation failed
        if (res.code === '401') {
            ElMessage.error(res.msg)
            router.push('/login')
        }
         // Handle case where the server returns a string
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        if (error.response.status === 404) {
            ElMessage.error('API point not found')
        } else if (error.response.status === 500) {
            ElMessage.error('System error, check the backend system')
        } else {
            console.error(error.message)
        }
        return Promise.reject(error)
    }
)

export default request
