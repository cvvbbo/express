package com.emiaoqian.express.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emiaoqian.express.R;
import com.emiaoqian.express.activity.WebviewtoNewActivity;
import com.emiaoqian.express.interfaces.Myinterface;
import com.emiaoqian.express.utils.ChomeClient;
import com.emiaoqian.express.utils.Constants;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.ToastUtil;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by xiong on 2018/4/1.
 *
 *  这个是鸿洋的博客 4.20
 *
 *
 */

public class WebviewDialogFragment extends DialogFragment implements View.OnClickListener
        ,ChomeClient.OpenFileChooserCallBack{





//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.sign_dialog_view,null);
//        return view;
//    }

    public static  WebviewDialogFragment newInstance(String url){

        WebviewDialogFragment fragment=new WebviewDialogFragment();
        Bundle bundle=new Bundle();
        bundle.putString("dialog_url",url);
        fragment.setArguments(bundle);

        return fragment;
    }




    public static Myinterface.DismessForNetwork dismessfornetwork;
    private TextView tv;
    private RelativeLayout close_im_rl;
    private FrameLayout webView_fl;
    private WebView webView;
    private WebSettings settings;
    private ProgressBar pg1;
    private int currentProgress;
    private boolean isAnimStart = false;
    private ImageView close_im;
    private Handler handler=new Handler();

    public static void setDismessfornetworkcallback(Myinterface.DismessForNetwork dismessfornetwork){
        WebviewDialogFragment.dismessfornetwork=dismessfornetwork;

    }


    @Override
    public void onResume() {
        //Log.e("66onResume","SigntvDialogFragment");
        super.onResume();
        int width = getActivity().getResources().getDimensionPixelOffset(R.dimen.x267);
        int height = getActivity().getResources().getDimensionPixelOffset(R.dimen.y280);
        getDialog().getWindow().setLayout(width,height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //点击外部不可取消
        getDialog().setCanceledOnTouchOutside(false);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;
        window.setAttributes(windowParams);

        // 下面这个备用，因为上面这个感觉不是同一个对象 5.28 // TODO: 2018/5/28
//        getDialog().setCanceledOnTouchOutside(false);
//        Window window = getDialog().getWindow();
//        WindowManager.LayoutParams windowParams = window.getAttributes();
//        windowParams.dimAmount = 0.5f;
//        window.setAttributes(windowParams);
//        window.setLayout(width,height);
//        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.my_translucent)));




    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_webview, null);
        webView_fl = (FrameLayout) view.findViewById(R.id.webView_fl);
        webView = new WebView(getActivity());
        close_im = (ImageView) view.findViewById(R.id.im);
        close_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        LogUtil.e("1022--"+webView.getView().getWidth());
        //ViewGroup.LayoutParams layoutParams = webView_fl.getLayoutParams();
        int height = getActivity().getResources().getDimensionPixelOffset(R.dimen.y280);
        webView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        LogUtil.e("1022--"+webView.getView().getWidth());
        webView_fl.addView(webView);
//        webView_fl.addView(webView,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT));
        pg1 = (ProgressBar) view.findViewById(R.id.progressBar1);

        settingwebview();
        //webView.loadUrl(Constants.MIAOQIAN_NEW);

        if (getArguments()!=null) {
            String dialog_url = getArguments().getString("dialog_url");
            webView.loadUrl(dialog_url);
            //webView.loadUrl("http://192.168.3.112/mobile/chain/exhomeshow/v/31/userid/223");

        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        return alertDialog;

    }




    private void settingwebview() {
        settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);//出现+,-号放大缩小,在wap网页上无效
        settings.setUseWideViewPort(true);//出现+,-号放大缩小,在wap网页上无效
        settings.setJavaScriptEnabled(true);//网页前端 html5  css js jquery  指定js可用
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSaveFormData(true);
        settings.setSavePassword(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //下面两个都是demo里面必须添加的（能使拍照弹框生效的）
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setDomStorageEnabled(true); //缓存机制相关
        //使网页端能够识别是什么客户端使用了代理
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() +" emiaoqian-1.0.2");
        String aa = webView.getSettings().getUserAgentString();
        LogUtil.e("WebviewDialogFragment", String.format("%s getUserAgentString=%s", this.getClass().getSimpleName(), webView.getSettings().getUserAgentString()));
        //webView.addJavascriptInterface(new JS(), "android");
        webView.addJavascriptInterface(this, "android");
        webView.setWebChromeClient(new ChomeClient(this) {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                currentProgress = pg1.getProgress();
                if (newProgress >= 100 && !isAnimStart) {
                    // 防止调用多次动画
                    isAnimStart = true;
                    pg1.setProgress(newProgress);
                    // 开启属性动画让进度条平滑消失
                    startDismissAnimation(pg1.getProgress());
                } else {
                    // 开启属性动画让进度条平滑递增
                    startProgressAnimation(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //获取网页上的title 5.7

            }
        });

        //让网页确保在webView中开启
        webView.setWebViewClient(new WebViewClient()
        {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                pg1.setVisibility(View.VISIBLE);
                pg1.setAlpha(1.0f);

            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                close_im.setVisibility(View.VISIBLE);
            }
        });
    }


    //进度条消失动画
    private void startDismissAnimation(final int progress) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(pg1, "alpha", 1.0f, 0.0f);
        anim.setDuration(1500);  // 动画时长
        anim.setInterpolator(new DecelerateInterpolator());     // 减速
        // 关键, 添加动画进度监听器
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                int offset = 100 - progress;
                pg1.setProgress((int) (progress + offset * fraction));
            }
        });

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束
                pg1.setProgress(0);
                pg1.setVisibility(View.GONE);
                isAnimStart = false;
            }
        });
        anim.start();
    }

    //进度条开始动画
    private void startProgressAnimation(int newProgress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(pg1, "progress", currentProgress, newProgress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //切换到竖屏

        }

    }



    @Override
    public void onPause() {
        //Log.e("66onPause","SigntvDialogFragment");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.e("66onDestroy","SigntvDialogFragment");
        super.onDestroy();
    }






    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {


    }

    @Override
    public boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }


    /****从webview上新开一个fragment***/
    @JavascriptInterface
    public void ReturnNew(String a){
        if (a!=null&&!a.equals("")) {
            //在webview的js跳转activity的时候，即使不在manifest文件中注册他即使这样写了也不会报错
            Intent intent = new Intent(getActivity(), WebviewtoNewActivity.class);
            if (!a.contains("http")) {
                intent.putExtra("jump_url", Constants.CONST_HOST + a);
            }else {
                intent.putExtra("jump_url", a);
            }
            startActivity(intent);
        }
    }
}
