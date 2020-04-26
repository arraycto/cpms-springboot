import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken,setToken} from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // 统一给所有的请求接口url加上base_api前缀 url = base url + request url
  timeout: 300000 // 请求超时时间
})


// request 拦截器
service.interceptors.request.use(
  config => {
    if (getToken()) {
    
      config.headers['Token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
 
    response => {

	if( response.headers['refresh-token']) { //更新本地token值
		
		setToken(response.headers['refresh-token'])
    }

    let res = response.data
		if (res.code === 5000) {
			// to re-login
			MessageBox.confirm(res.msg, {
				confirmButtonText: '重新登录',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				store.dispatch('user/resetToken').then(() => {
					location.reload()
				})
			})
		}
		return res
   
  },
  error => {
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
