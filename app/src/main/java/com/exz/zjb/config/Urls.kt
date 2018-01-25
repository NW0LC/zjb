package com.exz.zjb.config

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
object Urls{
    var APP_ID = ""
    var url = "http://zjb.xzsem.cn/"

    /**
     *  获取验证码
      */
    var ObtainCode =url+ "App/Account/ObtainCode.aspx"
    /**
     *  用户注册
      */
    var Register =url+ "App/Account/Register.aspx"
    /**
     *  忘记密码
      */
    var ForgetPassword =url+ "App/Account/ForgetPassword.aspx"
    /**
     *  登录
      */
    var Login =url+ "App/Account/Login.aspx"
    /**
     *  提交实名认证审核资料
      */
    var SubmitAuthentication =url+ "APP/Account/Apply/SubmitAuthentication.aspx"
    /**
     *  编辑实名认证审核资料
      */
    var editAuthentication =url+ "APP/Account/Apply/EditAuthentication.aspx"
    /**
     *  实名认证审核结果接口
      */
    var checkAuthentication =url+ "APP/Account/Apply/CheckAuthentication.aspx"
    /**
     *  首页_Banner
      */
    var banner =url+ "App/Home/Banner.aspx"
    /**
     *  首页_热点新闻(5条)
      */
    var topNews =url+ "App/Home/TopNews.aspx"
    /**
     *  热点新闻
      */
    var hotNews =url+ "App/BasicData/HotNews.aspx"
    /**
     *  首页_最新出售车源（4条）
      */
    var newCar =url+ "App/Home/NewCar.aspx"
    /**
     *  系统消息列表
      */
    var message =url+ "App/Account/Message.aspx"
    /**
     *  出售车辆列表
      */
    var sellList =url+ "App/Machine/SellList.aspx"
    /**
     *  省市
      */
    var areaList =url+ "App/BasicData/AreaList.aspx"
    /**
     *  出售车辆详情
      */
    var getSellInfo =url+ "App/Machine/GetSellInfo.aspx"
    /**
     *  求购车辆列表
      */
    var  buyList =url+ "App/Machine/BuyList.aspx"
    /**
     * 出租车辆列表
      */
    var  leaseList =url+ "App/Machine/LeaseList.aspx"
    /**
     * 求租车辆列表
      */
    var  rentList =url+ "App/Machine/RentList.aspx"
    /**
     * 招聘信息列表
      */
    var recruiterList =url+ "App/Job/RecruiterList.aspx"
    /**
     * 求职信息列表
      */
    var jobWantedList =url+ "App/Job/JobWantedList.aspx"
    /**
     * 收藏或取消收藏
      */
    var collectAction =url+ "App/Account/CollectAction.aspx"
    /**
     * 出租车辆详情
      */
    var getLeaseInfo =url+ "App/Machine/GetLeaseInfo.aspx"
    /**
     * 求租车辆详情
      */
    var getRentInfo =url+ "App/Machine/GetRentInfo.aspx"
    /**
     * 招聘信息详情
      */
    var getRecruiterInfo =url+ "App/Job/GetRecruiterInfo.aspx"
    /**
     * 求职信息详情
      */
    var getJobWantedInfo =url+ "App/Job/GetJobWantedInfo.aspx"

    /**
     * 求购车辆详情
      */
    var getBuyInfo =url+ "App/Machine/GetBuyInfo.aspx"



