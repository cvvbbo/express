package com.emiaoqian.express.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emiaoqian.express.R;
import com.emiaoqian.express.activity.SecondActivity;
import com.emiaoqian.express.application.MyApplication;
import com.emiaoqian.express.bean.CodeDatabean;
import com.emiaoqian.express.bean.GetCodebean;
import com.emiaoqian.express.bean.LoginBean;
import com.emiaoqian.express.interfaces.Myinterface2;
import com.emiaoqian.express.utils.Constants;
import com.emiaoqian.express.utils.EncodeBuilder;
import com.emiaoqian.express.utils.EncodeUtils;
import com.emiaoqian.express.utils.GsonUtil;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.Netutils;
import com.emiaoqian.express.utils.ToastUtil;
import com.emiaoqian.express.utils.httphelper;
import com.emiaoqian.express.utils.sharepreferenceUtils;
import com.emiaoqian.express.views.XiongProgressDialog;
import com.umeng.message.PushAgent;

import java.util.HashMap;

/**
 * Created by xiong on 2018/4/22.
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener{

    private ImageView im1;
    private EditText phonenum;
    private TextView send_code;
    private View v1;
    private ImageView im2;
    private EditText passwordnum;
    private View v2;
    private TextView tv;
    private TextView tvnote;
    private TextView nexttv;
    private ImageView im3;
    private LinearLayout changeloginstate;

    String login_type=null;

    private static final int UN_CHECK = 500;
    private static final int CAN_CHECK = 600;
    private int time = 60;


    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UN_CHECK:
                    time--;
                    send_code.setText("请稍后" + time + "s");
                    send_code.setTextColor(Color.GRAY);
                    send_code.setBackgroundResource(R.drawable.shape_send_code_press);
                    break;
                case CAN_CHECK:
                    //下面第一个判断防止崩溃 5.13
                    if (LoginFragment.this.isAdded()) {
                        time = 60;
                        send_code.setTextColor(getResources().getColor(R.color.tab_layout_text_color));
                        send_code.setBackgroundResource(R.drawable.shape_send_code);
                        send_code.setText("重新获取");
//                    //把按钮状态设置为按下
//                    send_code.setPressed(false);
//                    //最开始这个是不可点击状态
                        send_code.setEnabled(true);
                    }
                    break;
            }
        }
    };
    private ImageView returnIm;
    private TextView change_login_state_tv;
    private RelativeLayout returnIm_rl;
    private TextView agree_note;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止输入框上移底部布局 4.25
        getActivity().getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SecondActivity.getChildFragmentWebviewCallback(this);
    }

    @Override
    public int getlayout() {
        return R.layout.password_word_view;
    }

    @Override
    public void initialize() {

        agree_note = (TextView) view.findViewById(R.id.agree_note);
        agree_note.setOnClickListener(this);
        returnIm_rl = (RelativeLayout) view.findViewById(R.id.returnIm_rl);
        change_login_state_tv = (TextView) view.findViewById(R.id.change_login_state_tv);
        //im1 = (ImageView) findViewById(R.id.im1);
        phonenum = (EditText) view.findViewById(R.id.phone_num);
        send_code = (TextView) view.findViewById(R.id.send_code);
        // v1 = (View) findViewById(R.id.v1);
        //这个是输入密码获取输入验证码的图标 4.22
        im2 = (ImageView) view.findViewById(R.id.im2);
        //输入验证码或者输入密码 4.22
        passwordnum = (EditText)view.findViewById(R.id.password_num);
        passwordnum.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //v2 = (View) findViewById(R.id.v2);
        //这个是提示，输验证码才有
        tv = (TextView)view.findViewById(R.id.tv);
        //点击获取语音验证码
        tvnote = (TextView) view.findViewById(R.id.tv_note);
        // 下一步
        nexttv = (TextView) view.findViewById(R.id.next_tv);

        //最下面验证码和密码登录的切换
        im3 = (ImageView) view.findViewById(R.id.im3);
        //验证码和密码登录的切换
        changeloginstate = (LinearLayout) view.findViewById(R.id.change_login_state);

        returnIm = (ImageView) view.findViewById(R.id.returnIm);
        returnIm.setOnClickListener(this);

        changeloginstate.setOnClickListener(this);
        nexttv.setOnClickListener(this);
        tvnote.setOnClickListener(this);
        send_code.setOnClickListener(this);
        returnIm_rl.setOnClickListener(this);

        SecondActivity.getChildFragmentWebviewCallback(LoginFragment.this);


    }

    //获取已取件和代取件接口
    public static Myinterface2.getuserinfor getuserinfor;
    public static void setgetuserinforcallback(Myinterface2.getuserinfor getuserinfor){
        LoginFragment.getuserinfor=getuserinfor;

    }

    //获取用户头像 5.17
    public static Myinterface2.Getheadimg getheadimg;
    public static void setGetheadimgcallback(Myinterface2.Getheadimg getheadimg){
        LoginFragment.getheadimg=getheadimg;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点这个改图标，改提示
            case R.id.change_login_state:
                if (change_login_state_tv.getText().equals("验证码登录")){
                    passwordnum.setText("");
                    change_login_state_tv.setText("密码登录");
                    im3.setImageResource(R.drawable.loginpassim_p);
                    im2.setImageResource(R.drawable.loginsignim);
                    passwordnum.setHint("请输入验证码");
                    passwordnum.setInputType(InputType.TYPE_NULL|InputType.TYPE_CLASS_TEXT);
                    send_code.setVisibility(View.VISIBLE);
                    //暂时是没有这个接口的 4.28
//                    tv.setVisibility(View.VISIBLE);
//                    tvnote.setVisibility(View.VISIBLE);
                }else if (change_login_state_tv.getText().equals("密码登录")){
                    passwordnum.setText("");
                    passwordnum.setHint("请输入密码");
                    passwordnum.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    send_code.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                    tvnote.setVisibility(View.GONE);
                    change_login_state_tv.setText("验证码登录");
                    im3.setImageResource(R.drawable.loginsignim_p);
                    im2.setImageResource(R.drawable.loginpassim);
                }
                break;

            //点这个登录
            case R.id.next_tv:

                if (change_login_state_tv.getText().equals("验证码登录")){
                    login_type="password";
                }else if (change_login_state_tv.getText().equals("密码登录")){
                    login_type="code";
                }

                //首先是判断网络 5.15
                boolean networkAvalible = Netutils.isNetworkAvalible(getActivity());
                if (!networkAvalible){
                    ToastUtil.showToastCenter("当前无网络");
                    Netutils.checkNetwork(getActivity());
                    return;
                }

                final String phone_num = phonenum.getText().toString().trim();
                String passwor_num = passwordnum.getText().toString().trim();

                long l = System.currentTimeMillis();
                String str = String.valueOf(l);

               // String phone = phonenum.getText().toString().trim();
                //正则的匹配
                String num="[1][234567890]\\d{9}";//注意正则表达式这里是

                //首先输入了11位就能正常显示，然后再正则()

                if (TextUtils.isEmpty(phone_num)) {
                    ToastUtil.showToastCenter("号码不能为空");
                    return;
                }

                if (phone_num.matches(num)==false){
                    ToastUtil.showToastCenter("输入号码不正确");
                    return;

                }

                HashMap<String,String> datas=new HashMap<>();
                PushAgent mPushAgent = PushAgent.getInstance(getActivity());
                String device_no = mPushAgent.getRegistrationId();
                datas.put("ex_device_no","1&&"+device_no);
                datas.put("login_type",login_type);
                datas.put("mobile",phone_num);
                datas.put("password",passwor_num);
                datas.put("appv","v1");
                datas.put("timestamp",str);
                String s1 = EncodeBuilder.javaToJSONNewPager1(datas);
                String sign1 = EncodeBuilder.newString2(s1);
                datas.put("sign",sign1);
                final Dialog mDialog = XiongProgressDialog.createLoadingDialog(getActivity(), "登陆中");
                mDialog.show();//显示
//                httphelper.create().NewdataHomePage2(Constants.LOGIN, datas, new httphelper.httpcallback() {
                httphelper.create().NewdataHomePage2(Constants.LOGIN_V2, datas, new httphelper.httpcallback() {
                    @Override
                    public void success(String s) {
                        LogUtil.e(s);
                        final LoginBean loginBean = GsonUtil.parseJsonToBean(s, LoginBean.class);
                        if (loginBean.code.equals("100000")){
                            LogUtil.e("登录成功");

                            int user_id = loginBean.data.user_id;
                            String mobile = loginBean.data.mobile;
                            //获取用户头像
                            getheadimg.Getheadimgcallback(loginBean.data.headimg,loginBean.data.level_img);

                            //记录用户电话 4.26
                            sharepreferenceUtils.saveStringdata(MyApplication.mcontext,"phone_num",mobile);
                            sharepreferenceUtils.saveStringdata(MyApplication.mcontext,"user_id",String.valueOf(user_id));
                            if (loginBean.data.realname!=null) {
                                sharepreferenceUtils.saveStringdata(MyApplication.mcontext, "realname", loginBean.data.realname);
                            }
                            sharepreferenceUtils.saveBooleandata(MyApplication.mcontext,"isfirst",true);

                            //只是在用户登录成功的时候才进行这个个人信息的请求 4.24
                            GetUserInfor();
                            mDialog.dismiss();

                        }
                        else if (loginBean.code.equals("200006")){
                            ToastUtil.showToast("请先用短信注册并登陆");
                            mDialog.dismiss();
                        }
                        else if (loginBean.code.equals("200008")){
                            ToastUtil.showToast("用户密码错误");
                            mDialog.dismiss();
                        }
                        else {
                            ToastUtil.showToast("登陆失败");
                            mDialog.dismiss();
                        }

                    }

                    @Override
                    public void fail(Exception e) {
                        ToastUtil.showToastCenter("网络异常");
                        mDialog.dismiss();

                    }
                });


                break;

            //点这个获取语音验证码
            case R.id.tv_note:

                break;


            case R.id.send_code:
                LogUtil.e("--点击了么-");
                Getsignnum();

                break;

            case R.id.returnIm_rl:
                getFragmentManager().popBackStack();
                break;

            case R.id.agree_note:

                MenuItemFragment fragment1 = MenuItemFragment.newInstance(Constants.AGREE_LAW,true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fy,fragment1)
                        .addToBackStack(null)
                        .commit();

                break;

        }
    }

    //这个接口和获取标签的接口相同 4.25
    private void GetUserInfor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long l = System.currentTimeMillis();
                String str = String.valueOf(l);
                String user_id1 = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                final String s = EncodeBuilder.javaTouserinfor(user_id1,str);
                final String sign = EncodeBuilder.newString2(s);
                httphelper.create().GetPersonData(Constants.USER_INFOR, user_id1, sign, str, new httphelper.httpcallback() {
                    @Override
                    public void success(String s) {
                        LogUtil.e("--个人信息--"+s);
                        LoginBean loginBean1 = GsonUtil.parseJsonToBean(s, LoginBean.class);
                        CodeDatabean data = loginBean1.data;
                        int nonTakeCount = data.nonTakeCount;
                        int receivedCount = data.receivedCount;
                        //mDialog.dismiss();

                        String checkurl="";
                        String url = data.url;
                        if (url!=null){
                            if (!url.equals("")){
                                checkurl=url;
                            }
                        }

                        //已取件，待取件，用户消息
                        getuserinfor.getuserinforcallback(String.valueOf(nonTakeCount)
                                ,String.valueOf(receivedCount),String.valueOf(data.pushMess),checkurl);


                        //如果是验证码登录，还需要把这个消息移除了 4.24
                        handler.removeCallbacksAndMessages(null);

                        getFragmentManager().popBackStack();





                    }

                    @Override
                    public void fail(Exception e) {
                        LogUtil.e("--个人信息失败--"+e);

                    }
                });

            }
        }).start();
    }


    //获取验证码
    public void Getsignnum() {

        //判断为不为空，手机号是不是正确的。。以后再考虑

        //需求是当手机号码没有填满的时候，那个发短信的按钮是按不出来的

        String phone = phonenum.getText().toString().trim();
        //正则的匹配
        String num="[1][234567890]\\d{9}";//注意正则表达式这里是

        //首先输入了11位就能正常显示，然后再正则()

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToastCenter("号码不能为空");
            return;

        } else if (phone.matches(num)==false){
            ToastUtil.showToastCenter("输入号码不正确");
            return;

        }else {
            //首先显示验证码按钮
            long l = System.currentTimeMillis();
            String str = String.valueOf(l);
            String s = EncodeBuilder.javaToSendCode(str,phone);
            String sign = EncodeBuilder.newString2(s);
            httphelper.create().sendcode(Constants.SEND_CODE, phone,sign, str, new httphelper.httpcallback() {
                @Override
                public void success(String s) {

                    LogUtil.e("--发送短信--"+s);

                    //s="{\"code\":\"100000\",\"msg\":\"操作成功\",\"data\":[]}";



                    GetCodebean userbean = GsonUtil.parseJsonToBean(s, GetCodebean.class);
                    if (!userbean.code.equals("100000")){
                        ToastUtil.showToastCenter("短信验证码发送失败");
                        return;
                    }
                    if (userbean.code.equals("100000")) {
                        //设置倒计时
                        send_code.setBackgroundResource(R.drawable.shape_send_code_press);
                        send_code.setEnabled(false);
                        new Thread() {
                            @Override
                            public void run() {
                                while (time > 1) {
                                    handler.sendEmptyMessage(UN_CHECK);
                                    SystemClock.sleep(1000);
                                }
                                handler.sendEmptyMessage(CAN_CHECK);

                            }
                        }.start();

                    }

                }

                @Override
                public void fail(Exception e) {

                    LogUtil.e("--233404--"+e.toString());


                }
            });
        }
    }

    @Override
    public void onDestroy() {
        handler.sendEmptyMessage(UN_CHECK);
        handler.removeMessages(CAN_CHECK);
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();


    }
}
