import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * 固定的基础路由每个用户都可以访问到，不需要进行权限校验
 *
 */
export const constantRoutes = [
	{
		path: '/login',
		component: () => import('@/views/login/index'),
		hidden: true  // 由于侧边栏菜单是由路由生成的，这里通过hidden 来控制是否显示 true为不显示
	},

	{
		path: '/404',
		component: () => import('@/views/404'),
		hidden: true  // 
	},

	{
		path: '/',
		component: Layout,
		redirect: '/dashboard',
		name: 'Dashboard',
		meta: { title: '首页', icon: 'dashboard' },
		children: [
			{
				path: 'dashboard',
				name: 'Dashboard',
				component: () => import('@/views/dashboard/index'),
				meta: { title: '首页', icon: 'dashboard' }
			},
		]
	},

	{
		path: '/test',
		component: Layout,
		redirect: '/test/upload',
		name: 'test',
		meta: { title: '功能测试', icon: 'peoples' },
		children: 
		[
			{
				path: 'upload',
				name: 'upload',
				component: () => import('@/views/test/upload'),
				meta: { title: '上传文件' },
			},

			{
				path: 'websocket',
				name: 'websocket',
				component: () => import('@/views/test/websocket'),
				meta: { title: 'websocket' },
			},
		]
	},

	
]

const createRouter = () => new Router({
  // mode: 'history', // 这个模式需要服务端支持
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// 登出时重置路由
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router

// 需要授权的全部路由数据
export const permitRoutes = [
/**
 *  menuUrl : 用于数据库记录角色拥有菜单的权限  permit：用于数据库记录角色拥有页面操作的权限
 *  记录形式为：permit:[{name:'添加用户','url':'/user/add/**,/role/allRole/**'}], 表示拥有了添加用户的权限，也就同时有了获取角色列表的权限
 *  由于需要配合后端进行restful api 风格的权限判断，所以记录操作权限的url需要在后面加上 " /** "
 */
	{
		path: '/userRole',
		component: Layout,
		redirect: '/userRole/user',
		name: 'userRole',
		meta: { title: '用户角色', icon: 'peoples' },
		menuUrl:'/userRole',  
		children: 
		[
			{
				path: 'user',
				name: 'user',
				component: () => import('@/views/userRole/user'),
				meta: { title: '用户管理' },
				menuUrl:'/userRole/user',
				permit:[
					{name:'查看用户列表','url':'/user/allUser/**'},
					{name:'添加用户','url':'/user/add/**'},
					{name:'编辑用户','url':'/user/edit/**'},
					{name:'删除用户','url':'/user/delete/**'},
				]
			},

			{
				path: 'role',
				name: 'role',
				component: () => import('@/views/userRole/role'),
				meta: { title: '角色管理' },
				menuUrl:'/userRole/role',
				permit:[
					{name:'查看角色列表','url':'/role/allRole/**'},
					{name:'添加角色','url':'/role/add/**'},
					{name:'编辑角色','url':'/role/edit/**'},
					{name:'删除角色','url':'/role/delete/**'}
				]
			}
		]
	},
	
  // 把path: '*' 放在路由列表的最后 
  { path: '*', redirect: '/404', hidden: true }
]