    /**
     * 上传车辆图片
      */
    var uploadCarImg =url+ "App/Machine/UploadCarImg.aspx"
    /**
     * 发布出售
      */
    var publishSell =url+ "App/Machine/PublishSell.aspx"
    /**
     * 发布求购
      */
    var publishBuy =url+ "App/Machine/PublishBuy.aspx"
    /**
     * 发布出租
      */
    var publishLease =url+ "App/Machine/PublishLease.aspx"
    /**
     * 发布求租
      */
    var publishRent =url+ "App/Machine/PublishRent.aspx"
    /**
     * 发布招聘
      */
    var publishRecruiter =url+ "App/Job/PublishRecruiter.aspx"
    /**
     * 发布求职
      */
    var publishJobWanted =url+ "App/Job/PublishJobWanted.aspx"
    /**
     * 编辑出售
      */
    var editSell =url+ "App/Machine/EditSell.aspx"
    /**
     * 编辑求购
      */
    var editBuy =url+ "App/Machine/EditBuy.aspx"
    /**
     * 编辑出租
      */
    var editLease =url+ "App/Machine/EditLease.aspx"
    /**
     * 编辑求租
      */
    var editRent =url+ "App/Machine/EditRent.aspx"
    /**
     * 编辑招聘信息
      */
    var editRecruiter =url+ "App/Job/EditRecruiter.aspx"
    /**
     * 编辑求职信息
      */
    var editJobWanted =url+ "App/Job/EditJobWanted.aspx"
    /**
     * 出售车辆详情【编辑用】
      */
    var machineSellInfo =url+ "App/Machine/MachineSellInfo.aspx"
    /**
     * 求购车辆详情【编辑用】
      */
    var machineBuyInfo =url+ "App/Machine/MachineBuyInfo.aspx"
    /**
     * 出租车辆详情【编辑用】
      */
    var machineLeaseInfo =url+ "App/Machine/MachineLeaseInfo.aspx"
    /**
     * 求租车辆详情【编辑用】
      */
    var machineRentInfo =url+ "App/Machine/MachineRentInfo.aspx"
    /**
     * 招聘信息详情【编辑用】
      */
    var recruiterInfo =url+ "App/Job/RecruiterInfo.aspx"
    /**
     * 求职信息详情【编辑用】
      */
    var jobWantedInfo =url+ "App/Job/JobWantedInfo.aspx"

    /**
     * 我的出售车辆列表
      */
    var mySellList =url+ "App/Account/MySellList.aspx"
    /**
     * 我的求购车辆列表
      */
    var myBuyList =url+ "App/Account/MyBuyList.aspx"
    /**
     * 我的出租车辆列表
      */
    var myLeaseList =url+ "App/Account/MyLeaseList.aspx"
    /**
     * 我的求租车辆列表
      */
    var myRentList =url+ "App/Account/MyRentList.aspx"
    /**
     * 我的招聘信息列表
      */
    var myRecruiterList =url+ "App/Account/MyRecruiterList.aspx"
    /**
     * 我的求职信息列表
      */
    var MyJobWantedList =url+ "App/Account/MyJobWantedList.aspx"
    /**
     * 我的收藏列表
      */
    var myCollection =url+ "App/Account/MyCollection.aspx"
    /**
     * 删除出售信息
     */
    var deleteSell =url+ "App/Machine/DeleteSell.aspx"
    /**
     * 删除求购信息
     */
    var deleteBuy =url+ "App/Machine/DeleteBuy.aspx"
    /**
     * 删除出租信息
     */
    var deleteLease =url+ "App/Machine/DeleteLease.aspx"
    /**
     * 删除求租信息
     */
    var deleteRent =url+ "App/Machine/DeleteRent.aspx"
    /**
     * 删除招聘信息
     */
    var deleteRecruiter =url+ "App/Job/DeleteRecruiter.aspx"
    /**
     * 删除求职信息
     */
    var deleteJobWanted =url+ "App/Job/DeleteJobWanted.aspx"

    /**
     * 用户协议
     */
    var Information =url+ "APP/BasicData/Information.aspx?i=1"

    /**
     * 使用帮助
     */
    var Help =url+ "APP/BasicData/Help.aspx"

    /**
     * 使用帮助
     */
    var Information2 =url+ "APP/BasicData/Information.aspx?i=2"

    /**
     * 获取用户信息
     */
    var GetUserInfo =url+ "App/Account/GetUserInfo.aspx"

    /**
     * 修改用户资料
     */
    var ModifyUserInfo =url+ "App/Account/ModifyUserInfo.aspx"

    /**
     * 修改密码
     */
    var ModifyPassword =url+ "App/Account/ModifyPassword.aspx"

    /**
     * 意见反馈
     */
    var SubmitFeedback =url+ "App/Account/SubmitFeedback.aspx"

    /**
     * 退出
     */
    var Exit =url+ "App/Account/Exit.aspx"
    /**
     * 我的浏览记录
     */
    var MyBrowse =url+ "App/Account/MyBrowse.aspx"
}