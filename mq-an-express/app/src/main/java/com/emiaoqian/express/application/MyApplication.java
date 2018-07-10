package com.emiaoqian.express.application;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.emiaoqian.express.BuildConfig;
import com.emiaoqian.express.activity.WebviewtoNewActivity;
import com.emiaoqian.express.utils.ChannelUtil;
import com.emiaoqian.express.utils.Constants;
import com.emiaoqian.express.utils.FetchPatchHandler;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.ToastUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
//import com.umeng.socialize.PlatformConfig;

/**
 * Created by xiong on 2018/4/4.
 */

public class MyApplication extends Application {


    public static Context mcontext;


    //微信热修复
    private ApplicationLike tinkerApplicationLike;

    @Override
    public void onCreate() {
        super.onCreate();
        mcontext=this;
        UMConfigure.setLogEnabled(true);
        String channel = ChannelUtil.getChannel(this);

        //改版之后应该是这个(友盟统计),最后一个参数是友盟推送的 （线上的）
        UMConfigure.init(this, "5adf3a9af43e4837ec000222", channel, UMConfigure.DEVICE_TYPE_PHONE, "d89b1aa23f36548e1804388a804c7350");
        //自己环境的
        //UMConfigure.init(this, "5ad8b78af29d9803c7000325", channel, UMConfigure.DEVICE_TYPE_PHONE, "00351a83ad4480c44f501265c39ec4b2");
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setSessionContinueMillis(3000);

        //AjnLBV5S3uM9yG0xngVTvl7ONIGeNLyjaEsPG3vDPQd_
        //AomTTyYo_fIh1MLsr0e5cr1gTIE0gbAXaahxTaLQVM9H

        //友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtil.e("设备号是"+deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
                LogUtil.e(s,s1);
            }
        });




        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
               LogUtil.e("---message--"+msg.custom);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //通知来到的时候走这个方法！！(就是通知还没显示在状态栏上面，只是数据来了)
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 通知的回调方法
             * @param context
             * @param msg
             */
            @Override
            public void dealWithNotificationMessage(Context context, UMessage msg) {

                LogUtil.e("---message1--"+msg.after_open);
                String after_open = msg.after_open;
//                switch (after_open){
//                    case "go_app":
//                    case "go_url":
//                    case "go_activity":
//                    case "go_custom":
//                        Intent intent = new Intent(mcontext, WebviewtoNewActivity.class);
////                        if (!a.contains("http")) {
////                            intent.putExtra("jump_url", Constants.CONST_HOST + a);
////                        }else {
////                            intent.putExtra("jump_url", a);
////                        }
//                        startActivity(intent);
//
//                        break;
//                }
                //调用super则会走通知展示流程，不调用super则不展示通知
                super.dealWithNotificationMessage(context, msg);
            }

            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                LogUtil.e("---message2--"+uMessage.custom);
                String after_open = uMessage.after_open;
  //              switch (after_open){
//                    case "go_app":
//                    case "go_url":
    //                case "go_activity":
      //              case "go_custom":
//                        Intent intent = new Intent(mcontext, WebviewtoNewActivity.class);
////                        if (!a.contains("http")) {
////                            intent.putExtra("jump_url", Constants.CONST_HOST + a);
////                        }else {
////                            intent.putExtra("jump_url", a);
////                        }
//                        startActivity(intent);

              //          break;
              //  }
                return super.getNotification(context, uMessage);
            }


        };
        mPushAgent.setMessageHandler(messageHandler);




        //这个是科大讯飞语音的 5.2
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5ad94cff");


        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("app", " 是否是腾讯的内核 " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }


        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);


        if (BuildConfig.TINKER_ENABLE) {

            // 我们可以从这里获得Tinker加载过程的信息
            tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();

            // 初始化TinkerPatch SDK, 更多配置可参照API章节中的,初始化SDK
            TinkerPatch.init(tinkerApplicationLike)
                    .reflectPatchLibrary()
                    .setPatchRollbackOnScreenOff(true)
                    .setPatchRestartOnSrceenOff(true);

            // 每隔3个小时去访问后台时候有更新,通过handler实现轮训的效果
            //这里也很操蛋，事件必须是1到24小时之间
            new FetchPatchHandler().fetchPatchWithInterval(1);
            Log.i("TAG", "tinker init");
        }
    }

    //防止类超过65535个报错
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }


    {
        PlatformConfig.setWeixin("wx0adc4d32eb7632b7", "9748a4b00d400201929541f2ff9fe54e");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("1048270472", "4d770454adc753535867fff33d3139ac", "https://api.weibo.com/oauth2/default.html");
        PlatformConfig.setQQZone("1106808645", "X1kFN0EnHBaBQ9LN");


    }
}
