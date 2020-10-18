// pages/personal/personal.js

const app = getApp();
const API = require('../../utils/request.js')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo:null,
    isSignin: 0,
    userRole: null,
  
    // linkList:[
    //   {
    //     id:1,
    //     text:'我的兑换',
    //     url:'/pages/wodeduihuan/wodeduihuan'
    //   },
    //   {
    //     id:2,
    //     text:'积分明细',
    //     url:'/pages/jifenmingxi/jifenmingxi'
    //   },
    //   {
    //     id:3,
    //     text:'我的投稿',
    //     url:'/pages/myContribute/myContribute'
    //   }
    // ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.setData({
      userInfo:app.globalData.user
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  /**
   * 登录
   */
  weixinLogin(){
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              console.log(res)
              // 可以将 res 发送给后台解码出 unionId
              app.globalData.userInfo = res.userInfo
              this.login()
              //console.log(this.globalData.userInfo)
              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        }
      }
    })
  },
  login(){
    API.login({
      openId: app.globalData.openId,
      wxNickName: app.globalData.userInfo.nickName,
      avatarUrl: app.globalData.userInfo.avatarUrl
    }).then( res =>{
      wx.showToast({
        title: '登录成功',
        icon: 'success',
        duration: 2000
      })
      const request = JSON.parse(res)
      console.log(request)
      app.globalData.user = request.data.user
      app.globalData.token = request.data.token.token
      wx.setStorageSync('user', app.globalData.user)
      wx.setStorageSync('token', app.globalData.token)
      this.setData({
        userInfo: app.globalData.user,
        isSignin: request.isSignin,
      })
    })
  },
  signIn(){
    API.signIn({
      userId: wx.getStorageSync('user').id
    }).then(res => {
      const req = JSON.parse(res)      
      console.log(req);
      if(req.data.code == '200'){
        wx.showToast({
          title: '签到成功',
          icon: "success",
          tx: '签到成功，记得每天都要来哦'

        })
        this.setData({
          isSignin: 1,
          userInfo: res.data
        })
      }else {
        wx.showModal({
          cancelColor: 'cancelColor',
          title: '签到失败',
          content: '今天已经签到过了哦'
        })
      }
    })
  },
  exit(){
    app.globalData.user = null
    app.globalData.token = null
    this.setData({
      userInfo:null
    })
  },
  wodeduihuan(){  
    wx.navigateTo({
      url: '../wodeduihuan/wodeduihuan'
    })
  },
  jifenmingxi(){
    wx.navigateTo({
      url: '/pages/jifenmingxi/jifenmingxi',
    })
  },
  myContribute(){
    wx.navigateTo({
      url: '../myContribute/myContribute',
    })
  } 
})