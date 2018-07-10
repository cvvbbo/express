package com.emiaoqian.express.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emiaoqian.express.R;
import com.emiaoqian.express.activity.MainActivity;
import com.emiaoqian.express.activity.PhotoviewActivity;
import com.emiaoqian.express.activity.SecondActivity;
import com.emiaoqian.express.activity.WebviewtoNewActivity;
import com.emiaoqian.express.utils.AudioPlayerUtil;
import com.emiaoqian.express.utils.Constants;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.MakephotoUtils;

import java.io.File;

/**
 * Created by xiong on 2018/4/17.
 *
 *
 *   以后要多设置几个基类的basefragment，没种满足不同要求的，像这个app就是满足，回退之后保存数据的这个需求 5.22
 *
 *
 *
 */

public class WatchSignFragment extends BaseFragment implements View.OnClickListener {

    private ImageView returnIm;
    private TextView tv_time;
    private RelativeLayout returnIm_rl;
    private TextView tv1;
    private TextView tv11;

    public interface addonesignmessagelistener{

        void addonesignmessagecallback();
    }

//    public static  addonesignmessagelistener addonesignmessagelistener;
//    public static  void setaddonesignmessagelistener(addonesignmessagelistener addonesignmessagelistener){
//        WatchSignFragment.addonesignmessagelistener=addonesignmessagelistener;
//
//    }


    private TextView expressnum;
    private TextView nexttv;
    private ImageView signim;
    private ImageView positionim2;
    private ImageView positionim1;
    private TextView receiverposition;
    private TextView receivertime;
    private TextView receivertimemess;
    private TextView signmd5;
    private TextView phonenum;
    private TextView phonenummess;
    private TextView receivername;

    ImageView recordDetailView;
    LinearLayout record_contentLayout;

    private AnimationDrawable animationDrawable;
    private AudioPlayerUtil player;

    private boolean audioRecorder = true;

    File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
    File imagepath1=new File(appDir,"package_img.png");
    File imagepaht2=new File(appDir,"position_img.png");
    File imagepath3=new File(appDir,"personal_sign.png");


