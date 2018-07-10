package com.emiaoqian.express.utils;

public class Constants {

    public static final String CONST_HOST = "https://www.emiaoqian.com";
    public static final String CONST_HOST1 = "https://cpi.emiaoqian.com";

    //以后app里面所有图片里面的地址
    public static final String IMAGE="https://mqsd.oss-cn-beijing.aliyuncs.com";

//     public static final String CONST_HOST = "http://192.168.3.112";
//     public static final String CONST_HOST1 = "http://192.168.3.112:8081"; //所有被当成网页的都走这个地址 。。1.29


    //public static final String CONST_HOST = "http://192.168.3.59"; //所有被当成网页的都走这个地址 。。1.29

    public static final String UPDATA=CONST_HOST1+"/v1/appVersion";


    //登录的时候提交表单的地址
     public static final String GET_USE_INFOR= CONST_HOST+"/v1/app/saveDeviceNo";


    //192.168.3.112 //永久地址

   // public static final String CONST_HOST = "http://192.168.3.112";

    //真正下载的更新地址
    public static final String UP_DATA_URL="https://www.emiaoqian.com/v1/appVersion";




    //登录有两种方式，一种是验证码登录，另一种是密码登录
    public static final String LOGIN = CONST_HOST1 + "/v1/agent/login";

    public static final String LOGIN_V2 = CONST_HOST1 + "/v2/agent/login";

    //验证码登录 4.23
    public static final String SEND_CODE = CONST_HOST1 + "/v1/user/sendCode";

    //获取用户信息（已取件和代取件信息）
    public static final String USER_INFOR = CONST_HOST1+"/v2/agent/getNonTakeReceivedCount";

    //获取用户信息（头像，真实名字等！但是url里面是v2） 5.17
    public static final String USER_INFOR_DETAIL=CONST_HOST1+"/v2/agent/getAgentInfo";

    //查询运单号 4.24
    public static final String GET_EXPRESS_INFOR = CONST_HOST1+"/vx/evidence/logisticInfo";
    public static final String ABOUT_ME=CONST_HOST+"/aboutApp";

    public static final String REGIST=CONST_HOST+"/sys/pub/regist";
    public static final String LOGINOUT=CONST_HOST+"/sys/pub/logout";




    //快件
    public static final String EXPRESS=CONST_HOST+"/mobile_app/evidence/index";

    //存证
    public static final String SIGN=CONST_HOST+"/mobile_app/fastgood/index";

    //钱包
    public static final String WALLET =CONST_HOST+"/mobile_app/wallet/index";


    //客服
    public static final String SERVERPEOPLE=CONST_HOST+"/mobile_app/customservice/index";

    //设置
    public static final String URLSETTING=CONST_HOST+"/mobile_app/setting/index";

    //消息
    public static final String MESSAGE_EXPRESS=CONST_HOST+"/mobile_app/agent/message";



    //待取件
    public static final String WITE_ME_RECEIVER=CONST_HOST+"/mobile_app/fastgood/index";


    //已签收
    public static final String ALREADY_SIGN=CONST_HOST+"/mobile_app/evidence/index";

    //我的头像
    public static final String HEAD_ICON =CONST_HOST+"/mobile_app/agent/index";

    //招募
    public static final String EXPRESS_OFFIC=CONST_HOST+"/mobile_app/agent/recruit";

    //我的名片
    public static final String MY_CARD=CONST_HOST+"/mobile_app/agent/businesscard";

    //推荐有奖
    public static final String PRESENT =CONST_HOST+"/mobile_app/agent/recommend";

   //资讯
   public static final String MIAOQIAN_NEW=CONST_HOST+"/mobile_app/news";

    //权益
   public static final String MIAOQIAN_EQUITY=CONST_HOST+"/mobile_app/rights";

  //生活
   public static final String MIAOQAIN_LIFE=CONST_HOST+"/mobile_app/life";




    /********************************以上都是网页***********/


    //查询标签
    public static final String QUERY_TIP=CONST_HOST1+"/vx/evidence/getReceiverLabel";

    //添加标签
    public static final String ADD_TIP=CONST_HOST1+"/vx/evidence/addReceiverLabel";

    //删除标签
    public static final String DELETE_TIP=CONST_HOST1+"/vx/evidence/delReceiverLabel";




   /**我的签收状态**/

    public static final String WITE_TO_PAY=CONST_HOST1+"/sys/mydoc/documentManage/state/toPay";
    /***我的签收状态***/

    public static final String sian_save=CONST_HOST1+"/vx/evidence/saveEvidence";


    //获取秒签小哥名片
    public static final String GET_USER_CARDIMAGE=Constants.CONST_HOST1+"/v2/agent/getPhotoList";


    //同意法律条款
    public static final String AGREE_LAW=CONST_HOST+"/mobile_app/pub/agreement";

    //扫描二维码 判断是继续流程还是跳转网页
    public static final String SCAN_QR=CONST_HOST1+"/v2/agent/scan";













}
