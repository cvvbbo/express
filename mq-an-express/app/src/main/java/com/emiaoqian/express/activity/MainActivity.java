package com.emiaoqian.express.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emiaoqian.express.R;
import com.emiaoqian.express.fragment.MyDiaologFragment;
import com.emiaoqian.express.fragment.SigntvDialogFragment;
import com.emiaoqian.express.interfaces.Myinterface;
import com.emiaoqian.express.interfaces.Myinterface2;
import com.emiaoqian.express.utils.AudioPlayerUtil;
import com.emiaoqian.express.utils.AudioRecorderUtil;
import com.emiaoqian.express.utils.PopupWindowFactory;
import com.emiaoqian.express.utils.TimeUtils;
import com.emiaoqian.express.views.BadgeView;
import com.emiaoqian.express.views.FlowLayout;
import com.emiaoqian.express.views.MyImageview;
import com.emiaoqian.express.views.MydialogOnbutton;
import com.emiaoqian.express.views.XzhengRelativelayout;
import com.google.zxing.client.android.CaptureActivity;
import com.umeng.message.PushAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import testcontacts.com.mylibrary.DatePicker.DatePickerView2;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyDiaologFragment.InputListener,
        Myinterface.DismessSmallDialog, Myinterface2.deleteimage {

    @BindView(R.id.tag_vessel)
    FlowLayout tag_vessel;
    List<String> mTagList = new ArrayList<String>();
    int screenWidth = 0;

    @BindView(R.id.add_tag)
    ImageView add_tag;
    @BindView(R.id.front_id)
    ImageView frontId;
    @BindView(R.id.reverse_id)
    ImageView reverseId;
    @BindView(R.id.recordBtn)
    ImageView recordBtn;
    @BindView(R.id.record_detailView)
    ImageView recordDetailView;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.record_contentLayout)
    LinearLayout record_contentLayout;
    @BindView(R.id.next_tv)
    TextView nextTv;
    @BindView(R.id.send_time)
    TextView sendTime;
    @BindView(R.id.scan_qr)
    ImageView scanQr;
    @BindView(R.id.express_input)
    EditText expressInput;
    @BindView(R.id.ook)
    TextView ook;
    @BindView(R.id.front_id1)
    ImageView frontId1;
    @BindView(R.id.reverse_id1)
    ImageView reverseId1;
    @BindView(R.id.myimage)
    MyImageview myimage;

    //添加标签的名字 3.29
    private String newString;

    /***拍照相关***/
    //从相册中获得的bitmap对象
    private Bitmap albumbitmap_frontId;

    //照片的名字
    private String photoname1;

    private Bitmap makephotobitmap_frontId;
    private Bitmap makephotobitmap_reverseId;
    private Bitmap albumbitmap_reverseId;

    private static boolean zheng = true;

    private XzhengRelativelayout ReadXZ;

    /*****拍照相关***/

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
    View view;
    //  LinearLayout record_contentLayout;
    //   ImageView recordDetailView;
    private String audioFilePath;// 录音文件保存路径
    private AnimationDrawable animationDrawable;

    //写字板
    private SigntvDialogFragment signtvDialogFragment;
    private int QRCode_Action = 666;
    //private XzhengRelativelayout mTag;

    /**
     * 录音相关
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushAgent.getInstance(this).onAppStart();
        ButterKnife.bind(this);


        Log.e("66onCreate", "MainActivity");

        landscapeSignboardActivity.setDismessSmallDialogCallback(this);

        //这个只是拍照的权限，之后还有录音的权限 3.30
        makepermission();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        init(this);
        initAudioRecorderBtn();

        add_tag.setOnClickListener(this);

        //开始固定的标签
        mTagList.add("本人");
        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1);

        mTagList.add("同事/同学");
        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1);

        mTagList.add("家人");
        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1);

        mTagList.add("前台");
        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1);

        frontId.setOnClickListener(this);
        reverseId.setOnClickListener(this);
        nextTv.setOnClickListener(this);
        sendTime.setOnClickListener(this);
        scanQr.setOnClickListener(this);
        ook.setOnClickListener(this);

        XzhengRelativelayout.setDeleteimagecallback(this);

        MyDiaologFragment.setOninputListener(this);
        add_tag.setVisibility(View.VISIBLE);

        //真正的图片
        frontId1.setOnClickListener(this);
        reverseId1.setOnClickListener(this);
        myimage.setOnClickListener(this);


    }

    //放大图片的跳转 3.29
    private void Photoview(String photoname) {
        if (photoname != null) {
            Intent intent = new Intent(MainActivity.this, PhotoviewActivity.class);
            intent.putExtra("photoname", photoname);
            startActivity(intent);
        }
    }


//    private void showbadgeview(final ImageView imageView, final int select) {
//        final BadgeView badgeView = new BadgeView(MainActivity.this, imageView);
//        badgeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageView.setImageBitmap(null);
//                //imageView.setBackgroundResource(0);
//                imageView.setImageResource(R.drawable.up_identify);
//                /**记得这里还有关键的一步就是把照片删了 3.26
//                 *
//                 *
//                 */
//                switch (select) {
//                    case 1:
//                        makephotobitmap_frontId = null;
//                        break;
//                    case 2:
//                        makephotobitmap_reverseId = null;
//                        break;
//                    case 3:
//                        albumbitmap_frontId = null;
//                        break;
//                    case 4:
//                        albumbitmap_reverseId = null;
//                        break;
//                }
//
//                badgeView.toggle();
//            }
//        });
//        badgeView.setText("X");
//        badgeView.show();
//    }

    /**
     * @param imageView 这个是隐藏的假图片，并不是真图片,这个imageview就不会发生偏移，但也只是在没有margin的时候，有margin的时候还是会偏移
     * @param select
     */
    private void showbadgeview(final ImageView imageView, final int select) {
        final BadgeView badgeView = new BadgeView(MainActivity.this, imageView);
        badgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageView.setImageBitmap(null);
//                //imageView.setBackgroundResource(0);
//                imageView.setImageResource(R.drawable.up_identify);
//                /**记得这里还有关键的一步就是把照片删了 3.26
//                 *
//                 *
//                 */
//                switch (select) {
//                    case 1:
//                        makephotobitmap_frontId = null;
//                        break;
//                    case 2:
//                        makephotobitmap_reverseId = null;
//                        break;
//                    case 3:
//                        albumbitmap_frontId = null;
//                        break;
//                    case 4:
//                        albumbitmap_reverseId = null;
//                        break;
//                }

                //注意最后要把这个尾巴隐藏掉，试试直接隐藏图片，不隐藏这个标会怎么样
                // badgeView.toggle();

                frontId.setVisibility(View.VISIBLE);
                frontId1.setVisibility(View.GONE);
                badgeView.hide();


            }
        });
        badgeView.setText("X");
        badgeView.show();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.add_tag:

                // 添加标签
                AddTagDialog();
                break;

            //正面上传 分别是从相机中上传和从相册中上传
            case R.id.front_id:
