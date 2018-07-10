package com.emiaoqian.express.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.emiaoqian.express.activity.SecondActivity;
import com.emiaoqian.express.adapter.MenuListviewadapter;
import com.emiaoqian.express.interfaces.Myinterface;
import com.emiaoqian.express.interfaces.Myinterface2;
import com.emiaoqian.express.utils.Constants;
import com.emiaoqian.express.utils.LogUtil;

/**
 * Created by xiong on 2018/4/11.
 * 这个是包含webview的fragment
 */


//之前看到一篇文章是阻止fragment预加载的
public class MenuItemFragment extends BaseWebFragment implements Myinterface.getWebviewtitle {

    // BottomSheetBehavior variable
    private BottomSheetBehavior bottomSheetBehavior;

    // TextView variable
    private TextView bottomSheetHeading;

    // Button variables
    private Button expandBottomSheetButton;
    private Button collapseBottomSheetButton;
    private Button hideBottomSheetButton;
    private Button showBottomSheetDialogButton;

    private boolean islogin=false;
    private ImageView add_tag;


    public static MenuItemFragment newInstance(String menuurl,boolean isshow){

        MenuItemFragment fragment=new MenuItemFragment();
        Bundle bundle=new Bundle();
        bundle.putString("menuurl",menuurl);
       // bundle.putString("menutitle",menutitle);
        bundle.putBoolean("showtitle",isshow);
        fragment.setArguments(bundle);
        return fragment;
    }



    /**
     *
     * 这个就相当于add方法里面的onhidden方法 4.22
     * @param isVisibleToUser
     *
     *
     *   开始的时候这个是在tablayout上的，为了隐藏底部的buttom sheet 4.25
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.e("10086当前状态setUserVisibleHint"+getUserVisibleHint()+"--"+getClass().getSimpleName());
        if (isVisibleToUser){
            hiddenButtonSheet.HiddenButtonSheetcallback(true);

        }

    }



    //buttonsheet的隐藏和显示
    public static Myinterface.HiddenButtonSheet hiddenButtonSheet;
    public static void sethiddenButtonSheetcallback(Myinterface.HiddenButtonSheet hiddenButtonSheet){
        MenuItemFragment.hiddenButtonSheet=hiddenButtonSheet;
    }

    public static Myinterface2.WebviewChangeHeadIm webviewChangeHeadIm;
    public static void setWebviewChangeHeadImcallback(Myinterface2.WebviewChangeHeadIm webviewChangeHeadIm){
        MenuItemFragment.webviewChangeHeadIm=webviewChangeHeadIm;

    }


    @Override
    public void initialize() {
        super.initialize();

        //获取标题
        BaseWebFragment.setgetWebviewtitlecallback(this);
        //还有webview的fragment的回退
        SecondActivity.getChildFragmentWebviewCallback(this);



        if (getArguments()!=null){

            //menu_title.setText(getArguments().getString("menutitle"));

            boolean showtitle = getArguments().getBoolean("showtitle");
            if (!showtitle){
                title_rl.setVisibility(View.GONE);
            }
            String menuurl = getArguments().getString("menuurl");
                    urlDefault= menuurl;
                     loadUrl(menuurl);
            LogUtil.e(getClass().getSimpleName()+"_123456"+"--"+menuurl);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    menu_title.setText(webviewtitle);
//                }
//            },900);

        }

        returnIm_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        //https://www.emiaoqian.com/mobile_app/personal/accountManager
        closeIm_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getClass().getSimpleName().equals("WebviewtoNewActivity")){
                    getActivity().finish();
                }else {
                    getFragmentManager().popBackStack();
                }


            }
        });


    }

    @Override
    public void onResume() {
        String menuurl = getArguments().getString("menuurl");
        LogUtil.e("--10088onResume--"+menuurl);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        String menuurl = getArguments().getString("menuurl");
        LogUtil.e("--10088onDestroyview--"+menuurl);
        super.onDestroyView();
    }

    /***
     * 如果实在viewpager中是不走这个方法的，小心有坑
     * 5.8
     *
     */
    @Override
    public void onDestroy() {
        //这个是为了改变头像
        if (webView.getUrl().equals(Constants.CONST_HOST+"/mobile_app/agent/index")||
                webView.getUrl().equals(Constants.CONST_HOST+"/mobile_app/personal/accountManager")){
            LogUtil.e("销毁的时候地址是--"+webView.getUrl());
            webviewChangeHeadIm.WebviewChangeHeadImcallback();
        }
        SecondActivity.getChildFragmentWebviewCallback(null);
        SecondActivity.getWebviewlastpager(null);
        super.onDestroy();
    }

    @Override
    public void getWebviewtitlecallback(String webviewtitle) {
        if (webviewtitle.length()>6){
            menu_title.setText(webviewtitle.substring(0,6)+"...");
        }else {
            menu_title.setText(webviewtitle);
        }
    }
}
