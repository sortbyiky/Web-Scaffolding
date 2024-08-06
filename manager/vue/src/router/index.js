import Vue from 'vue'
import VueRouter from 'vue-router'

// 注册 VueRouter 插件
Vue.use(VueRouter)

// 解决在 Vue-Router 3.0 以上版本中点击同一路由导致的报错问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'), // 异步加载 Manager 组件
    redirect: '/home',  // 默认重定向到主页
    children: [ // 嵌套路由
      { path: '403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员信息' }, component: () => import('../views/manager/Admin') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: '公告信息' }, component: () => import('../views/manager/Notice') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'), // 异步加载 Front 组件
    children: [ // 嵌套路由
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/front/Home') },
      { path: 'person', name: 'Person', meta: { name: '个人信息' }, component: () => import('../views/front/Person') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404.vue') }, // 捕获所有未匹配的路由
]

// 创建 VueRouter 实例
const router = new VueRouter({
  mode: 'history', // 使用 HTML5 History 模式
  base: process.env.BASE_URL, // 基础路径，来自环境变量
  routes // 路由配置
})

// 注：如果不需要前台的项目，可以注释掉该路由守卫
// 路由守卫，用于拦截导航并执行逻辑判断
router.beforeEach((to, from, next) => {
  // 从本地存储中获取用户信息
  let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
  if (to.path === '/') { // 如果目标路径为根路径
    if (user.role) { // 检查用户角色
      if (user.role === 'USER') { // 如果用户角色为普通用户
        next('/front/home') // 导航到前台主页
      } else {
        next('/home') // 导航到后台主页
      }
    } else {
      next('/login') // 如果没有用户角色，导航到登录页面
    }
  } else {
    next() // 如果不是根路径，直接允许导航
  }
})

export default router // 导出路由实例
