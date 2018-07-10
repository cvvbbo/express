package com.emiaoqian.express.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.emiaoqian.express.R;
import com.emiaoqian.express.activity.SecondActivity;
import com.emiaoqian.express.adapter.MainViewPagerForFragment;
import com.emiaoqian.express.adapter.MenuListviewadapter;
import com.emiaoqian.express.application.MyApplication;
import com.emiaoqian.express.bean.CodeDatabean;
import com.emiaoqian.express.bean.GetCodebean;
import com.emiaoqian.express.bean.LoginBean;
import com.emiaoqian.express.interfaces.CustomShareListener;
import com.emiaoqian.express.interfaces.Myinterface;
import com.emiaoqian.express.interfaces.Myinterface2;
import com.emiaoqian.express.utils.Constants;
import com.emiaoqian.express.utils.EncodeBuilder;
import com.emiaoqian.express.utils.EncodeUtils;
import com.emiaoqian.express.utils.GaoDeUtils;
import com.emiaoqian.express.utils.GsonUtil;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.ToastUtil;
import com.emiaoqian.express.utils.httphelper;
import com.emiaoqian.express.utils.sharepreferenceUtils;
import com.emiaoqian.express.views.CircleImageView;
import com.emiaoqian.express.views.XiongViewPager;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.HttpHelper;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by xiong on 2018/4/12.
 */

