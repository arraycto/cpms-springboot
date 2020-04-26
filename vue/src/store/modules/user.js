import { login, logout, getInfo } from '@/api/user'
import { setToken, removeToken } from '@/utils/auth'
import { resetRouter, permitRoutes,constantRoutes} from '@/router'


/**
 * 递归过滤用户未授权的路由
 * @param routers          路由文件定义的所有需授权的路由
 * @param permissionRouter  当前登录的用户已授权的访问页面
 * return 返回过滤后能访问的路由信息
 */
function filterRoutes(routers,pagePermission) {
    const res = []
    routers.forEach(route => {
        const tmp = { ...route }
        if(pagePermission.includes(tmp.menuUrl) || tmp.hidden) {
            if (tmp.children) {
                tmp.children = filterRoutes(tmp.children,pagePermission)
            }

            res.push(tmp)
        }
    })
     
    return res
} 


const state = {
    // token: getToken(),
    name: '',
    avatar: '',
    pagePermission: [],  //用户的页面权限
    handlPermission:[]   // 用户的操作权限
}

const mutations = {
    // SET_TOKEN: (state, token) => {
    //     state.token = token
    // },
    SET_NAME: (state, name) => {
        state.name = name
    },
    SET_HANDLPERMISSION:(state,url)=>{
        state.handlPermission = url
    },
    SET_PAGEPERMISSION:(state,url)=>{
        state.pagePermission = url
    }
}

const actions = {
    // 用户登录只需要获取token值，用户其它信息另外调用接口
    login({ commit }, userInfo) {
        const { username, password } = userInfo
        return new Promise((resolve, reject) => {
            login({ username: username.trim(), password: password }).then(response => {
                    const { data } = response
                    if(response.code != 1000) {
                        reject(response.msg)
                    }

                    setToken(data.token) 
                
                    commit('SET_NAME', data.userName)
                    
                    let pagePermission  =  data.rolePagePermission.split(",")
                    let handlePermission =  data.roleHandlePermission.split(",")

                    commit('SET_HANDLPERMISSION',handlePermission)
                    commit('SET_PAGEPERMISSION',pagePermission)
                
                    resolve(pagePermission)

                }).catch(error => {
                    reject(error)
                })
        })
    },

    // 用户刷新页面时，调用此方法；目的是刷新后端生成的token信息
    refreshUserInfo({ commit, state }) {
        return new Promise((resolve, reject) => {
            getInfo().then(response => {
                    const { data } = response
                    setToken(data.token) 
                
                    commit('SET_NAME', data.userName)
                    
                    let pagePermission  =  data.rolePagePermission.split(",")
                    let handlePermission =  data.roleHandlePermission.split(",")

                    commit('SET_HANDLPERMISSION',handlePermission)
                    commit('SET_PAGEPERMISSION',pagePermission)
                
                    resolve(pagePermission)
                    
                }).catch(error => {
                    reject(error)
                })
            })
    },

  
    //动态生成路由信息,可以通过角色判断，如果是管理员则不需要过滤路由，如果不是管理员则通过获取用户信息的接口返回的pagePermission 授权URL进行过滤

    generateRoutes({ commit, state }, pagePermission) {
        return new Promise(resolve => {
            let addRoutes

            if (state.name =='admin') {

                addRoutes =  permitRoutes || []

            } else {

                addRoutes =  filterRoutes(permitRoutes,pagePermission)  // 根据用户角色页面权限动态生成路由
            }
            
            resolve(constantRoutes.concat(addRoutes))
        })
    },

    // user logout
    logout({ commit, state }) {
        return new Promise((resolve, reject) => {  
            commit('SET_NAME', '')
            commit('SET_HANDLPERMISSION', '')
            commit('SET_PAGEPERMISSION','')
            removeToken()
            resetRouter()
            resolve()
        })
    },

    // remove token 
    resetToken({ commit }) {
        return new Promise(resolve => {
            commit('SET_NAME', '')
            commit('SET_HANDLPERMISSION', '')
            commit('SET_PAGEPERMISSION','')
            removeToken()
            resetRouter()
            resolve()
        })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}

