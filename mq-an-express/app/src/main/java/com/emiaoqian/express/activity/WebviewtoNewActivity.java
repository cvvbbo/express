package com.emiaoqian.express.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.emiaoqian.express.R;
import com.emiaoqian.express.fragment.MenuItemFragment;
import com.emiaoqian.express.interfaces.WebWiewCallback;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * Created by xiong on 2018/5/7.
 * 这个是包含webviewfragment的activity，为了是跳转在childfragment中的fragment
 *
 */

//这个是解决在childfragment中跳转使用getfragmentmanger 5.15
public class WebviewtoNewActivity  extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_address_view);
        //友盟推送必须在每一个页面添加下面这一行！！5.16
        PushAgent.getInstance(this).onAppStart();
        String jump_url = getIntent().getStringExtra("jump_url");
        //第二个是通知的地址
        //String url = getIntent().getStringExtra("url");
        if (jump_url!=null) {
            if (!jump_url.equals("")) {
                MenuItemFragment fragment = MenuItemFragment.newInstance(jump_url, true);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.new_fy, fragment)
                        .commit();
            }
        }
//        if (url!=null){
//            MenuItemFragment fragment = MenuItemFragment.newInstance(url, true);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.new_fy, fragment)
//                    .commit();
//        }

    }

    //下面这个只是判断url的回退，然后再推出这个activity
    public static WebWiewCallback webWiewCallback;
    public static void getWebviewlastpager(WebWiewCallback webWiewCallback) {
        WebviewtoNewActivity.webWiewCallback = webWiewCallback;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        if (webWiewCallback != null && webWiewCallback.CanCallback()) {
            //else if (webWiewCallback.CanCallback()) {

            //super.onBackPressed();
            LogUtil.e("--666当前的接口是--"+webWiewCallback.getClass().getSimpleName());
            webWiewCallback.BackLastCallback();
        }

        else {
            super.onBackPressed();

        }
    }
}
