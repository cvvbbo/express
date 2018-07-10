package com.emiaoqian.express.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.emiaoqian.express.R;
import com.emiaoqian.express.fragment.SigntvDialogFragment;
import com.emiaoqian.express.fragment.WebviewDialogFragment;
import com.emiaoqian.express.utils.Constants;
import com.emiaoqian.express.utils.EncodeBuilder;
import com.emiaoqian.express.utils.GaoDeUtils;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.ToastUtil;
import com.emiaoqian.express.utils.httphelper;

/**
 * Created by xiong on 2018/4/13.
 */

public class TextSizeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_view);
        Button bt= (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SigntvDialogFragment  signtvDialogFragment = new SigntvDialogFragment();
//                signtvDialogFragment.show(getSupportFragmentManager(), "signtvDialogFragment");

                WebviewDialogFragment fragment=new WebviewDialogFragment();
                fragment.show(getSupportFragmentManager(),"WebviewDialogFragment");
            }
        });


        //这个是根据手机屏幕分辨率转的（转之前先看看手机的分辨率，好适配）
        LogUtil.e("--转化的结果是--"+px2sp(24));
        String address = GaoDeUtils.create().getAddress();
        ToastUtil.showToastCenter(address);
        aa();
    }


    public int px2sp(float pxValue) {
        final float fontScale =getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    //
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public void aa(){
        final long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        final String s = EncodeBuilder.javaToexpressimageinfor("express_work",str);
        final String sign = EncodeBuilder.newString2(s);
        LogUtil.e("--地址是--"+Constants.GET_USER_CARDIMAGE);
        httphelper.create().GetExpressImageinfo(Constants.GET_USER_CARDIMAGE, "express_work", sign, str, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                LogUtil.e(s);
            }

            @Override
            public void fail(Exception e) {

            }
        });
    }

}
