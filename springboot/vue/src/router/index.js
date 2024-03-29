import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/manage',
    component: () => import('../views/admin/Manage.vue'),
    redirect:"/manage/home",
    children:[
      {path: 'home', name: '首页',component: () => import('../views/admin/Home.vue')},
      {path: 'person', name: '个人信息', component: () => import('../views/admin/Person.vue')},
      {path: 'file', name: '文件管理', component: () => import('../views/admin/File.vue')},
      {path: 'products', name: '商品管理',component: () => import('../views/admin/Products.vue')},
      {path: 'lunbo', name: '轮播图管理', component: () => import('../views/admin/Lunbo.vue')},
      {path: 'orders', name: '订单管理', component: () => import('../views/admin/Orders.vue')},
    ]
  },
  {
    path: '/front',
    component: () => import('../views/front/Front.vue'),
    redirect:"/front/home",
    children: [
      {path: 'home',name:'首页',component:()=>import('../views/front/Home.vue')},
      {path: 'detail', name: '商品详情',component: () => import('../views/front/Detail.vue')},
      {path: 'cart', name: '购物车',component: () => import('../views/front/Cart.vue')},
      {path: 'orders', name: '我的订单',component: () => import('../views/front/Orders.vue')},
      {path: 'person', name: '个人信息', component: () => import('../views/front/Person.vue')},
    ]
  },
  {
    path: '/about',
    name: 'About',
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path:'/login',
    name:'Login',
    component:()=>import('../views/Login.vue')
  },
  {
    path:'/register',
    name:'Register',
    component:()=>import('../views/Register.vue')
  },
  {
    path: '/',
    redirect: '/login'
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to,from,next)=>{
  localStorage.setItem("currentPathName",to.name)      //设置当然路由名称
  store.commit("setPath")   //触发store的数据更新
  next()     //放行路由
})

export default router
