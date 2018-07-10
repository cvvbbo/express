package com.emiaoqian.express.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autonavi.amap.mapcore.FileUtil;
import com.emiaoqian.express.R;
import com.emiaoqian.express.activity.PhotoviewActivity;
import com.emiaoqian.express.activity.SecondActivity;
import com.emiaoqian.express.activity.landscapeSignboardActivity;
import com.emiaoqian.express.application.MyApplication;
import com.emiaoqian.express.bean.LoginBean;
import com.emiaoqian.express.interfaces.Myinterface;
import com.emiaoqian.express.utils.AudioPlayerUtil;
import com.emiaoqian.express.utils.AudioRecorderUtil;
import com.emiaoqian.express.utils.Constants;
import com.emiaoqian.express.utils.EncodeBuilder;
import com.emiaoqian.express.utils.GaoDeUtils;
import com.emiaoqian.express.utils.GsonUtil;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.MakephotoUtils;
import com.emiaoqian.express.utils.PopupWindowFactory;
import com.emiaoqian.express.utils.TimeUtils;
import com.emiaoqian.express.utils.ToFileUtil;
import com.emiaoqian.express.utils.ToastUtil;
import com.emiaoqian.express.utils.httphelper;
import com.emiaoqian.express.utils.sharepreferenceUtils;
import com.emiaoqian.express.views.BadgeView;
import com.emiaoqian.express.views.MyImageview;
import com.emiaoqian.express.views.MydialogOnbutton1;
import com.emiaoqian.express.views.XiongProgressDialog;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.R.string.cancel;

/**
 * Created by xiong on 2018/4/16.
 */