public class ExprssMainFragment extends BaseFragment implements View.OnClickListener,
        Myinterface.HiddenButtonSheet,InputExpressNumDialogFragment.InputnumListener,
        Myinterface2.getuserinfor,Myinterface2.GetDrawlayoutstate,Myinterface2.Getheadimg,
        Myinterface2.WebviewChangeHeadIm{

    private static final int QRCode_Action = 666;
    ImageView returnIm;
    TextView title;
    Toolbar homeTb1;
    DrawerLayout drawlayout;
    TabLayout tablayout;
    XiongViewPager vp;
    private ActionBar supportActionBar;
    private String[] titleArray;



    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView scan_qr;
    private TextView wait_num_tv;
    private TextView sign_num_tv;
    private TextView starttv;
    private ListView lv_menu;

    private int[] icon_im={R.drawable.fast_send,R.drawable.sign_im,
            R.drawable.wallet,
            R.drawable.server_im,
            R.drawable.set_im};

    private String[] menuString={"快件","存证","钱包","客服","设置"};
    private MenuListviewadapter menuListviewadapter;
    private CoordinatorLayout coordinatorLayout;
    private TextView input_express_num;

    private String getexpressnum="";
    private TextView icon_name;

    private String[] menuurls={Constants.SIGN,Constants.EXPRESS,Constants.WALLET,Constants.SERVERPEOPLE
    ,Constants.URLSETTING};
    private RelativeLayout package_rl;
    private RelativeLayout job_rl;
    private RelativeLayout present_im;
    private CircleImageView circleImageView;
    private RelativeLayout root_wait_rl;
    private RelativeLayout root_sign_rl;
    private ImageView imagebutton;
    private TextView tv_name;

    //动态权限 4.27
    String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};//需要的权限
    String dialogtitle = "拍照权限不可用";
    String content = "为了保证扫码正常使用,需要开启照相机权限\n否则，您将无法正常使用扫描功能";
    private ImageView idcard_im1;
    private ImageView idcard_im2;
    private ImageView idcard_im3;
    private ArrayList<ImageView> imageViews=new ArrayList<>();
    private ArrayList<String> imageurls=new ArrayList<>();

    private ArrayList<String>  imageurldetails=new ArrayList<>();

    private boolean showADdialog=true;


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what==1){
                //有userid的时候才循环,没有的时候不循环，因为没有的时候没有值
                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
                if (user_id.matches("[0-9]+")) {
                    GetUserInfor();
                }else {

                    if(getActivity() == null)
                        return;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sign_num_tv.setText("0");
                            wait_num_tv.setText("0");
                            LogUtil.e("哈哈哈");
                        }
                    });
                }
            }

        }
    };
    private RelativeLayout head_rool_rl;
    private ImageView levelIm;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public int getlayout() {
        return R.layout.main_fragment1;

        //return R.layout.menu_list;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.e(getClass().getSimpleName()+"---"+"onCreateView"+"----"+"savedInstanceState"+"--"+(savedInstanceState==null));
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.e(getClass().getSimpleName()+"---"+"onCreateView"+"----"+"savedInstanceState"+"--"+(savedInstanceState==null));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initialize() {
        String address = GaoDeUtils.create().getAddress();
        String latitude = GaoDeUtils.create().getLatitude();
        String longitude = GaoDeUtils.create().getLongitude();

        LogUtil.e("位置是--"+address+"--经度--"+latitude+"--纬度--"+longitude);

        head_rool_rl = (RelativeLayout) view.findViewById(R.id.head_rool);
        head_rool_rl.setOnClickListener(this);
        idcard_im1 = (ImageView) view.findViewById(R.id.idcard_im1);
        idcard_im2 = (ImageView) view.findViewById(R.id.idcard_im2);
        idcard_im3 = (ImageView) view.findViewById(R.id.idcard_im3);
        idcard_im1.setScaleType(ImageView.ScaleType.FIT_XY);
        idcard_im2.setScaleType(ImageView.ScaleType.FIT_XY);
        idcard_im3.setScaleType(ImageView.ScaleType.FIT_XY);
        idcard_im1.setOnClickListener(this);
        idcard_im2.setOnClickListener(this);
        idcard_im3.setOnClickListener(this);

        imageViews.add(idcard_im1);
        imageViews.add(idcard_im2);
        imageViews.add(idcard_im3);

        wait_num_tv = (TextView) view.findViewById(R.id.wait_num_tv);
        sign_num_tv = (TextView) view.findViewById(R.id.sign_num_tv);
        titleArray = getResources().getStringArray(R.array.tab_title);
        input_express_num = (TextView) view.findViewById(R.id.input_express_num);
        input_express_num.setOnClickListener(this);
        returnIm = (ImageView) view.findViewById(R.id.return_im);
        title = (TextView) view.findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        homeTb1 = (Toolbar) view.findViewById(R.id.home_tb1);
        tablayout = (TabLayout) view.findViewById(R.id.tablayout);
        vp = (XiongViewPager) view.findViewById(R.id.vp);
        drawlayout = (DrawerLayout) view.findViewById(R.id.drawlayout);
        //二维码扫描
        scan_qr = (ImageView) view.findViewById(R.id.scan_qr);
        scan_qr.setOnClickListener(this);

        package_rl = (RelativeLayout) view.findViewById(R.id.package_rl);
        package_rl.setOnClickListener(this);

        job_rl = (RelativeLayout) view.findViewById(R.id.job_rl);
        job_rl.setOnClickListener(this);

        present_im = (RelativeLayout) view.findViewById(R.id.present_rl);
        present_im.setOnClickListener(this);

        circleImageView = (CircleImageView) view.findViewById(R.id.headIcon);
        circleImageView.setOnClickListener(this);

        //待我取件
        root_wait_rl = (RelativeLayout) view.findViewById(R.id.root_wait_rl);
        root_wait_rl.setOnClickListener(this);
        //已经签收
        root_sign_rl = (RelativeLayout) view.findViewById(R.id.root_sign_rl);
        root_sign_rl.setOnClickListener(this);


        imagebutton = (ImageView) view.findViewById(R.id.imagebutton);
        imagebutton.setOnClickListener(this);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_name.setOnClickListener(this);
        levelIm = (ImageView) view.findViewById(R.id.levelIm);
        levelIm.setOnClickListener(this);


        //这个用户名只是在登录的时候获取到的，登录的时候还要传手机号和密码或者验证码，所以获取真实名字还的通过别的方法，如果在用户实名之后
        //下面这个方法只能是在登录之后获取，也不知道有没有坑 4.26
        String realname = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "realname", "");
        if (!realname.equals("")) {
            tv_name.setText(realname);
        }

        lv_menu = (ListView) view.findViewById(R.id.lv_menu);
        menuListviewadapter = new MenuListviewadapter(menuString,icon_im);
        //左侧侧滑菜单
        lv_menu.setAdapter(menuListviewadapter);
        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,  int position, long id) {

                MenuItemFragment fragment = MenuItemFragment.newInstance(menuurls[position],true);

                getFragmentManager().beginTransaction()
                        .replace(R.id.fy,fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        //设置手机号
        starttv = (TextView) view.findViewById(R.id.start_tv);
        starttv.setOnClickListener(this);
//        final Drawable drawable=getResources().getDrawable(R.drawable.into);
//        drawable.setBounds(0,0,10,10);
//        starttv.setCompoundDrawables(null,null,drawable,null);

//        Random random = new Random();
//        int i = random.nextInt(100);
//        wait_num_tv.setText(String.valueOf(i));
//        sign_num_tv.setText(String.valueOf(i));

        //这个一定要加上，不然根本不会出来 4.12(底部抽屉布局)
        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetLayout));
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorlayout);


        /***
         *
         *
         * 很简单只是自己没有思考而已,就是和fragment的点击穿透一样 4.20
         *
         */
        drawlayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
                //关联back回退 4.26
                SecondActivity.setgetDrawlayoutstatecallback(ExprssMainFragment.this);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                SecondActivity.setgetDrawlayoutstatecallback(null);

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });



        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {


            }
        });

        inittoolbar();

        showtab();

        //获取快递员名片 5.7
        getCourierimageinfor();

        GetUserHeadim();
