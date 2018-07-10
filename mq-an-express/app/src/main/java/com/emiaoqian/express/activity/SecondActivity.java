package com.emiaoqian.express.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;

import com.emiaoqian.express.R;
import com.emiaoqian.express.fragment.ExprssMainFragment;
import com.emiaoqian.express.fragment.LoginFragment;
import com.emiaoqian.express.fragment.SignMakePhotoAndVoiceFragment;
import com.emiaoqian.express.fragment.SignMessageFragemnt;
import com.emiaoqian.express.fragment.WatchSignFragment;
import com.emiaoqian.express.interfaces.FragmentCallback;
import com.emiaoqian.express.interfaces.Myinterface;
import com.emiaoqian.express.interfaces.Myinterface2;
import com.emiaoqian.express.interfaces.WebWiewCallback;
import com.emiaoqian.express.service.Myservice;
import com.emiaoqian.express.utils.GaoDeUtils;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.ToastUtil;
import com.emiaoqian.express.utils.UpDataUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.UMShareAPI;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiong on 2018/4/12.
 */

public class SecondActivity  extends AppCompatActivity {


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_COARSE_LOCATION };

    String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE};//需要的权限
    String title = "定位权限不可用";
    String content = "为了保证您的地图正常显示,需要获取定位权限\n否则，您将无法正常使用地图功能";
    private static final int permissionsRequestCode=2;


    private boolean isexit = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isexit = false;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continer_view);

        //verifyStoragePermissions(this);

        initPermission(permission,title,content);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            UpDataUtil upDataUtil=new UpDataUtil(this);
            upDataUtil.checkVersion();
        }



        //startService(new Intent(this, Myservice.class));

        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);

        String address = GaoDeUtils.create().getAddress();
        String latitude = GaoDeUtils.create().getLatitude();
        String longitude = GaoDeUtils.create().getLongitude();

        LogUtil.e("位置是--"+address+"--经度--"+latitude+"--纬度--"+longitude);

        LogUtil.e("分辨率是"+"display is" + metrics.widthPixels + "*" + metrics.heightPixels);
        getSupportFragmentManager().beginTransaction()
                // ExprssMainFragment 这个是进入页面
                .replace(R.id.fy,new ExprssMainFragment())
                .commit();
    }

    public static void verifyStoragePermissions(AppCompatActivity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


    /**
     * 初始化用户权限
     * @param permissions 需要的权限
     * @param title 我们自己对话框的标题
     * @param content 我们自己对话框的内容
     */
    public void initPermission(String[] permissions, String title, String content) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {//for循环把需要授权的权限都添加进来
                if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {  //未授权就进行授权
                    permissionList.add(permissions[i]);
                }
            }
            //如果permissionList是空的，说明没有权限需要授权,什么都不做，该干嘛干嘛，否则就发起授权请求
            if (!permissionList.isEmpty()) {
                showDialogTipUserRequestPermission(permissionList, title, content);
            }else {
                //自己的操作 // // TODO: 2018/5/30
                UpDataUtil upDataUtil=new UpDataUtil(this);
                upDataUtil.checkVersion();
            }
        }
    }

    private void showDialogTipUserRequestPermission(final List<String> permissionList, String title, String content) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission(permissionList);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission(permissionList);
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission(List<String> permissionList) {
        if (!permissionList.isEmpty()) {//不为空就进行授权申请
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);

            ActivityCompat.requestPermissions(this, permissions, permissionsRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UpDataUtil upDataUtil=new UpDataUtil(this);
        if (requestCode == permissionsRequestCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults.length > 0) {//安全写法，如果小于0，肯定会出错了
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        switch (grantResult){
                            case PackageManager.PERMISSION_GRANTED://同意授权0
                                //ToastUtil.showToast("112");
                                if(permissions[i].equals("android.permission.WRITE_EXTERNAL_STORAGE")){
                                    upDataUtil.checkVersion();
                                }
                                break;
                            case PackageManager.PERMISSION_DENIED://拒绝授权-1
//                                Utils.ShowToast(context,permissions[i]+"权限获取失败");
                                ToastUtil.showToastCenter(permissions[i]+"相关权限获取失败,->请去设置中打开相关权限");

                                //upDataUtil.checkVersion();
                                //finish();
                                break;
                        }
                    }
                }
            }
        }
    }


    public static WebWiewCallback webWiewCallback;

    public static void getWebviewlastpager(WebWiewCallback webWiewCallback) {
        SecondActivity.webWiewCallback = webWiewCallback;
    }

    public static FragmentCallback.ChildFragmentWebCallback childFragmentWebCallback;
    public static void getChildFragmentWebviewCallback(FragmentCallback.ChildFragmentWebCallback childFragmentWebCallback){
       SecondActivity.childFragmentWebCallback=childFragmentWebCallback;

    }

    public static Myinterface2.GetDrawlayoutstate getDrawlayoutstate;
    public static void setgetDrawlayoutstatecallback(Myinterface2.GetDrawlayoutstate getDrawlayoutstate){
        SecondActivity.getDrawlayoutstate=getDrawlayoutstate;

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

        if (childFragmentWebCallback!=null&&childFragmentWebCallback.childfragmentwebcallback()){
            //childFragmentWebCallback.childfragmentwebcallback();
            //注意下面这两个方法是有区别的，一个是childfragmentwebcancallback一个是childfragmentwebcallback 2.3
            childFragmentWebCallback.childfragmentwebcancallback();

            LogUtil.e("--233当前的接口是--"+childFragmentWebCallback.getClass().getSimpleName());
        }

        else if (webWiewCallback != null && webWiewCallback.CanCallback()) {
            //else if (webWiewCallback.CanCallback()) {

            //super.onBackPressed();
            LogUtil.e("--666当前的接口是--"+webWiewCallback.getClass().getSimpleName());
            webWiewCallback.BackLastCallback();
        }

        else if (getDrawlayoutstate!=null){
            getDrawlayoutstate.GetDrawlayoutstatecallback();
        }


        else if (isexit) {
            super.onBackPressed();
        } else {
            isexit = true;
            ToastUtil.showToast("再按一次退出");
            handler.sendEmptyMessageDelayed(0, 2000);
            //super.onBackPressed();
            LogUtil.e("栈里面的数量--" + getSupportFragmentManager().getBackStackEntryCount());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //友盟分享回调 5.4
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
