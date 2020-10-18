// pages/jifenmingxi/jifenmingxi.js
const app = getApp();
const API = require('../../utils/request.js')
const dateUtil = require('../../utils/util');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    bonusList: null,
    imgUrl: null,
    id: wx.getStorageSync('user').id
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
    
    var that = this
    console.log("1111111111111111");
    this.getBonusList()

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
  getBonusList(){
    // console.log("222222222222222222222222222222222222222222222");
    
    var that = this
    wx.request({
      url: 'http://localhost:8040/users/bonus/' + this.data.id,
      method: 'POST',
      success(res){
        that.setData({
          bonusList : res.data.data
        })
      }


    })
  }
})