//        idcard_im1.setScaleType(ImageView.ScaleType.FIT_XY);
//        SetNetUrlToImageview(idcard_im1,0);
//        idcard_im2.setScaleType(ImageView.ScaleType.FIT_XY);
//        SetNetUrlToImageview(idcard_im2,1);
//        idcard_im3.setScaleType(ImageView.ScaleType.FIT_XY);
//        SetNetUrlToImageview(idcard_im3,2);


//        ((ViewGroup) tablayout.getChildAt(0)).getChildAt(3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyDiaologFragment myDiaolog = new MyDiaologFragment();
//                myDiaolog.show(getFragmentManager(), "haha");
//            }
//        });


        /***
         *
         * 这里有个很神奇的现象，当捕捉的是action_down的时候不会产生水波纹效果，当是action_up的时候则会
         * action_down
         * action_up
         * 5.4
         *
         */

        //  这个就很精髓了
        //  先是执行ontouch时间，才会执行onclick时间
        ((ViewGroup) tablayout.getChildAt(0)).getChildAt(4).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
//                    MyDiaologFragment myDiaolog = new MyDiaologFragment();
//                    myDiaolog.show(getFragmentManager(), "haha");
                    ToastUtil.showToastCenter("功能紧急开发中");
//                    InputExpressNumDialogFragment inputExpressNumDialogFragment=new InputExpressNumDialogFragment();
//                    inputExpressNumDialogFragment.show(getFragmentManager(),"haha");
                    return true;
                }
                return false;
            }
        });

        /***
         *
         *
         *
         */
