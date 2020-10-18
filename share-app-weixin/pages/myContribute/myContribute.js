// pages/myContribute/myContribute.js
const API = require('../../utils/request')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    shareList: null
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
    const id = wx.getStorageSync('user').id
    wx.request({
      url: 'http://localhost:8040/shares/myContribute/' + id,
      method: "POST",
      success: res => {
        console.log(res);
        this.setData({
          shareList: res.data.data
        })
      }
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

  }
})