import request from '@/utils/request'

/**
 *
 * @param {*} method 请求方法
 * @param {*} url 请求地址
 * @param {*} data post、put 请求的参数
 * @param {*} params get、delete 请求的参数
 * @param {*} config 请求配置，如headers等
 */

// 登录
export function login(params) {
    return request({
        method: 'post',
        url: '/api/auth/login',
        data: params
    })
}

// 发送验证码
export function vaildEmail(params) {
    return request({
        method: 'post',
        url: '/api/auth/vaild-email',
        data: params
    })
}

// 注册请求
export function register(params) {
    return request({
        method: 'post',
        url: '/api/auth/register',
        data: params
    })
}

// export function Me(params) {
//     return request({
//         method: 'get',
//         url: '/api/me',
//         data: params
//     })
// }

//分类-名称
export function categoryName(params) {
    return request({
        method: 'get',
        url: '/category/name',
        data: params
    })
}

export function sortNameButton(params) {
    return request({
        method: 'get',
        url: '/category/sortName',
        data: params
    })
}

// 头部导航-搜索框(未对接  需要修改)
export function idxSearch(params) {
    return request({
        method: "get",
        url: "/api/auth/search/index",
        params
    })
}

// 头部导航-收藏(未对接)
export function getHeaderCollect(params) {
    return request({
        method: "get",
        url: '/user/favorite/all-list',
        data: params
    })
}

// 头部导航-历史(未对接)
export function getHeaderHistory(params) {
    return request({
        method: "get",
        url: "/user/history/list",
        data: params
    })
}

// 头部 个人信息
export function getMe(params) {
    return request({
        method: "get",
        url: "/api/auth/me",
        data: params
    })
}

// 主页视频列表
export function getIndexVideoList(params) {
    return request({
        method: "get",
        url: "/api/auth/usercf",
        data: params
    })
}

// 推荐轮播图
export function getIndexVideocarousel(params) {
    return request({
        method: "get",
        url: "/api/auth/carousel",
        data: params
    })
}

// 推荐轮播图
export function getvideoCF(params) {
    return request({
        method: "get",
        url: "/api/auth/videoCF",
        data: params
    })
}


//获取个人中心展示详情
export function getPersonalCenterInfo(params) {
    return request({
        method: 'get',
        url: '/user/personal/' + params + '/get-center',
        data: params
    })
}

// 分类标题(未对接)
export function getClassification(params) {
    return request({
        method: 'get',
        url: '/api/auth/{categoryID}/listcategory/{ficationID}',
        data: params
    })
}

// 显示默认分类列表
export function categorySelect(params) {
    return request({
        method: 'get',
        url: '/category/select/all-list',
        data: params
    })
}


// 获取分类下的视频
export function categoryVideo(params) {
    return request({
        method: 'get',
        url: '/category/select/video-list',
        params
    })
}


// 通知列表
export function getNotice(params) {
    return request({
        method: "get",
        url: "/social/notice/listSelect",
        params
    })
}


// 删除通知列表信息
export function postNoticeDelete(params) {
    return request({
        method: "post",
        url: "/social/notice/notifyDelete",
        params
    })
}

//获取历史记录列表
export function getHistoryList() {
    return request({
        method: 'get',
        url: "/user/history/list",
        // data: params
    })
}


//删除历史记录功能
export function deleteByVideoId(params) {
    return request({
        method: 'post',
        url: '/user/history/delete-by-videoId',
        params
    })
}


// 热门页视频
export function getShowHot() {
    return request({
        method: "get",
        url: "/api/auth/heat"
    })
}

// 收藏列表
export function getCollection(params) {
    return request({
        method: 'get',
        url: '/user/favorite/all-list',
        data: params
    })
}

// 删除当前收藏
export function deleteCollection(params) {
    return request({
        method: 'get',
        url: '/user/favorite/delete',
        params
    })
}


// 删除当前收藏
export function deleteVideofavorite(params) {
    return request({
        method: 'put',
        url: '/user/favorite/deletefavorite',
        params
    })
}

//删除粉丝
export function deletefan(params) {
    return deletefan({
        method: 'get',
        // url: '/user/fans/{id}/deletefan',
        url: '/user/fans/{followID}/deletefan',
        data: params
    })
}