//        ((ViewGroup) tablayout.getChildAt(0)).getChildAt(3).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                if (action == MotionEvent.ACTION_DOWN) {
//                    ToastUtil.showToastCenter("功能紧急开发中");
//                    return true;
//                }
//                return false;
//            }
//        });
//
//
//        ((ViewGroup) tablayout.getChildAt(0)).getChildAt(2).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                if (action == MotionEvent.ACTION_DOWN) {
//
//                    ToastUtil.showToastCenter("功能紧急开发中");
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        ((ViewGroup) tablayout.getChildAt(0)).getChildAt(1).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                if (action == MotionEvent.ACTION_DOWN) {
//
//                    ToastUtil.showToastCenter("功能紧急开发中");
//                    return true;
//                }
//                return false;
//            }
//        });


        MenuItemFragment.sethiddenButtonSheetcallback(this);
        Cfragment.sethiddenButtonSheetcallback(this);
        Afragment.sethiddenButtonSheetcallback(this);


        InputExpressNumDialogFragment.setonInpunumtCompletecallback(this);
        LoginFragment.setgetuserinforcallback(this);
        //BaseWebFragment.setgetDrawlayoutstatecallback(this);


        //头一回登录的时候展示用户头像
        LoginFragment.setGetheadimgcallback(this);
        MenuItemFragment.setWebviewChangeHeadImcallback(this);


        //获取当前待取件和已取件的信息
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        if (user_id.matches("[0-9]+")) {
            GetUserInfor();
            //侧滑菜单可滑动
            drawlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else {
            sign_num_tv.setText("0");
            wait_num_tv.setText("0");
            //侧滑菜单不可滑动
           drawlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        //轮询 5.7
        new Thread(mRunnable).start();

    }


    public void SetNetUrlToImageview(ImageView imageView,int i){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.a)
                .error(R.drawable.a)
                .priority(Priority.HIGH);
        Glide.with(getActivity())
                .asBitmap()
                .load(Constants.IMAGE+"/"+imageurls.get(i))
                .apply(options)
                .into(imageView);
    }


    //原首页下方快递员名片，获取三张图
    public void getCourierimageinfor(){
        final long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        final String s = EncodeBuilder.javaToexpressimageinfor("express_work",str);
        final String sign = EncodeBuilder.newString2(s);
        LogUtil.e("--地址是--"+Constants.GET_USER_CARDIMAGE);
        httphelper.create().GetExpressImageinfo(Constants.GET_USER_CARDIMAGE, "express_work", sign, str, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                LogUtil.e(s);
                imageurldetails.clear();
                GetCodebean getCodebean = GsonUtil.parseJsonToBean(s, GetCodebean.class);
                if (getCodebean.code.equals("100000")) {
                    List<CodeDatabean> data = getCodebean.data;
                    for (int i = 0; i < data.size(); i++) {
                        String value = data.get(i).value;
                        //imageurls.add(value);
                       imageurldetails.add(data.get(i).url);

                        RequestOptions options = new RequestOptions()
                                .placeholder(R.drawable.a)
                                .error(R.drawable.a)
                                .priority(Priority.HIGH);
                        Glide.with(getActivity())
                                .asBitmap()
                                .load(value)
                                .apply(options)
                                .into(imageViews.get(i));
                    }
                }else {
                    for (int i = 0; i < imageViews.size(); i++) {
                        imageurldetails.add("/");
                        RequestOptions options = new RequestOptions()
                                .placeholder(R.drawable.a)
                                .error(R.drawable.a)
                                .priority(Priority.HIGH);
                        Glide.with(getActivity())
                                .asBitmap()
                                .load(R.drawable.a)
                                .apply(options)
                                .into(imageViews.get(i));

                    }
                }
            }

            @Override
            public void fail(Exception e) {
                LogUtil.e(e+"");
                //网络错误时候显示的图片
                for (int i = 0; i < imageViews.size(); i++) {
                    imageurldetails.add("/");
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.a)
                            .error(R.drawable.a)
                            .priority(Priority.HIGH);
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(R.drawable.a)
                            .apply(options)
                            .into(imageViews.get(i));

                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        BaseWebFragment.setgetDrawlayoutstatecallback(this);
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        String realname = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "realname", "");
        String phone_num = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "phone_num", "");
        if (!realname.equals("")) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText(realname);
        }else {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText("姓名（空）");
        }
        if (!phone_num.equals("")){
            starttv = (TextView) view.findViewById(R.id.start_tv);
            starttv.setText(phone_num);
        }
        if (user_id.matches("[0-9]+")) {
            //已登录的时候则能滑出 5.16
            drawlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else {
            sign_num_tv.setText("0");
            wait_num_tv.setText("0");
            //未登录的时候，消息左侧侧滑菜单不可滑出！！5.16
            drawlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

//        if (user_id.matches("[0-9]+")&&showADdialog) {
//            WebviewDialogFragment fragment = WebviewDialogFragment.newInstance("http://www.jb51.net/article/83126.htm");
//            fragment.show(getFragmentManager(), "WebviewDialogFragment");
//        }
    }



//    @Override
//    public void onStart() {
//        super.onStart();
//
//        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
//
//        if (user_id.matches("[0-9]+")) {
//            GetUserInfor();
//        }else {
//            sign_num_tv.setText("0");
//            wait_num_tv.setText("0");
//        }
//    }

    private void inittoolbar() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(homeTb1);
        homeTb1.setTitleTextColor(Color.BLACK);
        homeTb1.setNavigationIcon(R.drawable.me);
        title.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.x45), 0);
        homeTb1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogUtil.e("--被点击了么--");
                String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");

                //没有userid的时候进行登录
                if (!user_id.matches("[0-9]+")) {
                    //没有userid就跳到登录 4.24
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fy,new LoginFragment())
                            .addToBackStack(null)
                            .commit();

                    //有userid的时候打开
                }else if (user_id.matches("[0-9]+")){

                    //有userid的时候能够拉出
                    drawlayout.openDrawer(GravityCompat.START);
                int drawerLockMode = drawlayout.getDrawerLockMode(GravityCompat.START);
                if (drawlayout.isDrawerVisible(GravityCompat.START)
                        && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {



                    LogUtil.e("23331111");

                    //下面这个第一个判断是手势划出 4.26
                } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
                    drawlayout.openDrawer(GravityCompat.START);



                    SecondActivity.setgetDrawlayoutstatecallback(ExprssMainFragment.this);
                    //拉出来的时候走这个方法 4.20
                    LogUtil.e("23332222");


                }

                }



            }
        });
    }


    /***获取快递员基本信息和登录之后获取快递员信息一致 5.23***/
    private void GetUserHeadim(){
        long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        String user_id1 = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        HashMap<String,String> datas=new HashMap<>();
        datas.put("user_id",user_id1);
        datas.put("timestamp",str);
        datas.put("appv","v1");
        String s = EncodeBuilder.javaToJSONNewPager1(datas);
        String sign = EncodeBuilder.newString2(s);
        datas.put("sign",sign);
        httphelper.create().NewdataHomePage2(Constants.USER_INFOR_DETAIL, datas, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                LogUtil.e(s);
                final LoginBean loginBean = GsonUtil.parseJsonToBean(s, LoginBean.class);
                if (loginBean.code.equals("100000")){

                    //获取用户真实姓名
                    tv_name.setText(loginBean.data.realname);



                    //下面是获取头像
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.ic_launcher)
                            .error(R.drawable.ic_launcher)
                            .priority(Priority.HIGH);
                    Glide.with(getActivity())
                            .asBitmap()
                            // TODO: 2018/5/17   这里获取头像的有点问题，问清楚
                            .load(loginBean.data.headimg)
                            .apply(options)
                            .into(circleImageView);
                    //当服务器返回的响应码不为100000

                    //下面是获取等级图片
                    RequestOptions options1 = new RequestOptions()
                            .placeholder(R.drawable.level_im)
                            .error(R.drawable.level_im)
                            .priority(Priority.HIGH);
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(loginBean.data.level_img)
                            .apply(options1)
                            .into(levelIm);


                }else {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.ic_launcher)
                            .error(R.drawable.ic_launcher)
                            .priority(Priority.HIGH);
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(R.drawable.ic_launcher)
                            .apply(options)
                            .into(circleImageView);

                }

            }

            @Override
            public void fail(Exception e) {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .priority(Priority.HIGH);
                Glide.with(getActivity())
                        .asBitmap()
                        .load(R.drawable.ic_launcher)
                        .apply(options)
                        .into(circleImageView);

            }
        });

    }


    //这个接口和获取标签的接口相同 4.25(获取已取件和代取件的数目)
    private void GetUserInfor() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {

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

                            sign_num_tv.setText(String.valueOf(receivedCount));
                            wait_num_tv.setText(String.valueOf(nonTakeCount));
                            if (data.pushMess.equals("0")) {
                                imagebutton.setImageResource(R.drawable.mess);
                            }
                            else if (data.pushMess.equals("1")){
                                imagebutton.setImageResource(R.drawable.n_mess);
                            }
                        }

                        @Override
                        public void fail(Exception e) {
                            LogUtil.e("--个人信息失败--"+e);

                        }
                    });


           // }
       // }).start();


            }




    private void showtab() {

        //可以很坑爹，getfragmentmanager也是能用的。。至于之前的小demo不使用getchildfragment会报错未知 4.23
        //发现问题，该为getfragmentmanger的时候退出回退栈会报错 4.23

        MainViewPagerForFragment mvf = new MainViewPagerForFragment(getChildFragmentManager(),titleArray);
        mvf.add(new Afragment());

        //mvf.add(new MenuItemFragment());
        // https://www.emiaoqian.com/recharge

        /**
         *
         * 这里有个问题，要是使用像activity独立跳转那样的activity的fragment基类，这个tablayout+viewpager+fragment的组合有问题
         * 加载的时候只能全部加载，才不会出错。但是很耗内存。
         *
         * 以后用像谷歌市场那样的方法来解决。所以以后基类要换一个！！ 5.9为了tablayout上的fragment。fragment之间的独立跳转就用之前的就好了
         *
         *
         */
        MenuItemFragment fragment = MenuItemFragment.newInstance(Constants.MIAOQIAN_NEW,false);
        //MenuItemFragment fragment1 = MenuItemFragment.newInstance("https://www.baidu.com/s?wd=csdn&ie=UTF-8",false);
        MenuItemFragment fragment1 = MenuItemFragment.newInstance(Constants.MIAOQIAN_EQUITY,false);
        MenuItemFragment fragment2 = MenuItemFragment.newInstance(Constants.MIAOQAIN_LIFE,false);
       // http://192.168.3.59/mobile_app/fastgood/sendInfo/id/44
        //MenuItemFragment fragment2 = MenuItemFragment.newInstance("http://192.168.3.59/mobile_app/life/index",false);

        mvf.add(fragment);
        mvf.add(fragment1);
        mvf.add(fragment2);
        mvf.add(new EmptyFragment());

        //提前预加载也能够防止被销毁，但是开始可能就很卡
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(mvf);
        //可惜，和tablayout结合的viewpager不需要framelayout？2017.
        //tablayout的作用是绑定viewpager。
        tablayout.setupWithViewPager(vp);

        //下面这个是设置指示器的颜色，就是滑动时候的指示器。4.10
        //tablayout.setSelectedTabIndicatorColor(Color.BLUE);

        //这个是选中颜色和未选中颜色 4.11
        tablayout.setTabTextColors(Color.GRAY,getActivity().getResources().getColor(R.color.tab_layout_text_color));



        tablayout.setSelectedTabIndicatorHeight(0);


        //这个就是修改整个tablayout整个背景颜色的方法。4.10
        //tablayout.setBackground(new ColorDrawable(Color.RED));


        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu);

        //bitmap简直万能啊
       // Bitmap bitmap1 = changeBitmapSize(R.drawable.menu);
        Bitmap bitmap1 = drawable2bitmap(R.drawable.menu);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap1);

        //
        //为最后一个tab添加图片 4.16
        tablayout.getTabAt(4).setIcon(bitmapDrawable);

    }

    //修改bitmap图片的大小 4.25
    private Bitmap changeBitmapSize(int drawableid) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableid);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
