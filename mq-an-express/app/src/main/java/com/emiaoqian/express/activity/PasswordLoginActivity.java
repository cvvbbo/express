package com.emiaoqian.express.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.emiaoqian.express.R;

/**
 * Created by xiong on 2018/4/21.
 */

public class PasswordLoginActivity  extends AppCompatActivity implements View.OnClickListener{


    private ImageView im1;
    private EditText phonenum;
    private TextView sendcode;
    private View v1;
    private ImageView im2;
    private EditText passwordnum;
    private View v2;
    private TextView tv;
    private TextView tvnote;
    private TextView nexttv;
    private ImageView im3;
    private TextView changeloginstate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         *
         * 下面这个并不能正真隐藏底部的导航栏,直接以带有底部导航栏的手机为底部即可 4.22
         *
         */
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.password_word_view);

        //im1 = (ImageView) findViewById(R.id.im1);
        //phonenum = (EditText) findViewById(R.id.phone_num);
        sendcode = (TextView) findViewById(R.id.send_code);
       // v1 = (View) findViewById(R.id.v1);

        //这个是输入密码获取输入验证码的图标 4.22
        im2 = (ImageView) findViewById(R.id.im2);
        //输入验证码或者输入密码 4.22
        passwordnum = (EditText) findViewById(R.id.password_num);
        //v2 = (View) findViewById(R.id.v2);

        //这个是提示，输验证码才有
        tv = (TextView) findViewById(R.id.tv);
        //点击获取语音验证码
        tvnote = (TextView) findViewById(R.id.tv_note);

        // 下一步
        nexttv = (TextView) findViewById(R.id.next_tv);

        //最下面验证码和密码登录的切换
        im3 = (ImageView) findViewById(R.id.im3);
        //验证码和密码登录的切换
        changeloginstate = (TextView) findViewById(R.id.change_login_state);
        changeloginstate.setOnClickListener(this);
        nexttv.setOnClickListener(this);
        tvnote.setOnClickListener(this);
        sendcode.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点这个改图标，改提示
            case R.id.change_login_state:
                if (changeloginstate.getText().equals("验证码登录")){
                    changeloginstate.setText("密码登录");
                    im3.setImageResource(R.drawable.loginpassim);
                    im2.setImageResource(R.drawable.loginsignim);
                    passwordnum.setHint("请输入验证码");
                    sendcode.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    tvnote.setVisibility(View.VISIBLE);
                }else if (changeloginstate.getText().equals("密码登录")){
                    passwordnum.setHint("请输入密码");
                    sendcode.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                    tvnote.setVisibility(View.GONE);
                    changeloginstate.setText("验证码登录");
                    im3.setImageResource(R.drawable.loginsignim);
                    im2.setImageResource(R.drawable.loginpassim);
                }
                break;

            //点这个登录
            case R.id.next_tv:
                break;

            //点这个获取语音验证码
            case R.id.tv_note:

                break;

        }

    }
}
