import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter)

const routes = [
    {
        // 首页
        path: '/',
        name: 'home',
        component: () => import('../views/Home.vue'),
        redirect: '/index',
        children: [
            {
                path: 'index',
                chinaName: '主题',
                name: 'index',
                meta: {
                    father: '视图',
                    icon: 'el-icon-monitor'
                },
                component: () => import('../views/show/IndexView.vue')
            },
            {
                path: 'search',
                chinaName: '搜索',
                name: 'search',
                meta: {
                    father: '视图',
                    icon: 'el-icon-search',
                    isShow: true
                },
                component: () => import('../views/show/Search.vue')
            },
            {
                //分类
                path: 'classification',
                chinaName: '分类',
                name: 'classification',
                meta: {
                    father: '视图',
                    icon: 'el-icon-menu'
                },
                component: () => import('../views/show/classification.vue')
            },
            {
                //热门
                path: 'showHot',
                chinaName: '热门',
                name: 'showHot',
                meta: {
                    father: '视图',
                    icon: 'el-icon-s-opportunity'
                },
                component: () => import('../views/show/showHot.vue')
            },

            {
                path: 'fans',
                chinaName: '粉丝',
                name: 'fans',
                meta: {
                    father: '个人',
                    icon: 'el-icon-user'
                },
                component: () => import('../views/personal/fans.vue')
            },
            {
                //关注者界面
                path: 'follow',
                chinaName: '关注',
                name: 'follow',
                meta: {
                    father: '个人',
                    icon: 'el-icon-user'
                },
                component: () => import('../views/personal/follow.vue')
            },
            {
                //收藏界面
                path: 'collection',
                chinaName: '收藏',
                name: 'collection',
                meta: {
                    father: '个人',
                    icon: 'el-icon-star-off'
                },
                component: () => import('../views/personal/collection.vue')
            },
            {
                //动态界面
                path: 'dynamic',
                chinaName: '动态',
                name: 'dynamic',
                meta: {
                    father: '个人',
                    icon: 'el-icon-s-promotion'
                },
                component: () => import('../views/personal/dynamic.vue')
            },
            {
                //历史
                path: 'history',
                chinaName: '历史',
                name: 'history',
                meta: {
                    father: '个人',
                    icon: 'el-icon-time'
                },
                component: () => import('../views/personal/history.vue')
            },
            {
                //通知
                path: 'notify',
                chinaName: '通知',
                name: 'notify',
                meta: {
                    father: '个人',
                    icon: 'el-icon-bell'
                },
                component: () => import('../views/personal/notify.vue')
            },

            {
                //他人空间
                path: 'otherSpace',
                chinaName: '他人空间',
                name: 'otherSpace',
                meta: {
                    father: '个人',
                    icon: 'el-icon-bell',
                    isShow: true
                },
                component: () => import('../views/personal/otherSpace.vue')
            },
            {
                //个人中心
                path: 'personalCenter',
                chinaName: '个人中心',
                name: 'personalCenter',
                meta: {
                    father: '个人',
                    icon: 'el-icon-bell',
                    isShow: true
                },
                component: () => import('../views/personal/personalCenter.vue')
            },
            {
                //举报
                path: 'report',
                chinaName: '举报',
                name: 'report',
                meta: {
                    father: '个人',
                    icon: 'el-icon-bell',
                    isShow: true
                },
                component: () => import('../views/personal/report.vue')
            },

            {
                //投稿界面
                path: 'contribute',
                chinaName: '投稿',
                name: 'contribute',
                meta: {
                    father: '投稿',
                    icon: 'el-icon-bell'
                },
                component: () => import('../views/contribute/contribute.vue')
            },
            {
                //投稿管理界面
                path: 'contributeManage',
                chinaName: '投稿管理',
                name: 'contributeManage',
                meta: {
                    father: '投稿',
                    icon: 'el-icon-bell'
                },
                component: () => import('../views/contribute/contributeManage.vue')
            },
            {
                //支付界面
                path: 'pay',
                chinaName: '会员购买',
                name: 'pay',
                meta: {
                    father: '购买',
                    icon: 'el-icon-bell'
                },
                component: () => import('../views/pay/pay.vue')
            },
            {
                //订单界面
                path: 'orders',
                chinaName: '订单',
                name: 'order',
                meta: {
                    father: '购买',
                    icon: 'el-icon-bell'
                },
                component: () => import('../views/pay/orders.vue')
            }
        ]
    },
    {
        //视频页面
        path: '/videoIndex',
        chinaName: '视频',
        name: 'videoIndex',
        component: () => import('../views/video/videoIndex.vue')
    },
    // {
    //     //首页
    //     path: '/',
    //     name: 'index',
    //     component: () => import('../views/show/IndexView.vue')
    // },
    // {
    //     //管理员页面
    //     path: '/adminIndex',
    //     name: 'adminIndex',
    //     component: () => import('../views/admin/adminIndex.vue')
    // },
    // {
    //     //管理员登录页面
    //     path: '/adminLogin',
    //     name: 'adminLogin',
    //     component:() => import('../views/admin/adminLogin.vue')
    // },
    // {
    //     //投稿界面
    //     path: '/contribute',
    //     name: 'contribute',
    //     component:() =>import('../views/contribute/contribute.vue')
    // },
    // {
    //     //投稿管理界面
    //     path: '/contributeManage',
    //     name: 'contributeManage',
    //     component:() =>import('../views/contribute/contributeManage.vue')
    // },
    // {
    //     //购买界面
    //     path: '/commodity',
    //     name: 'commodity',
    //     component:() =>import('../views/pay/commodity.vue')
    // },
    // {
    //     //支付界面
    //     path: '/pay',
    //     name: 'pay',
    //     component:() =>import('../views/pay/pay.vue')
    // },
    // {
    //     //收藏界面
    //     path: '/collection',
    //     name: 'collection',
    //     component:() =>import('../views/personal/collection.vue')
    // },
    // {
    //     //动态界面
    //     path: '/dynamic',
    //     name: 'dynamic',
    //     component:() =>import('../views/personal/dynamic.vue')
    // },
    // {
    //     //粉丝界面
    //     path: '/fans',
    //     name: 'fans',
    //     component:() =>import('../views/personal/fans.vue')
    // },
    // {
    //     //关注者界面
    //     path: '/follow',
    //     name: 'follow',
    //     component:() =>import('../views/personal/follow.vue')
    // },
    // {
    //     //历史
    //     path: '/history',
    //     name: 'history',
    //     component:() =>import('../views/personal/history.vue')
    // },
    // {
    //     //通知
    //     path: '/notify',
    //     name: 'notify',
    //     component:() =>import('../views/personal/notify.vue')
    // },
    // {
    //     //他人空间
    //     path: '/otherSpace',
    //     name: 'otherSpace',
    //     component:() =>import('../views/personal/otherSpace.vue')
    // },
    // {
    //     //个人中心
    //     path: '/personalCenter',
    //     name: 'personalCenter',
    //     component:() =>import('../views/personal/personalCenter.vue')
    // },
    // {
    //     //举报
    //     path: '/report',
    //     name: 'report',
    //     component:() =>import('../views/personal/report.vue')
    // },
    // {
    //     //分类
    //     path: '/classification',
    //     name: 'classification',
    //     component:() =>import('../views/show/classification.vue')
    // },
    // {
    //     //搜索页
    //     path: '/Search',
    //     name: 'Search',
    //     component:() =>import('../views/show/Search.vue')
    // },
    // {
    //     //热门
    //     path: '/showHot',
    //     name: 'showHot',
    //     component:() =>import('../views/show/showHot.vue')
    // },
    // {
    //     //登录
    //     path: '/login',
    //     name: 'login',
    //     component:() =>import('../views/user/login.vue')
    // },
    // {
    //     //注册
    //     path: '/reg',
    //     name: 'reg',
    //     component:() =>import('../views/user/reg.vue')
    // },
    // {
    //     //视频页面
    //     path: '/videoIndex',
    //     name: 'videoIndex',
    //     component:() =>import('../views/video/videoIndex.vue')
    // },

]

const router = new VueRouter({
    routes
})

export default router