//加载粉丝列表-
export function fansList(params) {
    return request({
        method: 'get',
        url: '/user/fans/fanlist',
        data: params
    })
}


//添加粉丝-
export function addfan(params) {
    return request({
        method: 'get',
        url: '/user/follow/addfollow',
        params
    })
}


//取消关注-
export function followDelete(params) {
    return request({
        method: 'get',
        // url: '/user/fans/{followeID}/deletefan',
        url: '/user/follow/deletefollow',
        params
    })
}

//加载关注列表-
export function followList(params) {
    return request({
        method: 'get',
        url: '/user/follow/followlist',
        data: params
    })
}


//  动态页信息
export function getDyNamic() {
    return request({
        method: "get",
        url: "/Social/dynamic/dy-list"
    })
}

/**
 * 退出登录
 * @author 赵錾
 * @returns {Promise<axios.AxiosResponse<any>>}
 */
export function logout() {
    return request({
        method: "get",
        url: "/api/auth/logout"
    })
}


//删除订单
export function handleDeleteOrders(params) {
    return request({
        method: 'post',
        url: '/orders/details/delete',
        data: params
    })
}

//加载订单列表
export function loadOrdersList(params) {
    return request({
        method: 'get',
        url: '/orders/details/list',
        data: params
    })
}

// 添加订单功能
export function PayForOrders(params) {
    return request({
        method: "post",
        url: "/orders/details/pay",
        data: params
    })
}


// 上传视频
export function uploadVideo(params, onUploadProgress) {
    return request({
        method: "post",
        url: "/video/upload",
        data: params,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        onUploadProgress: onUploadProgress
    })
}

// 上传封面
export function uploadCover(params, onUploadProgress) {
    return request({
        method: "post",
        url: "/video/uploadCover",
        data: params,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        onUploadProgress: onUploadProgress
    })
}

// 上传视频信息
export function uploadContent(params) {
    return request({
        method: "post",
        url: "/video/uploadContent",
        data: params
    })
}


// 获取视频页所有数量
export function LikeCount(params) {
    return request({
        method: "get",
        url: "/video/all/counts",
        data: params
    })
}

// // 点赞
// export function ForLike(params) {
//     return request({
//         method: "post",
//         url: "/video/handel/like",
//         data: params
//     })
// }
// 取消点赞
export function ForLike(params) {
    return request({
        method: "post",
        url: "/video/handel/removelike",
        data: params
    })
}


// 获取评论列表
export function getComment(params, pageNum) {
    return request({
        method: 'get',
        url: '/api/auth/comment/' + params + '/get-comment',
        data: params, pageNum
    })
}

//添加评论
export function insertComment(params) {
    return request({
        method: "post",
        url: '/api/auth/comment/reply/add-new',
        data: params
    })
}

//添加回复
export function insertCommentOfVideo(params) {
    return request({
        method: "post",
        url: '/api/auth/comment/add-new',
        data: params
    })
}

//获取弹幕
export function LoadBarrage(params) {
    return request({
        method: "get",
        url: "/api/video/loadBarrage",
        params
    })
}

//插入弹幕
export function SendBarrage(params) {
    return request({
        method: "put",
        url: "/barrages/sendBarrage",
        data: params
    })
}

// 获取视频页所有数量
export function allCount(params) {
    return request({
        method: "get",
        url: "/api/video/videoList",
        params
    })
}

// 点赞
export function forLike(params) {
    return request({
        method: "post",
        url: "/video/handle/like",
        data: params
    })
}

// 取消点赞
export function deleteLike(params) {
    return request({
        method: "post",
        url: "/video/handle/removelike",
        data: params
    })
}

// 点踩
export function ForDislike(params) {
    return request({
        method: "post",
        url: "/video/handle/dislike",
        data: params
    })
}

// 取消点踩
export function deleteDisLike(params) {
    return request({
        method: "post",
        url: "/video/handle/removedislike",
        data: params
    })
}

//添加收藏
export function addfavorite(params) {
    return request({
        method: "put",
        url: "/user/favorite/add-favorite",
        params
    })
}

//获取视频标签
export function getvideoTag(params) {
    return request({
        method: "get",
        url: "/api/video/taglist",
        params
    })
}