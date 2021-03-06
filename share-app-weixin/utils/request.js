//统一接口封装
const API_BASE_URL = 'http://localhost:8040';
const app = getApp()

const get = (url,data) => { 
  let _url = API_BASE_URL  + url;
  return new Promise((resolve, reject) => {
    // wx.showLoading({
    //   title: "正在加载中...",
    // })
    console.log(_url)
    wx.request({
      url: _url,
      method: 'GET',
      data: data,
      header: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'X-Token': app.globalData.token==null? 'no-token':app.globalData.token
      },
      success(request) {
        console.log(request)
        // wx.hideLoading();
        resolve(request.data)
      },
      fail(error) {
        reject(error)
      }
    })
  });
}
 const post = (url, data,contentType) => {
  let _url = API_BASE_URL  + url;
  switch(contentType){
    case "form" :
      var headerObj = {'content-type' : 'application/x-www-form-urlencoded'};
    break;
    case "json" : 
      var headerObj = {'content-type' : 'application/json'};
    break;
    default :
      var headerObj = {'content-type' : 'application/json'};
  }
  return new Promise((resolve, reject) => {
    wx.request({
      url      : _url,
      data     : data,
      method   : "POST",
      dataType : JSON,
      header: headerObj,
      success(request) {
        resolve(request.data)
      },
      fail(error) {
        reject(error)
      }
    })
  });
}
const put = (url, data,contentType) => {
  let _url = API_BASE_URL  + url;
  switch(contentType){
    case "form" :
      var headerObj = {'content-type' : 'application/x-www-form-urlencoded'};
    break;
    case "json" : 
      var headerObj = {'content-type' : 'application/json'};
    break;
    default :
      var headerObj = {'content-type' : 'application/json'};
  }
  return new Promise((resolve, reject) => {
    wx.request({
      url      : _url,
      data     : data,
      method   : "PUT",
      dataType : JSON,
      header: headerObj,
      success(request) {
        resolve(request.data)
      },
      fail(error) {
        reject(error)
      }
    })
  });
}
module.exports ={
  login:(data) =>{
    console.log("登录")
    return post('/users/login',data,'json') //微信登录
  },
  getShares:(data) =>{
    console.log('获取分享列表')
    return get('/shares/query',data)//获取分享列表
  },
  getNotic:() =>{
    console.log('获取最新公告')
    return get('/notices/one') //获取最新公告
  },
  getMyShares:(data) =>{
    console.log("获取我的投稿")
    return get('/shares/query/myContribution',data) //获取我的投稿
  },
  exchange:(data)=>{
    console.log("兑换分享")
    return post("/shares/exchange",data,'json') //兑换分享
  },
  update:(data)=>{
    console.log("刷新用户信息")
    return get("/users",data)//刷新用户信息
  },
  signIn: (data) => {
    return post('/users/signin',data,'json') // 签到
  },
  contribute:(data) => {
    return post('/shares/contribute',data,'json') //投稿
  },
  // myContribute: (data) => {
  //   return post('/shares/myContribute',data,'json') //查询我的投稿
  // },
  myShare: (data) => {
    return post('/shares/myShare',data,'json') //查询我的兑换
  }
}