public class SignMakePhotoAndVoiceFragment extends BaseFragment implements View.OnClickListener
        , Myinterface.DismessSmallDialog, Myinterface.DismessForNetwork {

    //存储传入的数据
    HashMap<String, String> data = new HashMap<>();
    private TextView tv7;
    private TextView tv8;

//    ImageView frontId;
//    ImageView reverseId;


    //照片的名字
    private String photoname1;

    private Bitmap albumbitmap_frontId;

    private Bitmap makephotobitmap_frontId;
    private Bitmap makephotobitmap_reverseId;
    private Bitmap albumbitmap_reverseId;

    private static boolean zheng = true;
   // private RelativeLayout photo_rl;
    private RelativeLayout photo_rl_second;


    ImageView recordBtn;
    ImageView recordDetailView;
    TextView tv_time;
    LinearLayout record_contentLayout;
    String finishurl;

    /**
     * 录音相关
     **/
    private boolean audioRecorder = false;
    private AudioPlayerUtil player;
    // private Button recordBtn;//录音按钮
    private String ROOT_PATH;
    ImageView mImageView;
    TextView mTextView;
    PopupWindowFactory mPop;
    View popview;
    //  LinearLayout record_contentLayout;
    //   ImageView recordDetailView;
    private String audioFilePath;// 录音文件保存路径
    private AnimationDrawable animationDrawable;
    private TextView again_tv;
    private RelativeLayout my_voicerl;
    private TextView next_tv;
    private SigntvDialogFragment signtvDialogFragment;

    private Handler handler = new Handler();
    private TextView phonenum;
    private TextView signtime;
    private TextView receivername;
    private TextView expressnum;

    private String currenttime;
    private ImageView returnIm;

    //记录录音时间
    private String recordtime="";

    //十位数字的时间戳，不是加密的
    private String timenum;

    //签收人加签收标签
    private String receiverallname;

    private String recordsignnum;


    //动态权限 4.27
    String[] permission = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};//需要的权限
    String dialogtitle = "录音权限权限不可用";
    String content = "为了保证语音正常使用,需要开启麦克风权限\n否则，您将无法正常使用语音功能";
    private MyImageview position1;
    private MyImageview position2;
    private RelativeLayout returnIm_rl;
    private Bitmap bitmap123;
    private Bitmap bitmap1231;


    public static SignMakePhotoAndVoiceFragment newInstance(String express_num, String receiver_name
            , String phone_num, String receiver_mess) {
        Bundle bundle = new Bundle();
        bundle.putString("express_num", express_num);
        bundle.putString("receiver_name", receiver_name);
        bundle.putString("phone_num", phone_num);
        bundle.putString("receiver_mess", receiver_mess);
        SignMakePhotoAndVoiceFragment fragemnt = new SignMakePhotoAndVoiceFragment();
        fragemnt.setArguments(bundle);
        return fragemnt;
    }


    @Override
    public int getlayout() {
        return R.layout.c_fragment;
    }


    @Override
    public void onResume() {

        SecondActivity.getChildFragmentWebviewCallback(SignMakePhotoAndVoiceFragment.this);
        super.onResume();
    }

    @Override
    public void initialize() {

        makepermission();

        returnIm_rl = (RelativeLayout) view.findViewById(R.id.returnIm_rl);
        phonenum = (TextView) view.findViewById(R.id.phone_num);
        signtime = (TextView) view.findViewById(R.id.sign_time);
        receivername = (TextView) view.findViewById(R.id.receiver_name);
        expressnum = (TextView) view.findViewById(R.id.express_num);
        //包裹照片1
        position1 = (MyImageview) view.findViewById(R.id.position1);
        position1.Gettv().setText("拍包裹");

        position2 = (MyImageview) view.findViewById(R.id.position2);

        if (getArguments() != null) {
            String express_num = getArguments().getString("express_num");
            String receiver_name = getArguments().getString("receiver_name");
            String phone_num = getArguments().getString("phone_num");
            String receiver_mess = getArguments().getString("receiver_mess");

            //这里存在数组里面是像回退之后重新走生命周期方法的时候能够保存数据，但是现在不用了，开心 4.18
            data.put("express_num", express_num);
            data.put("receiver_name", receiver_name);
            data.put("phone_num", phone_num);

            phonenum.setText(phone_num);
            //获取当前时间
            currenttime = TimeUtils.getCurrentTime();
            timenum = TimeUtils.date2TimeStamp(currenttime, "yyyy-MM-dd HH:mm");
            signtime.setText(currenttime);
            expressnum.setText(express_num);
            //记录签收人和标签全长
            receiverallname = receiver_name + "/" + "(" + receiver_mess + ")";
            receivername.setText(receiver_name + "/" + "(" + receiver_mess + ")");

        }



        tv_time = (TextView) view.findViewById(R.id.tv_time);
        record_contentLayout = (LinearLayout) view.findViewById(R.id.record_contentLayout);
        recordDetailView = (ImageView) view.findViewById(R.id.record_detailView);
        recordBtn = (ImageView) view.findViewById(R.id.recordBtn);
        again_tv = (TextView) view.findViewById(R.id.again_tv);
        my_voicerl = (RelativeLayout) view.findViewById(R.id.my_voice);
        next_tv = (TextView) view.findViewById(R.id.next_tv);
        returnIm = (ImageView) view.findViewById(R.id.returnIm);
        returnIm.setOnClickListener(this);
        next_tv.setOnClickListener(this);

        again_tv.setOnClickListener(this);
        position1.setOnClickListener(this);
        position2.setOnClickListener(this);
        returnIm_rl.setOnClickListener(this);




        //录音相关
        init(getActivity());
        initAudioRecorderBtn();

        //横屏写字板
        landscapeSignboardActivity.setDismessSmallDialogCallback(this);
        SigntvDialogFragment.setDismessfornetworkcallback(this);

    }

    //这个是按下录音时候的播放效果
    private void initAudioRecorderBtn() {
        popview = View.inflate(getActivity(), R.layout.layout_microphone, null);
        mPop = new PopupWindowFactory(getActivity(), popview);
        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) popview.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) popview.findViewById(R.id.tv_recording_time);

        mImageView.setImageResource(R.drawable.frame2);
        animationDrawable = (AnimationDrawable) mImageView.getDrawable();
        animationDrawable.start();