//        Log.e("width","width:"+width);
//        Log.e("height","height:"+height);
        //设置想要的大小

        //https://blog.csdn.net/cnmilan/article/details/38339109
        int size = getResources().getDimensionPixelSize(R.dimen.x30);


        int newWidth=size;
        int newHeight=size;

        //计算压缩的比率
        float scaleWidth=((float)newWidth)/width;
        float scaleHeight=((float)newHeight)/height;

        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);

        //获取新的bitmap
        bitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        //bitmap=Bitmap.createBitmap(bitmap,0,0,size,size);
        bitmap.getWidth();
        bitmap.getHeight();
//        Log.e("newWidth","newWidth"+bitmap.getWidth());
//        Log.e("newHeight","newHeight"+bitmap.getHeight());
        return bitmap;
    }


    public Bitmap drawable2bitmap(int drawableid){
        int size = getResources().getDimensionPixelOffset(R.dimen.x30);
        //int size=30;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableid);
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(bitmap, size, size);
        return bitmap1;
    }






    @Override
    public void onClick(View v) {
        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        switch (v.getId()){

            case R.id.scan_qr:

                if (user_id.matches("[0-9]+")) {

                    initPermission(permission,dialogtitle,content);
                    //拍照，小米的没有主动弹权限的框 4.27
                    startActivityForResult(new Intent(getActivity(),CaptureActivity.class),QRCode_Action);
                }else {
                   getFragmentManager().beginTransaction()
                           .replace(R.id.fy,new LoginFragment())
                           .addToBackStack(null)
                           .commit();
                }


                break;

            case R.id.input_express_num:



                if (user_id.matches("[0-9]+")) {

                    InputExpressNumDialogFragment fragment=new InputExpressNumDialogFragment();
                    fragment.show(getFragmentManager(),"InputExpressNumDialogFragment");


                }else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fy,new LoginFragment())
                            .addToBackStack(null)
                            .commit();
                }


                break;



            case R.id.package_rl:
                //http://192.168.3.59/mobile_app/setting/index
                MenuItemFragment fragment2 = MenuItemFragment.newInstance(Constants.MY_CARD,true);
                //MenuItemFragment fragment2 = MenuItemFragment.newInstance("http://192.168.3.112/mobile_app/personal/authcardstart",true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fy,fragment2)
                        .addToBackStack(null)
                        .commit();

                //drawlayout.closeDrawer(GravityCompat.START);

                break;

            case R.id.job_rl:
                MenuItemFragment fragment = MenuItemFragment.newInstance(Constants.EXPRESS_OFFIC,true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fy,fragment)
                        .addToBackStack(null)
                        .commit();

                break;

            case R.id.present_rl:
                MenuItemFragment fragment1 = MenuItemFragment.newInstance(Constants.PRESENT,true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fy,fragment1)
                        .addToBackStack(null)
                        .commit();

                break;


            case R.id.tv_name:
            case R.id.start_tv:
            case R.id.levelIm:


            case R.id.headIcon:
                MenuItemFragment fragment3 = MenuItemFragment.newInstance(Constants.HEAD_ICON,true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fy,fragment3)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.root_wait_rl:

                if (user_id.matches("[0-9]+")) {
                    MenuItemFragment fragment4 = MenuItemFragment.newInstance(Constants.WITE_ME_RECEIVER,true);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fy, fragment4)
                            .addToBackStack(null)
                            .commit();
                }else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fy,new LoginFragment())
                            .addToBackStack(null)
                            .commit();
                }

                break;

            case R.id.root_sign_rl:

                if (user_id.matches("[0-9]+")) {
                    MenuItemFragment fragment5 = MenuItemFragment.newInstance(Constants.ALREADY_SIGN,true);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fy, fragment5)
                            .addToBackStack(null)
                            .commit();
                }else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fy,new LoginFragment())
                            .addToBackStack(null)
                            .commit();

                }

                break;

            case R.id.imagebutton:

                if (user_id.matches("[0-9]+")) {
                    //"http://192.168.3.59/mobile_app/life"
                    MenuItemFragment fragment6 = MenuItemFragment.newInstance(Constants.MESSAGE_EXPRESS,true);
                   // MenuItemFragment fragment6 = MenuItemFragment.newInstance("http://shouji.baidu.com/software/23920909.html",true);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fy, fragment6)
                            .addToBackStack(null)
                            .commit();


//                    WebviewDialogFragment diafragment = WebviewDialogFragment.newInstance("aaa");
//                    diafragment.show(getFragmentManager(),"WebviewDialogFragment");

                }else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fy,new LoginFragment())
                            .addToBackStack(null)
                            .commit();
                }

                break;

            case R.id.idcard_im1:
                //第一个是快递员名片首先是要登录之后才能点击！！5.29
                if (user_id.matches("[0-9]+")) {
                    MenuItemFragment fragment4 = MenuItemFragment.newInstance(imageurldetails.get(0), true);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fy, fragment4)
                            .addToBackStack(null)
                            .commit();
                }else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fy,new LoginFragment())
                            .addToBackStack(null)
                            .commit();

                }

                break;
            case R.id.idcard_im2:
                MenuItemFragment fragment5 = MenuItemFragment.newInstance(imageurldetails.get(1), true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fy,fragment5)
                        .addToBackStack(null)
                        .commit();

                break;
            case R.id.idcard_im3:
                MenuItemFragment fragment6 = MenuItemFragment.newInstance(imageurldetails.get(2), true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fy,fragment6)
                        .addToBackStack(null)
                        .commit();

                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!= Activity.RESULT_OK) {
            return;
        }
        if (resultCode==Activity.RESULT_OK){
            switch (requestCode){
                case QRCode_Action:

                    final String qrcode = data.getStringExtra("qrcode");
                    HashMap<String,String> datas=new HashMap<>();
                    //{"code":"1234", "appv":"v1", "timestamp":"1111111111111", "sign":"11","test_key":"MIAOQIAN_API_TEST"
                    datas.put("code",qrcode);
                    datas.put("appv","v1");
                    long l = System.currentTimeMillis();
                    String time = String.valueOf(l);
                    datas.put("timestamp",time);
                    String s = EncodeBuilder.javaToJSONNewPager1(datas);
                    String sign = EncodeBuilder.newString2(s);
                    datas.put("sign", sign);
                    httphelper.create().NewdataHomePage2(Constants.SCAN_QR, datas, new httphelper.httpcallback() {
                        @Override
                        public void success(String s) {
                            LogUtil.e(s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String data1 = jsonObject.getString("data");
                                CodeDatabean codeDatabean = GsonUtil.parseJsonToBean(data1, CodeDatabean.class);
                                //LogUtil.e("-"+codeDatabean);

                                if (codeDatabean.status.equals("1")){
                                    SignMessageFragemnt signMessageFragemnt=new SignMessageFragemnt();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("qrcode",qrcode);
                                    signMessageFragemnt.setArguments(bundle);

                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.fy,signMessageFragemnt)
                                            .addToBackStack(null)
                                            .commit();

                                }else if (codeDatabean.status.equals("2")){
                                    MenuItemFragment fragment = MenuItemFragment.newInstance(codeDatabean.data,true);
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.fy,fragment)
                                            .addToBackStack(null)
                                            .commit();
                                }else {
                                    ToastUtil.showToastCenter("网络错误，请稍后~");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                ToastUtil.showToastCenter("网络错误，请稍后~");
                            }

                        }

                        @Override
                        public void fail(Exception e) {

                        }
                    });

                    //下面是原来的
//                    if (qrcode.toString().trim()!=null){
//                        SignMessageFragemnt signMessageFragemnt=new SignMessageFragemnt();
//                        Bundle bundle=new Bundle();
//                        bundle.putString("qrcode",qrcode);
//                        signMessageFragemnt.setArguments(bundle);
//
//                       getFragmentManager().beginTransaction()
//                               .replace(R.id.fy,signMessageFragemnt)
//                               .addToBackStack(null)
//                               .commit();
//
//                    }

                    break;

            }
        }
    }




    //在tab上面的各个子fragment的，控制buttomsheet的隐藏和显示 4.22

    @Override
    public void HiddenButtonSheetcallback(boolean ishide) {
        if (ishide) {
            bottomSheetBehavior.setHideable(true);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }else {
            bottomSheetBehavior.setHideable(false);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    //手动输入订单号
    @Override
    public void onInpunumtComplete(final String text) {
        HashMap<String,String> datas=new HashMap<>();
        //{"code":"1234", "appv":"v1", "timestamp":"1111111111111", "sign":"11","test_key":"MIAOQIAN_API_TEST"
        datas.put("code",text);
        datas.put("appv","v1");
        long l = System.currentTimeMillis();
        String time = String.valueOf(l);
        datas.put("timestamp",time);
        String s = EncodeBuilder.javaToJSONNewPager1(datas);
        String sign = EncodeBuilder.newString2(s);
        datas.put("sign", sign);
        httphelper.create().NewdataHomePage2(Constants.SCAN_QR, datas, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                LogUtil.e(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data1 = jsonObject.getString("data");
                    CodeDatabean codeDatabean = GsonUtil.parseJsonToBean(data1, CodeDatabean.class);
                    //LogUtil.e("-"+codeDatabean);

                    if (codeDatabean.status.equals("1")){
                        SignMessageFragemnt signMessageFragemnt=new SignMessageFragemnt();
                        Bundle bundle=new Bundle();
                        bundle.putString("qrcode",text);
                        signMessageFragemnt.setArguments(bundle);

                        getFragmentManager().beginTransaction()
                                .replace(R.id.fy,signMessageFragemnt)
                                .addToBackStack(null)
                                .commit();

                    }else if (codeDatabean.status.equals("2")){
                        MenuItemFragment fragment = MenuItemFragment.newInstance(codeDatabean.data,true);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fy,fragment)
                                .addToBackStack(null)
                                .commit();
                    }else {
                        ToastUtil.showToastCenter("网络错误，请稍后~");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showToastCenter("网络错误，请稍后~");
                }

            }

            @Override
            public void fail(Exception e) {

            }
        });

        //==========================
//                        SignMessageFragemnt signMessageFragemnt=new SignMessageFragemnt();
//                Bundle bundle=new Bundle();
//                bundle.putString("qrcode",text);
//                signMessageFragemnt.setArguments(bundle);
//
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.fy,signMessageFragemnt)
//                        .addToBackStack(null)
//                        .commit();

    }


    //添加一个快件信息，以及减少一个待收件



    @Override
    public void getuserinforcallback(String wait, String signed,String haspush,String checkurl) {
        sign_num_tv.setText(signed);
        wait_num_tv.setText(wait);
        //0是没消息，1是有消息
        if (haspush.equals("1")) {
            imagebutton.setImageResource(R.drawable.n_mess);
        }else if (haspush.equals("0")){
            imagebutton.setImageResource(R.drawable.mess);
        }

        if (!checkurl.equals("")){
            //WebviewDialogFragment fragment=new WebviewDialogFragment();
            WebviewDialogFragment fragment = WebviewDialogFragment.newInstance(checkurl);
            fragment.show(getFragmentManager(),"WebviewDialogFragment");
            showADdialog=false;
        }

    }

    //登录之后刷新头像的接口 5.17
    @Override
    public void Getheadimgcallback(String headimg,String leve_im) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.present)
                .error(R.drawable.present)
                .priority(Priority.HIGH);
        Glide.with(getActivity())
                .asBitmap()
                //// TODO: 2018/5/17 问清楚，到底是什么
                .load(headimg)
                .apply(options)
                .into(circleImageView);


        //设置等级图片 // TODO: 2018/5/29
        RequestOptions options1 = new RequestOptions()
                .placeholder(R.drawable.level_im)
                .error(R.drawable.level_im)
                .priority(Priority.HIGH);
        Glide.with(getActivity())
                .asBitmap()
                //// TODO: 2018/5/17 问清楚，到底是什么
                .load(leve_im)
                .apply(options1)
                .into(levelIm);


    }




    @Override
    public void GetDrawlayoutstatecallback() {

        drawlayout.closeDrawers();
//        if (drawlayout.isDrawerVisible(GravityCompat.START)){
//            drawlayout.closeDrawer(GravityCompat.START);
//            //drawlayout.closeDrawers();
//        }
        //用完了就消除，
        SecondActivity.setgetDrawlayoutstatecallback(null);
    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(30000);
                    //mHandler.sendMessage(mHandler.obtainMessage());
                    handler.sendMessage(handler.obtainMessage(1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        LogUtil.e(getClass().getSimpleName()+"---"+"onDestroy");
        super.onDestroy();
    }

    @Override
    public void WebviewChangeHeadImcallback() {
        GetUserHeadim();
    }
}