//                if (makephotobitmap_frontId != null) {
//                    Photoview("package_img.png");
//                    return;
//                } else if (albumbitmap_frontId != null) {
//                    Photoview("package_img.png");
//                    return;
//                }

                //还要添加个dialog。。
                photoname1 = "package_img.png";
                MydialogOnbutton mydialog = new MydialogOnbutton(MainActivity.this);
                zheng = true;
                mydialog.show();
                break;

            //背面上传，分别是从相册中获取和从相机中获取 3.30
            case R.id.reverse_id:
                if (makephotobitmap_reverseId != null) {
                    Photoview("position_img.png");
                    return;
                } else if (albumbitmap_reverseId != null) {
                    Photoview("position_img.png");
                    return;
                }
                photoname1 = "position_img.png";
                MydialogOnbutton mydialog1 = new MydialogOnbutton(MainActivity.this);
                zheng = false;
                mydialog1.show();
                break;

            case R.id.next_tv:
                signtvDialogFragment = new SigntvDialogFragment();
                signtvDialogFragment.show(getSupportFragmentManager(), "signtvDialogFragment");
                break;

            case R.id.send_time:

                DatePickerView2 pickerView = new DatePickerView2(MainActivity.this, new DatePickerView2.DatePickerListener() {
                    @Override
                    public void dateChange(String s) {
                        sendTime.setText(s);

                    }

                    @Override
                    public void finish(String s) {
                        sendTime.setText(s);

                    }
                });
                pickerView.setFromYearAndToYear(1900, 2016);
                pickerView.initDate(2016, 3, 9);
                pickerView.show();

                break;

            case R.id.myimage:

                photoname1 = "position_img.png";
                MydialogOnbutton mydialog2 = new MydialogOnbutton(MainActivity.this);
                zheng = false;
                mydialog2.show();


                break;

            case R.id.front_id1:
                if (makephotobitmap_frontId != null) {
                    Photoview("package_img.png");
                    return;
                } else if (albumbitmap_frontId != null) {
                    Photoview("package_img.png");
                    return;
                }


                break;

            case R.id.reverse_id1:

                break;

            case R.id.scan_qr:

                startActivityForResult(new Intent(this, CaptureActivity.class), QRCode_Action);

                break;

            case R.id.ook:

//                long l = System.currentTimeMillis();
//                String str = String.valueOf(l);
//                //记得如果要修改的话javatojson这个方法里面也是要修改的 1.17
//                final String s = EncodeBuilder.javaToJSONsign("84",str,"6905273029212","17600710151","3","1523322999","xz");
//                final String sign = EncodeBuilder.newStringUpfile(s);
//
//                File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
//                File file1 = new File(appDir, "package_img.png");
//                File file2 = new File(appDir, "position_img.png");
//                File file3 = new File(appDir, "personal_sign.png");
//                File mp3file2 = new File(appDir, "message_voice.mp3");
//                String[] files=new String[]{file1.getPath(),file2.getPath(),file3.getPath(),mp3file2.getPath()};
//
//                for (String a:files){
//                    LogUtil.e("--文件的名字是--"+a);
//                }
//
//                LogUtil.e("--地址是--"+ Constants.sian_save);
//                httphelper.create().upImage(Constants.sian_save, files,
//                        "84", "6905273029212", "17600710151", "1523322999", "xz", "3", str, sign, new httphelper.httpcallback() {
//                            @Override
//                            public void success(String s) {
//                                LogUtil.logE("hava data？"+s);
//
//                            }
//
//                            @Override
//                            public void fail(Exception e) {
//                                LogUtil.e("fails data"+e);
//
//                            }
//                        });
//

                break;

        }

    }


    /**
     * 添加标签的对话框
     */
    public void AddTagDialog() {
        MyDiaologFragment myDiaolog = new MyDiaologFragment();
        myDiaolog.show(getSupportFragmentManager(), "haha");
    }


    //动态设置shape
    private Drawable getBack() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(20);
        drawable.setColor(Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
        return drawable;
    }

    /**
     * 添加标签
     *
     * @param tag （第一个参数是取出添加的参数，第二个参数是记录位置的）
     * @param i
     */
    @SuppressLint("NewApi")
    public void AddTag(String tag, int i) {

        final XzhengRelativelayout mTag = new XzhengRelativelayout(this);
        //mTag.setText("  " + tag + "  ");
        mTag.setText(" " + tag + " ");
        mTag.setTextsize(16);
        mTag.setTextTextColor(Color.BLACK);

        //这个是修改背景的大小的，就是当字体变大的时候修改底部的背景
        // 最开始的时候字体的大小是16 的时候 ，这个宽是warp_content，然后height是40dp 4.2          高度 原来是55 4.11
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //这个是设置flowlayout和外面布局的margin，就像linearlayout设置margin一样  4.2
        //params.setMargins(10, 10, 10, 10);

        int pixelSize = getResources().getDimensionPixelSize(R.dimen.x4);
        params.setMargins(pixelSize, 0, 0, 0);


        tag_vessel.addView(mTag, i, params);


        mTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //view.Gone对应的int值是 8
                //这一步就是把x号标签给隐藏起来
                if (showredcircle.isshowcallback() != 8) {
                    showredcircle.hidecallback();
                }

                // Toast.makeText(MainActivity.this, mTag.getText().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, mTag.getTextTostring(), Toast.LENGTH_SHORT).show();
            }

        });


        //长按删除
        mTag.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                if (mTagList.size() < 4) {
                    return true;
                }

                //当长点击的是前面几个的时候，不进行任何操作 4.3
                if (mTag.getTextTostring().equals("本人") || mTag.getTextTostring().equals("家人")
                        || mTag.getTextTostring().equals("前台") || mTag.getTextTostring().equals("同事/同学")) {
                    return true;
                }

                ReadXZ = mTag;

                ImageView imageview = mTag.getImageview();
                imageview.setVisibility(View.VISIBLE);

                //delectimagecallback(mTag);


                //长按显示这个左上角的x号图标 4.3
                //showredcircle.showredcirclecallback();

                add_tag.setVisibility(View.VISIBLE);


                return true;
            }
        });
    }

    //添加标签的接口，检查值
    @Override
    public void onInputComplete(String text) {
        newString = text;
        mTagList.add(newString.toString());
        if (mTagList.size() > 8) {
            add_tag.setVisibility(View.GONE);
        }
        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1);