    //缺少一个坐标 4.18
    public static WatchSignFragment newInstance(String express_num,String receiver_name
            ,String phone_num,String receiver_mess,String receiver_time,String duration,
    String signnum,String my_position,String finishurl,int pack_img,int posi_img){

        WatchSignFragment fragment=new WatchSignFragment();
        Bundle bundle=new Bundle();
        bundle.putString("express_num",express_num);
        bundle.putString("receiver_name",receiver_name);
        bundle.putString("phone_num",phone_num);
        bundle.putString("receiver_mess",receiver_mess);
        bundle.putString("receiver_time",receiver_time);
        bundle.putString("duration",duration);
        bundle.putString("md5sign_num",signnum);
        bundle.putString("my_position",my_position);
        bundle.putString("finish_url",finishurl);
        //照片
        bundle.putInt("pack_img",pack_img);
        bundle.putInt("posi_img",posi_img);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getlayout() {
        return R.layout.d_fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SecondActivity.getChildFragmentWebviewCallback(this);
    }

    @Override
    public void initialize() {
        //包裹信息里面的运单编号
        expressnum = (TextView) view.findViewById(R.id.express_num);
        nexttv = (TextView) view.findViewById(R.id.next_tv);
        returnIm_rl = (RelativeLayout) view.findViewById(R.id.returnIm_rl);
        returnIm_rl.setOnClickListener(this);

        nexttv.setOnClickListener(this);

        //手写签名图片
        signim = (ImageView) view.findViewById(R.id.sign_im);
        positionim2 = (ImageView) view.findViewById(R.id.position_im2);
        positionim1 = (ImageView) view.findViewById(R.id.position_im1);
        //包裹信息里面的手机号
        phonenummess = (TextView) view.findViewById(R.id.phone_num_mess);
        //包裹信息里面的签收时间
        receivertimemess = (TextView) view.findViewById(R.id.receiver_time_mess);
        //包裹信息里面的签收地点
        receiverposition = (TextView) view.findViewById(R.id.receiver_position);
        //基本信息里面的签收时间
        receivertime = (TextView) view.findViewById(R.id.receiver_time);
        //基本信息里面的存证标号
        signmd5 = (TextView) view.findViewById(R.id.sign_md5);
        //基本信息里面的手机号码
        phonenum = (TextView) view.findViewById(R.id.phone_num);
        //包裹信息里面的收货人
        receivername = (TextView) view.findViewById(R.id.receiver_name);

        //记录录音时长
        tv_time = (TextView) view.findViewById(R.id.tv_time);

        returnIm = (ImageView) view.findViewById(R.id.returnIm);
        returnIm.setOnClickListener(this);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv11 = (TextView) view.findViewById(R.id.tv11);

        record_contentLayout= (LinearLayout) view.findViewById(R.id.record_contentLayout);
        recordDetailView= (ImageView) view.findViewById(R.id.record_detailView);
        record_contentLayout.setOnClickListener(this);
        signim.setOnClickListener(this);
        positionim1.setOnClickListener(this);
        positionim2.setOnClickListener(this);
        phonenum.setOnClickListener(this);
        tv1.setOnClickListener(this);


        if (getArguments()!=null){
            String express_num = getArguments().getString("express_num");
            String receiver_name = getArguments().getString("receiver_name");
            String phone_num = getArguments().getString("phone_num");
            String receiver_mess = getArguments().getString("receiver_mess");
            String receiver_time = getArguments().getString("receiver_time");
            String duration = getArguments().getString("duration");
            if (duration.equals("")){
                record_contentLayout.setVisibility(View.GONE);
                tv11.setVisibility(View.GONE);

            }
            String md5sign_num = getArguments().getString("md5sign_num");
            String my_position = getArguments().getString("my_position");
            int pack_img = getArguments().getInt("pack_img");
            int posi_img = getArguments().getInt("posi_img");
            signmd5.setText(md5sign_num);

            receiverposition.setText(my_position);
            receivername.setText(receiver_name+"/"+"("+receiver_mess+")");
            phonenum.setText(phone_num);
            phonenummess.setText(phone_num);
            receivertime.setText(receiver_time);
            receivertimemess.setText(receiver_time);
            expressnum.setText(express_num);
            tv_time.setText(duration+"s");

            //图片
            if (imagepath1.exists()&&pack_img!=View.GONE) {
                Bitmap bitmap1 = BitmapFactory.decodeFile(imagepath1.getPath());
                positionim1.setImageBitmap(bitmap1);
                positionim1.setPadding(0,0,0,0);
            }
             if (imagepaht2.exists()&&posi_img!=View.GONE){
                Bitmap bitmap2 = BitmapFactory.decodeFile(imagepaht2.getPath());
                 positionim2.setImageBitmap(bitmap2);
                 positionim2.setPadding(0,0,0,0);
            }

            if (imagepath3.exists()){
                Bitmap bitmap3 = BitmapFactory.decodeFile(imagepath3.getPath());
                signim.setImageBitmap(bitmap3);
                signim.setPadding(0,0,0,0);
            }

        }
        SecondActivity.getChildFragmentWebviewCallback(this);


    }

    public void delete(){
        if (imagepath1.exists()) {
            MakephotoUtils.deleimage(getActivity(), "package_img.png");
        }
        if (imagepath3.exists()) {
            MakephotoUtils.deleimage(getActivity(), "personal_sign.png");
        }
        if (imagepaht2.exists()) {
            MakephotoUtils.deleimage(getActivity(), "position_img.png");
        }
    }



    @Override
    public void onDestroy() {

        //addonesignmessagelistener.addonesignmessagecallback();

        //使用popBackStackImmediate()的这个回报错，但是不在ondestory方法中使用就不会报错 4.18（比如自己写的按钮里面）
        //下面这个方法就是退出回退栈中所有的方法 4.18
        delete();
        getFragmentManager().popBackStack(null,1);

        super.onDestroy();

    }


    @Override
    public void onClick(View v) {
        int pack_img = getArguments().getInt("pack_img");
        int posi_img = getArguments().getInt("posi_img");
        switch (v.getId()){
            case R.id.record_contentLayout:

                File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");

                File audioFilePath=new File(appDir,"message_voice.mp3");

                if (TextUtils.isEmpty(audioFilePath.getPath()) || !audioRecorder) {
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
                player.start(audioFilePath.getPath(), new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        animationDrawable.stop();
                        //下面这个是播放结束时候的动画 3.29
                        recordDetailView.setImageResource(R.drawable.gxx);
                    }
                });

                break;

            case R.id.next_tv:

                //这里直接调用 ondestory会走两次接口，原因未知？ 4.20
                //onDestroy();

                getFragmentManager().popBackStack(null,1);
                break;


            //返回按钮左上角
            case R.id.returnIm_rl:

            case R.id.returnIm:

                getFragmentManager().popBackStack(null,1);

                break;

            case R.id.sign_im:
                Photoview("personal_sign.png");
                break;

            case R.id.position_im1:
                if (pack_img!=View.GONE) {
                    Photoview("package_img.png");
                }
                break;

            case R.id.position_im2:
                if (posi_img!=View.GONE) {
                    Photoview("position_img.png");
                }
                break;

            case R.id.phone_num:
                //拨打电话
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phonenummess.getText().toString().trim()));
                startActivity(intent);
                break;

            case R.id.tv1:
                String finish_url = getArguments().getString("finish_url");
                MenuItemFragment fragment1 = MenuItemFragment.newInstance(finish_url,true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fy,fragment1)
                        .addToBackStack(null)
                        .commit();

//                Intent intent1 = new Intent(getActivity(), WebviewtoNewActivity.class);
//                intent1.putExtra("jump_url", finish_url);
//                startActivity(intent1);
                break;


        }



        }


    private void Photoview(String photoname) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
        File path = new File(appDir, photoname);
        if (photoname != null&&path.exists()) {
            Intent intent = new Intent(getActivity(), PhotoviewActivity.class);
            intent.putExtra("photoname", photoname);
            startActivity(intent);
        }
    }

    }