//        PackageManager pm = getActivity().getPackageManager();
//        boolean permission1 = (PackageManager.PERMISSION_GRANTED ==
//                pm.checkPermission("android.permission.RECORD_AUDIO", "packageName"));
//        // LogUtil.e("---"+permission1);
//        if (!permission1){
//            //animationDrawable.stop();
//             ToastUtil.showToastCenter("录音相关权限获取失败,->请去设置中打开相关权限");
//            return;
//        }



        final AudioRecorderUtil audioRecorderUtil = new AudioRecorderUtil(ROOT_PATH);
        audioRecorderUtil.setOnAudioStatusUpdateListener(new AudioRecorderUtil.OnAudioStatusUpdateListener() {
            @Override
            public void onStart() {

                // record_contentLayout.setVisibility(View.GONE);
            }

            @Override
            public void onProgress(double db, long time) {
                //根据分贝值来设置录音时话筒图标的上下波动,同时设置录音时间
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onStop(String filePath) {


                mPop.dismiss();
                record_contentLayout.setVisibility(View.VISIBLE);
                //最终文件的路径是这个
                audioFilePath = filePath;
                Log.e("===path", audioFilePath);
                // TODO 上传音频文件
            }
        });



        recordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 停止播放
                if (player != null) {
                    player.stop();
                }
                audioRecorder = true;//正在录音
                // 处理动作
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initPermission(permission,dialogtitle,content);
                        boolean start = audioRecorderUtil.start();
                        if (!start){
                            break;
                        }
                        mPop.showAtLocation(view.getRootView(), Gravity.CENTER, 0, 0);


                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        long time = audioRecorderUtil.getSumTime();
                        if (time < 1000) {
                            boolean cancel = audioRecorderUtil.cancel();
                            if (!cancel){
                                mPop.dismiss();
                                break;
                            }
                            // Toast.makeText(MainActivity.this, "录音时间太短！", Toast.LENGTH_SHORT).show();
                            ToastUtil.showToastCenter("录音时间太短");
                            mImageView.getDrawable().setLevel(0);
                            mTextView.setText(TimeUtils.long2String(0));
                            audioRecorderUtil.stop();
                            mPop.dismiss();
                            record_contentLayout.setVisibility(View.GONE);
                            again_tv.setVisibility(View.GONE);
                            audioFilePath = "";
                            return true;
                        } else {
                            recordtime = String.valueOf(time / 1000);
                            tv_time.setText(time / 1000 + "s");
                        }
                        mImageView.getDrawable().setLevel(0);
                        mTextView.setText(TimeUtils.long2String(0));
                        audioRecorderUtil.stop();
                        mPop.dismiss();
                        record_contentLayout.setVisibility(View.VISIBLE);
                        again_tv.setVisibility(View.VISIBLE);
                        recordBtn.setEnabled(false);
                        break;


                }
                return true;
            }
        });


        //这个是录音播放按钮的动画 3.26
        record_contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(audioFilePath) || !audioRecorder) {
                    return;
                }
                if (player == null) {
                    player = new AudioPlayerUtil();
                } else {
                    player.stop();
                }

                //这个背景必须是动画，不能改3.29
                recordDetailView.setImageResource(R.drawable.frame1);
                animationDrawable = (AnimationDrawable) recordDetailView.getDrawable();
                animationDrawable.start();
                player.start(audioFilePath, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        animationDrawable.stop();
                        //下面这个是播放结束时候的动画 3.29
                        recordDetailView.setImageResource(R.drawable.gxx);
                    }
                });
            }
        });
    }

    //仿扣扣录音相关 3.30
    public void init(Context context) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        ROOT_PATH = appDir.getPath();
    }


    @Override
    public void onDestroy() {
        if (player != null) {
            player.stop();
        }

        Intent intent = new Intent();
        intent.putExtra("express_num", data.get("express_num"));
        intent.putExtra("receiver_name", data.get("receiver_name"));
        intent.putExtra("phone_num", data.get("phone_num"));
        //getTargetFragment().onActivityResult(233, Activity.RESULT_OK,intent);

        super.onDestroy();
    }

    //6.0的动态权限
    public void makepermission() {
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 222);
        }
    }




    //放大图片的跳转 3.29
    private void Photoview(String photoname) {
        if (photoname != null) {
            Intent intent = new Intent(getActivity(), PhotoviewActivity.class);
            intent.putExtra("photoname", photoname);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.position1:
                if (position1.Getrealim().getVisibility() != View.GONE) {
                    if (makephotobitmap_frontId != null) {
                        Photoview("package_img.png");
                        return;
                    } else if (albumbitmap_frontId != null) {
                        Photoview("package_img.png");
                        return;
                    }

                }

                //还要添加个dialog。。
                photoname1 = "package_img.png";
                MydialogOnbutton1 mydialog2 = new MydialogOnbutton1(getActivity(), this);
                zheng = true;
                mydialog2.show();


                break;

            case R.id.position2:

                if (position2.Getrealim().getVisibility() != View.GONE) {
                    if (makephotobitmap_reverseId != null) {
                        Photoview("position_img.png");
                        return;
                    } else if (albumbitmap_reverseId != null) {
                        Photoview("position_img.png");
                        return;
                    }

                }


                //还要添加个dialog。。
                photoname1 = "position_img.png";
                MydialogOnbutton1 mydialog3 = new MydialogOnbutton1(getActivity(), this);
                zheng = false;
                mydialog3.show();


                break;

            case R.id.again_tv:

                record_contentLayout.setVisibility(View.GONE);
                again_tv.setVisibility(View.GONE);
                recordBtn.setEnabled(true);

                break;

            case R.id.next_tv:

                File position_img = new File(new File(Environment.getExternalStorageDirectory(), "emiaoqian"), "position_img.png");
                File package_img = new File(new File(Environment.getExternalStorageDirectory(), "emiaoqian"), "package_img.png");

                File new_position_img = new File(new File(Environment.getExternalStorageDirectory(), "emiaoqian"), "1.png");
                File new_package_img = new File(new File(Environment.getExternalStorageDirectory(), "emiaoqian"), "2.png");


                if (position_img.exists()) {
                    MakephotoUtils.lubanImage(getActivity(), position_img.getPath(), position_img.getParent(), "2.png");

                }

                if (package_img.exists()) {
                    MakephotoUtils.lubanImage(getActivity(), package_img.getPath(), position_img.getParent(), "1.png");
                }

//                if (position1.Getrealim().getVisibility() == View.GONE) {
//                    ToastUtil.showToastCenter("没有包裹照片");
//                    return;
//                }
//
//                if (position2.Getrealim().getVisibility() == View.GONE) {
//                    ToastUtil.showToastCenter("没有包裹位置照片");
//                    return;
//                }

                if (position1.Getrealim().getVisibility() == View.GONE&&position2.Getrealim().getVisibility()==View.GONE){
                    ToastUtil.showToastCenter("至少拍摄一张照片~");
                    return;
                }


//                if (record_contentLayout.getVisibility() == View.GONE) {
//                    ToastUtil.showToastCenter("语音留言不能为空");
//                    return;
//                }



                signtvDialogFragment = new SigntvDialogFragment();
                signtvDialogFragment.show(getFragmentManager(), "signtvDialogFragment");


                break;

            case R.id.returnIm_rl:

                getFragmentManager().popBackStack();

                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case 100:
                    if (Build.VERSION.SDK_INT>Build.VERSION_CODES.N) {
                        if (zheng) {
                            //因为还是要从7.0新建的这个目录里面找文件。
                            makephotobitmap_frontId = MakephotoUtils.compressImage(mActivity, new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                    + "/test/" + "image.jpg").getPath());
                            MakephotoUtils.saveImage(getActivity(), makephotobitmap_frontId, photoname1);

                            position1.Getrealim().setVisibility(View.VISIBLE);
                            //
                            position1.Getrelativelayout().setVisibility(View.INVISIBLE);
                            //小图标
                            position1.Getsmallim().setVisibility(View.VISIBLE);

                            position1.Getrealim().setImageBitmap(makephotobitmap_frontId);
                            position1.Getsmallim().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    position1.Getrealim().setVisibility(View.GONE);
                                    position1.Getrelativelayout().setVisibility(View.VISIBLE);
                                    position1.Getsmallim().setVisibility(View.GONE);
                                }
                            });

                        } else {
                            makephotobitmap_reverseId = MakephotoUtils.compressImage(getActivity(), new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                    + "/test/" + "image.jpg").getPath());
                            MakephotoUtils.saveImage(getActivity(), makephotobitmap_reverseId, photoname1);


                            position2.Getrealim().setVisibility(View.VISIBLE);
                            position2.Getrelativelayout().setVisibility(View.INVISIBLE);
                            //小图标
                            position2.Getsmallim().setVisibility(View.VISIBLE);

                            position2.Getrealim().setImageBitmap(makephotobitmap_reverseId);
                            position2.Getsmallim().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    position2.Getrealim().setVisibility(View.GONE);
                                    position2.Getrelativelayout().setVisibility(View.VISIBLE);
                                    position2.Getsmallim().setVisibility(View.GONE);
                                }
                            });

                        }
                    }else {
                        if (zheng) {
                            makephotobitmap_frontId = MakephotoUtils.compressImage(mActivity,
                                    new File(Environment.getExternalStorageDirectory(), "image.jpg").getPath());

                            MakephotoUtils.saveImage(getActivity(), makephotobitmap_frontId, photoname1);


                            position1.Getrealim().setVisibility(View.VISIBLE);
                            //
                            position1.Getrelativelayout().setVisibility(View.INVISIBLE);
                            //小图标
                            position1.Getsmallim().setVisibility(View.VISIBLE);
                            position1.Getrealim().setImageBitmap(makephotobitmap_frontId);
                            position1.Getsmallim().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    position1.Getrealim().setVisibility(View.GONE);
                                    position1.Getrelativelayout().setVisibility(View.VISIBLE);
                                    position1.Getsmallim().setVisibility(View.GONE);
                                }
                            });
                        } else {

                            makephotobitmap_reverseId = MakephotoUtils.compressImage(getActivity(),
                                    new File(Environment.getExternalStorageDirectory(), "image.jpg").getPath());

                            MakephotoUtils.saveImage(getActivity(), makephotobitmap_reverseId, photoname1);


                            position2.Getrealim().setVisibility(View.VISIBLE);
                            position2.Getrelativelayout().setVisibility(View.INVISIBLE);
                            //小图标
                            position2.Getsmallim().setVisibility(View.VISIBLE);
                            position2.Getrealim().setImageBitmap(makephotobitmap_reverseId);
                           // position2.Getrealim().setImageBitmap(BitmapFactory.decodeFile(file1.getAbsolutePath()));
                            position2.Getsmallim().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    position2.Getrealim().setVisibility(View.GONE);
                                    position2.Getrelativelayout().setVisibility(View.VISIBLE);
                                    position2.Getsmallim().setVisibility(View.GONE);
                                }
                            });

                        }
                    }

                    //如果是bitmap里面不等于空，然后转跳Activity


                    break;

                //从相册中获取
                case 102:
                    ContentResolver resolver = getActivity().getContentResolver();
                    //照片的原始资源地址，这样获取并不是压缩过的，如果拍照的图片也是这样获取就是压缩过的！！
                    Uri originalUri = data.getData();

                    //使用ContentProvider通过URI获取原始图片
                    try {
                        if (zheng) {
                            albumbitmap_frontId = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                            position1.Getrealim().setVisibility(View.VISIBLE);
                            //
                            position1.Getrelativelayout().setVisibility(View.INVISIBLE);
                            //小图标
                            position1.Getsmallim().setVisibility(View.VISIBLE);
                            position1.Getrealim().setImageBitmap(albumbitmap_frontId);
                            position1.Getsmallim().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    position1.Getrealim().setVisibility(View.GONE);
                                    position1.Getrelativelayout().setVisibility(View.VISIBLE);
                                    position1.Getsmallim().setVisibility(View.GONE);
                                }
                            });


                            new Thread() {
                                @Override
                                public void run() {
                                    File file = MakephotoUtils.saveImageofalbum(getActivity(), albumbitmap_frontId, photoname1);
                                    Bitmap afterbitmap = MakephotoUtils.compressImage(mActivity, file.getPath());
                                    MakephotoUtils.saveImage(getActivity(), afterbitmap, photoname1);

                                }
                            }.start();



                        } else {
                            albumbitmap_reverseId = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                            position2.Getrealim().setVisibility(View.VISIBLE);
                            //
                            position2.Getrelativelayout().setVisibility(View.INVISIBLE);
                            //小图标
                            position2.Getsmallim().setVisibility(View.VISIBLE);
                            position2.Getrealim().setImageBitmap(albumbitmap_reverseId);
                            position2.Getsmallim().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    position2.Getrealim().setVisibility(View.GONE);
                                    position2.Getrelativelayout().setVisibility(View.VISIBLE);
                                    position2.Getsmallim().setVisibility(View.GONE);
                                }
                            });


                            //拍照之后好像不能直接就变成bitmap对象，是要单独获取到路径才行！！！(下面这个工具类是压缩图片里面的Compressor这个第三方的库)




                            //这个保存有点耗时，差不多10秒，才完成，先放在子线程中，现在主线程中
                            new Thread() {
                                @Override
                                public void run() {
                                    File file = MakephotoUtils.saveImageofalbum(getActivity(), albumbitmap_reverseId, photoname1);
                                    LogUtil.e(""+file);
                                    Bitmap afterbitmap = MakephotoUtils.compressImage(mActivity, file.getPath());
                                    MakephotoUtils.saveImage(getActivity(), afterbitmap, photoname1);

                                }
                            }.start();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;


            }

        }
    }


    /**
     * 网络请求在这里，横屏签名之后确认返回的这个（网络请求的逻辑是首先是关闭当前的dialogfragment，然后回到这个fragment） 4.24
     */
    @Override
    public void DismessSmallDialogcallback() {
        signtvDialogFragment.dismiss();

        UpFileToNet();
    }






    /**
     * 签名小框确认之后的网络请求
     */
    @Override
    public void DismessForNetworkcallback() {
        //ToastUtil.showToastCenter("我是网络请求");

        UpFileToNet();


    }

    private void UpFileToNet() {
        final Dialog mDialog = XiongProgressDialog.createLoadingDialog(getActivity(), "正在生成加密存证，请稍后");
        mDialog.show();//显示

        String receiver_name = getArguments().getString("receiver_name");
        String receiver_mess = getArguments().getString("receiver_mess");

        String express_num = expressnum.getText().toString().trim();
        String user_id1 = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        String phone_num = phonenum.getText().toString().trim();
        long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        //记得如果要修改的话javatojson这个方法里面也是要修改的 1.17
        //第三个参数是录音时长。

        String address = GaoDeUtils.create().getAddress();
        String latitude = GaoDeUtils.create().getLatitude();
        String longitude = GaoDeUtils.create().getLongitude();

        final String s = EncodeBuilder.javaToJSONsign(user_id1, str, express_num, phone_num,
                recordtime, currenttime, receiver_mess,receiver_name,longitude,latitude,address);
        final String sign = EncodeBuilder.newStringUpfile(s);


        File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
//        File file1 = new File(appDir, "package_img.png");
//        File file2 = new File(appDir, "position_img.png");
        File file1 = new File(appDir, "1.png");
        File file2 = new File(appDir, "2.png");
        File file3 = new File(appDir, "personal_sign.png");
        File mp3file2 = new File(appDir, "message_voice.mp3");
       // String[] files = new String[]{file1.getPath(), file2.getPath(), file3.getPath(), mp3file2.getPath()};

        ArrayList<String> files=new ArrayList<>();
        if (file1.exists()&&position1.Getrealim().getVisibility()!=View.GONE) {
            files.add(file1.getPath());
        }
        if (file2.exists()&&position2.Getrealim().getVisibility()!=View.GONE) {
            files.add(file2.getPath());
        }
        if (file3.exists()) {
            files.add(file3.getPath());
        }
        if (mp3file2.exists()&&record_contentLayout.getVisibility() != View.GONE) {
            files.add(mp3file2.getPath());
        }

//        for (String a : files) {
//            LogUtil.e("--文件的名字是--" + a);
//        }

        LogUtil.e("--地址是--" + Constants.sian_save);
        httphelper.create().upImage(Constants.sian_save,
                files,
                user_id1,
                express_num,
                phone_num,
                currenttime,
                receiver_mess,
                receiver_name,
                recordtime,
                longitude,
                latitude,
                str,
                address,
                sign, new httphelper.httpcallback() {
                    @Override
                    public void success(String s) {

                        LogUtil.e("--最后一页的信息是--"+s);


                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String code = jsonObject.getString("code");
                            if (code.equals("100000")) {
                                LoginBean loginBean = GsonUtil.parseJsonToBean(s, LoginBean.class);
                                //存证的编号 4.26
                                String doc_no = loginBean.data.doc_no;
                                recordsignnum=doc_no;
                                finishurl=loginBean.data.url;
                                mDialog.dismiss();
                                //登录成功时候跳转到最后一页
                                ReturnFragment();
                            } else {
                                mDialog.dismiss();
                                ToastUtil.showToastCenter("出了点小问题，请重试");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void fail(Exception e) {
                        LogUtil.e("fails data" + e);

                    }
                });
    }


    public void ReturnFragment() {
        if (getArguments() != null) {
            String express_num = getArguments().getString("express_num");
            String receiver_name = getArguments().getString("receiver_name");
            String phone_num = getArguments().getString("phone_num");
            String receiver_mess = getArguments().getString("receiver_mess");
            String address = GaoDeUtils.create().getAddress();
            String realaddress="";
            if (address!=null&& !address.equals("")){
               realaddress=address;
            }else {
                realaddress="暂无";
            }

            if (record_contentLayout.getVisibility() == View.GONE){
                recordtime="";
            }

            WatchSignFragment fragment = WatchSignFragment.newInstance(express_num, receiver_name, phone_num,
                    receiver_mess,
                    currenttime,
                    recordtime,
                    recordsignnum,
                    realaddress,
                    finishurl,
                    position1.Getrealim().getVisibility(),
                    position2.Getrealim().getVisibility());

            getFragmentManager().beginTransaction()
                    .replace(R.id.fy, fragment)
                    .addToBackStack(null)
                    .commit();

        }


    }


}
