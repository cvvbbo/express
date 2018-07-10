package com.emiaoqian.express.fragment;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.emiaoqian.express.R;
import com.emiaoqian.express.interfaces.FragmentCallback;
import com.emiaoqian.express.interfaces.WebWiewCallback;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xiong on 2017/10/10.
 *
 *  原来来自 （注意它的缺点是初始化和数据的展示都是在oncreate中了！！！，不再是oncreateview和onactivitycreate了）
 *  https://www.cnblogs.com/laishenghao/p/5157914.html
 *
 *  1.这个之所以这样写，是为了在完整的fragment的跳转中，就是整个fragment的跳转（仿照activity之间的跳转），在回退的时候保留上次的数据。
 *  2.replace方法中，在commit的时候会走ondestoryview方法，但是不走ondestory方法。在回退的时候会重走oncreatview方法。
 *
 */

public abstract class BaseFragment extends Fragment implements FragmentCallback.ChildFragmentWebCallback{

    private static final int permissionsRequestCode=2;

    private Handler mhandler=new Handler();


    //父类的共有成员变量子类能够共享
    //然后这种写法是在谷歌市场里面！！！
    public View view;
   // private UMShareListener mShareListener;
   // private ShareAction mShareAction;
    Activity mActivity;
   // Handler handler=new Handler();
    private String sign;

   // private String getClassname=null;




    /**
     *  这个是只有在viewpager添加fragment的时候才会走的方法，普通的add。replace方法貌似不走
     *  @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //LogUtil.e("10086setUserVisibleHint---用户是否可见--"+isVisibleToUser+"--"+getClass().getSimpleName());
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onAttach(Context context) {
        //LogUtil.e("10086onAttach--"+getClass().getSimpleName());
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //LogUtil.e("10086onCreate--"+getClass().getSimpleName());
        super.onCreate(savedInstanceState);

        view=LayoutInflater.from(getActivity()).inflate(getlayout(),null);
        view.setClickable(true);


        // 这样的话初始化数据和数据的展示都在一起了。。。4.16
        mActivity = getActivity();
        //展示数据
        initialize();

        if (Build.VERSION.SDK_INT>=21){
            Window window = getActivity().getWindow();
            //取消状态栏透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(R.color.white));
            //设置系统状态栏处于可见状态
            //把状态栏的字体改为黑色 https://blog.csdn.net/zhangyiminsunshine/article/details/68064926
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //让view不根据系统窗口来调整自己的布局
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        //LogUtil.e("--onHiddenChanged10086--"+hidden+"--当前的类-"+getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //LogUtil.e("oncreateview10086--"+getClass().getSimpleName());

        return view;
    }

    /**
     *
     * 这个方法在弹栈的时候还会会走
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /***换中加载的形式 4.16**/

        //mActivity = getActivity();
        //Mainactivity.getChildFragmentWebviewCallback(this);
        //initialize();

        //LogUtil.e("10086onActivityCreated---"+getClass().getSimpleName());

    }


    @Override
    public void onStart() {
        //LogUtil.e("10086onStart--"+getClass().getSimpleName());
        super.onStart();
    }

    @Override
    public void onPause() {
        //LogUtil.e("10086onPause--"+getClass().getSimpleName());
        super.onPause();
    }

    public abstract int getlayout();

    public  abstract void initialize();

    @Override
    public void onDestroyView() {
        ViewGroup mGroup = (ViewGroup) view.getParent();
        if (mGroup != null) {
            /**
             * 但是removeAllViewsInLayout() 需要先测量当前的布局, 一旦调用该方法,只能移除已经自身布局中已计算好的所包含的子view
             *
             * https://blog.csdn.net/stzy00/article/details/43966149
             *
             * 下面这里已经是强制转为了父布局了，所以只是移除了父布局的宽高 4.16
             *
             */
            mGroup.removeAllViewsInLayout();
        }

        LogUtil.e(getClass().getSimpleName() + "--10086-onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtil.e(getClass().getSimpleName()+"--10086-onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtil.e(getClass().getSimpleName()+"--10086-onDetach");
        super.onDetach();
    }


    public static WebWiewCallback webWiewCallback;
    public static void getWebviewlastpager(WebWiewCallback webWiewCallback) {
        BaseFragment.webWiewCallback = webWiewCallback;
    }


    //这个是执行webview不再回退之后，执行fragment的回退 2.3
    @Override
    public void childfragmentwebcancallback() {
        getFragmentManager().popBackStack();

    }

    //这个是判断fragment里面嵌套的webview能不能回退 2.3
    //简单的来说就是 先监听webview里面的回退，这个webview是腾讯的内核，它的回退方式和不同安卓的webview不一样 2.3
    //然后把腾讯的webview全部退完之后再去退出fragment的回退 2.3
    @Override
    public boolean childfragmentwebcallback() {

        if (webWiewCallback!=null&&this.getClass().getSimpleName().equals(webWiewCallback.getClass().getSimpleName())) {
            LogUtil.e("--当前webview的接口的谁的--"+webWiewCallback.getClass().getSimpleName());
            if (getFragmentManager()!=null&&getFragmentManager().getBackStackEntryCount() != 0 && !webWiewCallback.CanCallback()) {
                //getFragmentManager().popBackStack();
                return true;
            } else {
                return false;
            }
        }else {

            if (getFragmentManager()!=null&&getFragmentManager().getBackStackEntryCount() != 0) {
                LogUtil.e(getFragmentManager().getBackStackEntryCount()+"233接口的数量是");
                //getFragmentManager().popBackStack();
                return true;
            } else {
                return false;
            }

        }
    }

    public void initPermission(String[] permissions, String title, String content) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {//for循环把需要授权的权限都添加进来
                if (ContextCompat.checkSelfPermission(getActivity(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {  //未授权就进行授权
                    permissionList.add(permissions[i]);
                }
            }
            //如果permissionList是空的，说明没有权限需要授权,什么都不做，该干嘛干嘛，否则就发起授权请求
            if (!permissionList.isEmpty()) {
                showDialogTipUserRequestPermission(permissionList, title, content);
            }
        }
    }

    private void showDialogTipUserRequestPermission(final List<String> permissionList, String title, String content) {
        new android.support.v7.app.AlertDialog.Builder(getActivity())
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

            ActivityCompat.requestPermissions(getActivity(), permissions, permissionsRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionsRequestCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults.length > 0) {//安全写法，如果小于0，肯定会出错了
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        switch (grantResult){
                            case PackageManager.PERMISSION_GRANTED://同意授权0
                                break;
                            case PackageManager.PERMISSION_DENIED://拒绝授权-1
//                                Utils.ShowToast(context,permissions[i]+"权限获取失败");
                                ToastUtil.showToastCenter(permissions[i]+"权限获取失败");
                                //getActivity().finish();
                                break;
                        }
                    }
                }
            }
        }
    }


}