//        else {
//            add_tag.setVisibility(View.VISIBLE);
//        }

    }

    //压缩图片的方法
    public Bitmap compressImage(String filepath) {
        int height = getWindowManager().getDefaultDisplay().getHeight();
        int width = getWindowManager().getDefaultDisplay().getWidth();
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        width = p.x;
        height = p.y;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //下面这个是获取不到大小的，因为加载进内存的大小为0
        Bitmap bitmap = BitmapFactory.decodeFile(filepath);
        BitmapFactory.decodeFile(filepath, options);
        //Log.e("--压缩之前", bitmap.getByteCount() + " ");
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        int index = 1;
        if (outHeight > height || outWidth > width) {
            float heightRate = outHeight / height;
            float widthrate = outHeight / width;

            index = (int) Math.max(heightRate, widthrate);
        }
        options.inSampleSize = index;
        options.inJustDecodeBounds = false;
        Bitmap afterbitmap = BitmapFactory.decodeFile(filepath, options);
        //Log.e("--压缩之后", afterbitmap.getByteCount() + " ");
        return afterbitmap;
    }

    //保存图片，这个是保存bitmap的图片从相册
    public File saveImageofalbum(Context m, Bitmap bmp, String photoname) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        String fileName = photoname;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //100%品质就是不会压缩
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库(网上的大部分方案还需要这个 3.27)
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //因为这个方法是为了临时保存的，所以看看不刷新会怎么样
        m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));

        return file;

    }

    //6.0的动态权限
    public void makepermission() {
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 222);
        }
    }

    //保存图片，这个是保存bitmap的图片。
    public void saveImage(Context m, Bitmap bmp, String photoname) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = photoname;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //100%品质就是不会压缩，这里只是把图片保存到另一个地方
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));
    }


    //仿扣扣录音相关 3.30
    public void init(Context context) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        ROOT_PATH = appDir.getPath();
    }

    //这个是按下录音时候的播放效果
    private void initAudioRecorderBtn() {
        view = View.inflate(this, R.layout.layout_microphone, null);
        mPop = new PopupWindowFactory(this, view);
        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);

        mImageView.setImageResource(R.drawable.frame2);
        animationDrawable = (AnimationDrawable) mImageView.getDrawable();
        animationDrawable.start();


        final AudioRecorderUtil audioRecorderUtil = new AudioRecorderUtil(ROOT_PATH);
        audioRecorderUtil.setOnAudioStatusUpdateListener(new AudioRecorderUtil.OnAudioStatusUpdateListener() {
            @Override
            public void onStart() {

                record_contentLayout.setVisibility(View.GONE);
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
                        audioRecorderUtil.start();
                        mPop.showAtLocation(view.getRootView(), Gravity.CENTER, 0, 0);


                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        long time = audioRecorderUtil.getSumTime();
                        if (time < 1000) {
                            audioRecorderUtil.cancel();
                            Toast.makeText(MainActivity.this, "录音时间太短！", Toast.LENGTH_SHORT).show();
                            mImageView.getDrawable().setLevel(0);
                            mTextView.setText(TimeUtils.long2String(0));
                            audioRecorderUtil.stop();
                            mPop.dismiss();
                            record_contentLayout.setVisibility(View.GONE);
                            audioFilePath = "";
                            return true;
                        } else {
                            tv_time.setText(time / 1000 + "s");
                        }
                        mImageView.getDrawable().setLevel(0);
                        mTextView.setText(TimeUtils.long2String(0));
                        audioRecorderUtil.stop();
                        mPop.dismiss();
                        record_contentLayout.setVisibility(View.VISIBLE);
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

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.stop();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //防止什么也没干，异常报错 4.13
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    //设置图片这里肯定不能写死，得写个判断的！！而且这里很精髓
                    if (zheng) {

                        makephotobitmap_frontId = compressImage(new File(Environment.getExternalStorageDirectory(), "image.jpg").getPath());
                        saveImage(MainActivity.this, makephotobitmap_frontId, photoname1);
                        showbadgeview(frontId, 1);
                        frontId.setImageBitmap(makephotobitmap_frontId);
                    } else {
                        makephotobitmap_reverseId = compressImage(new File(Environment.getExternalStorageDirectory(), "image.jpg").getPath());
                        saveImage(MainActivity.this, makephotobitmap_reverseId, photoname1);

                        showbadgeview(reverseId, 2);
                        reverseId.setImageBitmap(makephotobitmap_reverseId);
                    }

                    //如果是bitmap里面不等于空，然后转跳Activity


                    break;

                //从相册中获取
                case 102:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址，这样获取并不是压缩过的，如果拍照的图片也是这样获取就是压缩过的！！
                    Uri originalUri = data.getData();

                    //使用ContentProvider通过URI获取原始图片
                    try {
                        //下面这个是原始图片的bitmap，但是并不在本应用的文件夹下面
                        //这里就是成员变量


                        //先放图片，然后在保存，很骚。。。，这样展示就快了

                        //这里不能相册就放在背面
                        if (zheng) {

                            albumbitmap_frontId = MediaStore.Images.Media.getBitmap(resolver, originalUri);

                            frontId.setVisibility(View.GONE);
                            frontId1.setVisibility(View.VISIBLE);
                            showbadgeview(frontId1, 3);

                            //frontId.setImageResource(0);
                            frontId1.setImageBitmap(albumbitmap_frontId);


                            new Thread() {
                                @Override
                                public void run() {
                                    // 第一次保存时将系统的保存在自己的文件夹中
                                    File file = saveImageofalbum(MainActivity.this, albumbitmap_frontId, photoname1);
                                    //最后压缩保存
                                    Bitmap afterbitmap = compressImage(file.getPath());

                                    //这个是最终保存的的
                                    saveImage(MainActivity.this, afterbitmap, photoname1);

                                }
                            }.start();

                        } else {

                            albumbitmap_reverseId = MediaStore.Images.Media.getBitmap(resolver, originalUri);
//                            showbadgeview(reverseId, 4);
//                            reverseId.setImageBitmap(albumbitmap_reverseId);

                            //首先展示出关闭
//                            myimage.Getsmallim().setVisibility(View.VISIBLE);
//                            //把假图先隐藏了
//                            myimage.Getbigim().setVisibility(View.INVISIBLE);
//                            myimage.Getrealim().setVisibility(View.VISIBLE);
//                            //为真的背景设置图片
//                            myimage.Getrealim().setImageBitmap(albumbitmap_reverseId);
//                            myimage.Getsmallim().setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    myimage.Getrealim().setVisibility(View.GONE);
//                                    myimage.Getbigim().setVisibility(View.VISIBLE);
//                                    myimage.Getsmallim().setVisibility(View.GONE);
//                                }
//                            });


                            //这个保存有点耗时，差不多10秒，才完成，先放在子线程中，现在主线程中
                            new Thread() {
                                @Override
                                public void run() {
                                    // 第一次保存时将系统的保存在自己的文件夹中
                                    File file = saveImageofalbum(MainActivity.this, albumbitmap_reverseId, photoname1);
                                    //最后压缩保存
                                    Bitmap afterbitmap = compressImage(file.getPath());

                                    //这个是最终保存的的
                                    saveImage(MainActivity.this, afterbitmap, photoname1);

                                }
                            }.start();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                case 666:

                    String qrcode = data.getStringExtra("qrcode");
                    expressInput.setText(qrcode);


                    break;
            }
        }
    }

    @Override
    protected void onPause() {

        Log.e("66onPause", "MainActivity");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.e("66onResume", "MainActivity");
        super.onResume();
    }

    @Override
    public void DismessSmallDialogcallback() {
        signtvDialogFragment.dismiss();

    }

    public static Myinterface2.showredcircle showredcircle;

    public static void setShowredcircle(Myinterface2.showredcircle showredcircle) {
        MainActivity.showredcircle = showredcircle;

    }


    @Override
    public void delectimagecallback(XzhengRelativelayout tv) {

        //标签消失
        //

//        tag_vessel.removeView(mTag);
//        for (int j = 0; j < mTagList.size(); j++) {
//            Log.v("==", mTag.getTextTostring() + "==" + mTagList.get(j).toString());
//            if (mTag.getTextTostring().replaceAll(" ", "")
//                    .equals(mTagList.get(j).toString().replaceAll(" ", ""))) {
//                mTagList.remove(j);
//            }
//        }


        tag_vessel.removeView(tv);
        for (int j = 0; j < mTagList.size(); j++) {
            Log.v("==", tv.getTextTostring() + "==" + mTagList.get(j).toString());
            if (tv.getTextTostring().replaceAll(" ", " ")
                    .equals(mTagList.get(j).toString().replaceAll(" ", " "))) {
                mTagList.remove(j);
            }
        }


    }


}
