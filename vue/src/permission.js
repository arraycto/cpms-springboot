import router from './router'
import {constantRoutes} from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist


router.beforeEach(async(to, from, next) => {
  // start progress bar
NProgress.start()

const noNeedpermitRoute  = [] //  //固定的基础路由 不需要验证路由权限 

// 遍历出不需要验证路由权限的path
constantRoutes.map(item=>{

    if(item.children === undefined){
        noNeedpermitRoute.push(item.path)
    }else{
        let basePath = item.path

        for (const iterator of item.children) {
            noNeedpermitRoute.push((basePath+'/'+iterator.path).replace("//",'/'))
        }
    }
})

//   console.log(noNeedpermitRoute)
//   console.log("from:"+from.path)
 
document.title = getPageTitle(to.meta.title)  // 设置document的 title标题

const hasToken = getToken()

if (hasToken) {
    if (to.path === '/login') {

      next({ path: '/' })

      NProgress.done()

    } else {
        const pagePermission = store.getters.pagePermission
        const hasGetUserInfo = store.getters.name
        if(pagePermission.length > 0) {
            if(hasGetUserInfo == 'admin') {

                next()

            }else{
                if (noNeedpermitRoute.indexOf(to.path) === -1) { // 需要验证路由权限

                    // 在这里可以判断访问路由是否在授权路由中，没有就跳转到404页面
                    if(![...pagePermission].includes(to.path)){ 

                        next({ path: '/404' })
                    }
                }
            }
        }
        

      // 这里用于判断是否获取了用户信息或页面刷新时store数据丢失，需要重新获取

        if (hasGetUserInfo) {
            next()  // next()没有参数表示直接进入路由对应的组件页面，有参数表示重定向，会再次进入beforeEach方法中执行相关的判断

        } else {
            try {
               
                // 获取用户信息  用一个接口直接把该用户的相关路由权限以及一些基本信息获取到
                const pagePermission =  await store.dispatch('user/refreshUserInfo')
              
                const accessRoutes = await store.dispatch('user/generateRoutes', pagePermission)

                // 动态添加路由
                router.options.routes = accessRoutes  // 一定要更新options 否则动态添加的路由不会显示在菜单栏
                
                router.addRoutes(accessRoutes)

                next({ ...to, replace: true }) // 一定要写成这样，否则刷新页面会出现空白

            } catch (error) {

                await store.dispatch('user/resetToken')
                Message.error(error || 'Has Error')
                next(`/login?redirect=${to.path}`)
                NProgress.done()
            }
        }
    }
} else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {

        next()
    } else {

        next(`/login?redirect=${to.path}`)
        NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
