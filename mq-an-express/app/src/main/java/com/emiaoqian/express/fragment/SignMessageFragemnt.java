package com.emiaoqian.express.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emiaoqian.express.R;
import com.emiaoqian.express.activity.MainActivity;
import com.emiaoqian.express.activity.SecondActivity;
import com.emiaoqian.express.adapter.TraceListAdapter;
import com.emiaoqian.express.application.MyApplication;
import com.emiaoqian.express.bean.CodeDatabean;
import com.emiaoqian.express.bean.ExpressBean;
import com.emiaoqian.express.bean.GetCodebean;
import com.emiaoqian.express.bean.LoginBean;
import com.emiaoqian.express.bean.Trace;
import com.emiaoqian.express.interfaces.Myinterface2;
import com.emiaoqian.express.utils.Constants;
import com.emiaoqian.express.utils.EncodeBuilder;
import com.emiaoqian.express.utils.GsonUtil;
import com.emiaoqian.express.utils.IatSettings;
import com.emiaoqian.express.utils.JsonParser;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.ToastUtil;
import com.emiaoqian.express.utils.httphelper;
import com.emiaoqian.express.utils.sharepreferenceUtils;
import com.emiaoqian.express.views.FlowLayout;
import com.emiaoqian.express.views.FlowLayout1;
import com.emiaoqian.express.views.XiongListview;
import com.emiaoqian.express.views.XiongProgressDialog;
import com.emiaoqian.express.views.XzhengRelativelayout;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.client.android.CaptureActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.value;

/**
 * Created by xiong on 2018/4/13.
 * <p>
 * https://www.cnblogs.com/laishenghao/p/5157914.html
 */

public class SignMessageFragemnt extends BaseFragment implements View.OnClickListener,
        MyDiaologFragment.InputListener, Myinterface2.deleteimage {


    private int selectinput=0;
    private boolean mTranslateEnable = false;
    private SpeechRecognizer mIat;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    int ret = 0; // 函数调用返回值

    private SharedPreferences mSharedPreferences;

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    ////////////////以上科大讯飞语音


    //扫描二维码 4.25
    private static final int QRCode_Action = 666;

    private static final int REQUEST_CODE = 233;
    private ImageView returnIm;
    private ImageView scanqr;
    private EditText expressnum;
    private ImageView clear_im;
    ImageView add_tag;

    FlowLayout1 tag_vessel;

    //标签的数组（如果以后有自定义的要传进来，不能固定写死了） 4.21
    ArrayList<String> mTagList = new ArrayList<String>();


    //这个是订单详情的下拉listview
    private List<Trace> traceList = new ArrayList<>();
    private TraceListAdapter adapter;

    private ArrayList<String> expresstime=new ArrayList<>();
    private ArrayList<String> expressdis=new ArrayList<>();


    //添加标签的名字 3.29
    private String newString;
    private TextView next_tv;
    private EditText phone_num;
    private EditText receiver_name;


    private String selectname = "";
    private XiongListview express_detailed_list;

    private float mDensity;
    private int mFoldedViewMeasureHeight;
    private ImageView im1;

    private boolean isFold = false;//是否是收起状态
    boolean isAnimating = false;//是否正在执行动画
    private RelativeLayout express_content_more;
    private ImageView clear_im1;
    //private String acceptStation;
    //private String acceptTime;
    //private List<ExpressBean.DataBean> detail;

    private HashMap<String,String> addNewTip=new HashMap<>();
    private ImageView mico_phone;
    private ImageView miconame;

    //IdentityHashMap<String,Object> addNewTip =new IdentityHashMap<String,Object>();

    String[] permission = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};//需要的权限
    String dialogtitle = "录音权限权限不可用";
    String content = "为了保证语音正常使用,需要开启麦克风权限\n否则，您将无法正常使用语音功能";
    private RelativeLayout returnIm_rl;


    @Override
    public int getlayout() {
        return R.layout.b_fragment;
    }

    @Override
    public void onResume() {
        SecondActivity.getChildFragmentWebviewCallback(SignMessageFragemnt.this);
        super.onResume();
    }

    @Override
    public void initialize() {

        mIat = SpeechRecognizer.createRecognizer(getActivity(), mInitListener);
        expressnum = (EditText) view.findViewById(R.id.express_num);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            String qrcode = arguments.getString("qrcode");
            expressnum.setText(qrcode);
        }




        //订单详情的listview
        express_detailed_list = (XiongListview) view.findViewById(R.id.express_detailed_list);

        express_content_more = (RelativeLayout) view.findViewById(R.id.express_content_more);

        clear_im = (ImageView) view.findViewById(R.id.clear_im);
        //科大讯飞语音相关，详见科大讯飞开发平台
        mSharedPreferences = getActivity().getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);
        mico_phone = (ImageView) view.findViewById(R.id.micophone);
        phone_num = (EditText) view.findViewById(R.id.phone_num);
        mico_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //申请语音权限
                initPermission(permission,dialogtitle,content);
                selectinput=1;
                FlowerCollector.onEvent(getActivity(), "iat_recognize");

                phone_num.setText(null);// 清空显示内容
                mIatResults.clear();
                setParam();

                //不显示对话框
                ret = mIat.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    ToastUtil.showToast("听写失败,错误码：" + ret);
                } else {
                    ToastUtil.showToast("请开始说话");
                }
            }
        });


        clear_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressnum.setText("");
            }
        });

       // GetNetInfor();
       // initData();
        im1 = (ImageView) view.findViewById(R.id.im1);

        //点击按钮展开listview 4.24
        showIbtn();
        // 可伸缩的listview 4.24
        CanScrollListview();

        returnIm_rl = (RelativeLayout) view.findViewById(R.id.returnIm_rl);
        returnIm = (ImageView) view.findViewById(R.id.returnIm);
        scanqr = (ImageView) view.findViewById(R.id.scan_qr);
        clear_im1 = (ImageView) view.findViewById(R.id.clear_im);
        add_tag = (ImageView) view.findViewById(R.id.add_tag);
        next_tv = (TextView) view.findViewById(R.id.next_tv);
        next_tv.setOnClickListener(this);
        returnIm.setOnClickListener(this);
        scanqr.setOnClickListener(this);
        returnIm_rl.setOnClickListener(this);


        tag_vessel = (FlowLayout1) view.findViewById(R.id.tag_vessel);
        receiver_name = (EditText) view.findViewById(R.id.receiver_name);

        miconame = (ImageView) view.findViewById(R.id.miconame);
//        miconame.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                initPermission(permission,dialogtitle,content);
//
//                return true;
//            }
//        });
        miconame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //申请语音权限
                initPermission(permission,dialogtitle,content);
                selectinput=2;
                FlowerCollector.onEvent(getActivity(), "iat_recognize");

                receiver_name.setText(null);// 清空显示内容
                mIatResults.clear();
                setParam();

                //不显示对话框
                ret = mIat.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    ToastUtil.showToast("听写失败,错误码：" + ret);
                } else {
                    ToastUtil.showToast("请开始说话");
                }

            }
        });

        add_tag.setOnClickListener(this);

        //和获取验证码是相同的接口 4.25(获取标签)
        GetUserTip();


        //这个接口要改，添加标签的时候，添加成功时候返回一个标签的id，然后再用android的设置tag的操作4.25
        //AddTipToNet("lalalla");

        //开始固定的标签  4.24
//        mTagList.add("本人");
//        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1,"1");
//
//        mTagList.add("同事/同学");
//        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1,"2");
//
//        mTagList.add("家人");
//        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1,"3");
//
//        mTagList.add("前台");
//        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1,"4");
//
//        mTagList.add("门卫");
//        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1,"5");
//
//        mTagList.add("物业");
//        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1,"6");
//
//        mTagList.add("超市");
//        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1,"7");

        MyDiaologFragment.setOninputListener(this);

        XzhengRelativelayout.setDeleteimagecallback(this);

    }




    //科大讯飞相关参数
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        if (selectinput==1) {
            phone_num.setText(resultBuffer.toString().substring(0, resultBuffer.toString().length() - 1));
            //设置光标的位置
            phone_num.setSelection(phone_num.length());
        }else if (selectinput==2){
            receiver_name.setText(resultBuffer.toString().substring(0, resultBuffer.toString().length() - 1));
            //设置光标的位置
            receiver_name.setSelection(receiver_name.length());
        }
    }

    //科大讯飞相关参数
    private void printTransResult (RecognizerResult results) {
        String trans  = JsonParser.parseTransResult(results.getResultString(),"dst");
        String oris = JsonParser.parseTransResult(results.getResultString(),"src");

        if( TextUtils.isEmpty(trans)||TextUtils.isEmpty(oris) ){
            ToastUtil.showToast( "解析结果失败，请确认是否已开通翻译功能。" );
        }else{
            if (selectinput==1) {
                phone_num.setText("原始语言:\n" + oris + "\n目标语言:\n" + trans);
            }else if (selectinput==2){
                receiver_name.setText("原始语言:\n" + oris + "\n目标语言:\n" + trans);
            }
        }

    }

    /**
     * 听写监听器。(科大讯飞语音接口)
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        int errorcode=502;

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            ToastUtil.showToast("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            if(mTranslateEnable && error.getErrorCode() == 14002) {
                ToastUtil.showToast( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
               ToastUtil.showToast(error.getPlainDescription(true));
                errorcode=error.getErrorCode();
            }
        }

        @Override
        public void onEndOfSpeech() {
            if (errorcode == 502){
                // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
                ToastUtil.showToast("结束说话");
        }

        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d("haha", results.getResultString());
            if( mTranslateEnable ){
                printTransResult( results );
            }else{
                printResult(results);
            }

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //ToastUtil.showToast("当前正在说话，音量大小：" + volume);
            Log.d("haha", "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    //科大讯飞语音参数
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        this.mTranslateEnable = mSharedPreferences.getBoolean( this.getString(R.string.pref_key_translate), false );
        if( mTranslateEnable ){
            Log.i( "111", "translate enable" );
            mIat.setParameter( SpeechConstant.ASR_SCH, "1" );
            mIat.setParameter( SpeechConstant.ADD_CAP, "translate" );
            mIat.setParameter( SpeechConstant.TRS_SRC, "its" );
        }

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            if( mTranslateEnable ){
                mIat.setParameter( SpeechConstant.ORI_LANG, "en" );
                mIat.setParameter( SpeechConstant.TRANS_LANG, "cn" );
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if( mTranslateEnable ){
                mIat.setParameter( SpeechConstant.ORI_LANG, "cn" );
                mIat.setParameter( SpeechConstant.TRANS_LANG, "en" );
            }
        }
        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
//        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }


    //科大讯飞语音
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("haha", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
               ToastUtil.showToast("初始化失败，错误码：" + code);
            }
        }
    };



    public void AddTipToNet(String Tipname){

        long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        String user_id1 = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        final String s = EncodeBuilder.javaToAddTip(user_id1,str,Tipname);
        final String sign = EncodeBuilder.newString2(s);
        httphelper.create().AddUserTip(Constants.ADD_TIP, Tipname,user_id1, sign, str, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                LogUtil.e("能添加标签么"+s);


            }

            @Override
            public void fail(Exception e) {
                LogUtil.e("--个人信息失败--"+e);

            }
        });

    }


    //和获取短信验证码的接口相同 4.25
    public void GetUserTip(){
        long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        String user_id1 = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", "");
        final String s = EncodeBuilder.javaTouserinfor(user_id1,str);
        final String sign = EncodeBuilder.newString2(s);
        httphelper.create().GetPersonData(Constants.QUERY_TIP, user_id1, sign, str, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                LogUtil.e("--个人标签--"+s);
                GetCodebean getCodebean = GsonUtil.parseJsonToBean(s, GetCodebean.class);
                List<CodeDatabean> data = getCodebean.data;
                for (int i=0;i<data.size();i++) {
                    String id=data.get(i).id;
                    String name = data.get(i).name;
                    mTagList.add(name);
                    AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1,String.valueOf(i));

                    LogUtil.e(data.size() + "-数量-");

                }


            }

            @Override
            public void fail(Exception e) {
                LogUtil.e("--个人信息失败--"+e);

            }
        });
    }

    private void CanScrollListview() {
        //获取像素密度
        mDensity = getResources().getDisplayMetrics().density;
        //获取布局的高度
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        express_detailed_list.measure(w, h);
        int height = express_detailed_list.getMeasuredHeight();

        //这个应该是获取真实的高度
        mFoldedViewMeasureHeight = (int) (mDensity * height + 0.5);

        //快递详情的下拉按钮4.24
        express_content_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnimating)
                    return;
                //如果动画没在执行,走到这一步就将isAnimating制为true , 防止这次动画还没有执行完毕的
                //情况下,又要执行一次动画,当动画执行完毕后会将isAnimating制为false,这样下次动画又能执行
                isAnimating = true;

                if (express_detailed_list.getVisibility() == View.GONE) {
                    GetNetInfor();
                    //打开动画

                } else {
                    //关闭动画
                    animateClose(express_detailed_list);
                }
            }
        });
    }




    //获取快递信息并查询，注意双重循环里面的i和j不要写错了！！！！4.24
    private void GetNetInfor(){
        final String num = expressnum.getText().toString().trim();
        //String num="485829819293";
        final long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        final String s = EncodeBuilder.javaToexpressinfor(num,str);
        final String sign = EncodeBuilder.newString2(s);
        final Dialog mDialog = XiongProgressDialog.createLoadingDialog(getActivity(), "请稍后");
        mDialog.show();//显示

       // httphelper.create().GetExpressData("https://blog.csdn.net/android_freshman/article/details/51364272", num, sign, str, new httphelper.httpcallback() {
        httphelper.create().GetExpressData(Constants.GET_EXPRESS_INFOR, num, sign, str, new httphelper.httpcallback() {
            @Override
            public void success(String s) {
                LogUtil.e(num);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (!code.equals("100000")){
                        mDialog.dismiss();
                        ToastUtil.showToastCenter("服务器异常");
                        //重置动画的状态位
                        isAnimating=false;
                        return;
                    }
                    String data = jsonObject.getString("data");
                    if (data!=null){
                        mDialog.dismiss();
                    }
                    LogUtil.e(data);
                    ArrayList<ExpressBean> expressinfo =(ArrayList<ExpressBean>)GsonUtil.
                            parseJsonToList(data,new TypeToken<List<ExpressBean>>(){}.getType());
                    traceList.clear();
                    for (int i=0;i<expressinfo.size();i++) {
                        String day = expressinfo.get(i).day;
                        List<ExpressBean.DataBean> detail = expressinfo.get(i).data;
                        for (int j=0;j<detail.size();j++){
                            String  acceptStation = detail.get(j).AcceptStation;
                            String  acceptTime = detail.get(j).AcceptTime;
                            traceList.add(new Trace(day+" "+acceptTime, acceptStation));

                        }
                    }
                    adapter = new TraceListAdapter(getActivity(), traceList);
                    adapter.notifyDataSetChanged();
                    express_detailed_list.setAdapter(adapter);
                    //应该把打开动画放到这里
                    animateOpen(express_detailed_list);
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showToastCenter("服务器异常");
                    //重置动画的状态位
                    isAnimating=false;
                    mDialog.dismiss();
                }

            }

            @Override
            public void fail(Exception e) {

            }
        });


    }


    @SuppressLint("NewApi")
    public void AddTag(final String tag, int i,String id) {

        final XzhengRelativelayout mTag = new XzhengRelativelayout(getActivity());
        //mTag.setText("  " + tag + "  ");
        mTag.setText(" " + tag + " ");
        mTag.setTextsize(16);
        mTag.setTextTextColor(Color.BLACK);
        mTag.setTag(id);

        //这个是修改背景的大小的，就是当字体变大的时候修改底部的背景
        // 最开始的时候字体的大小是16 的时候 ，这个宽是warp_content，然后height是40dp 4.2          高度 原来是55 4.11
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //这个是设置flowlayout和外面布局的margin，就像linearlayout设置margin一样  4.2
        //params.setMargins(10, 10, 10, 10);

        int pixelSize = getResources().getDimensionPixelSize(R.dimen.x4);
        params.setMargins(0, 0, 0, pixelSize);



        tag_vessel.addView(mTag, i, params);


        mTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //view.Gone对应的int值是 8
                //这一步就是把x号标签给隐藏起来
//                if (showredcircle.isshowcallback() != 8) {
//                    showredcircle.hidecallback();
//                }

                ImageView imageview = mTag.getImageview();
                if (imageview.getVisibility() != View.GONE) {
                    imageview.setVisibility(View.GONE);
                }

                TextView textView = mTag.getTextView();

                //先让全部都变成白色
                for (int i = 0; i < tag_vessel.getChildCount(); i++) {
                    View childAt = tag_vessel.getChildAt(i);
                    if (childAt instanceof XzhengRelativelayout) {

                        XzhengRelativelayout xz = (XzhengRelativelayout) childAt;

                        TextView textView1 = xz.getTextView();
                        textView1.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_tips_n));
                    }
                }

                //被选中的改变颜色
                if (textView.isEnabled()) {
                    textView.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_tips_p));
                } else {
                    textView.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_tips_n));
                }




                // Toast.makeText(MainActivity.this, mTag.getText().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), mTag.getTextTostring(), Toast.LENGTH_SHORT).show();
                selectname = mTag.getTextTostring();
            }

        });


        //长按删除
        mTag.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                if (mTagList.size() < 12) {
                    return true;
                }

                //当长点击的是前面几个的时候，不进行任何操作 4.3
                if (mTag.getTextTostring().equals("本人") || mTag.getTextTostring().equals("家人")
                        || mTag.getTextTostring().equals("前台") || mTag.getTextTostring().equals("同事/同学")
                        || mTag.getTextTostring().equals("门卫") || mTag.getTextTostring().equals("物业")
                        || mTag.getTextTostring().equals("超市")) {
                    return true;
                }


                ImageView imageview = mTag.getImageview();
                imageview.setVisibility(View.VISIBLE);

                //delectimagecallback(mTag);


                //长按显示这个左上角的x号图标 4.3
                //showredcircle.showredcirclecallback();

                /***5.21这里暂时注释掉（这个不添加也不删除）**/
                //add_tag.setVisibility(View.VISIBLE);
                //add_tag.setEnabled(true);


                return true;
            }
        });
    }

    public void AddTagDialog() {
        MyDiaologFragment myDiaolog = new MyDiaologFragment();
        myDiaolog.show(getFragmentManager(), "haha");
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_tag:

                if (mTagList.size() > 12) {
                    // add_tag.setVisibility(View.GONE);
                    //add_tag.setEnabled(false);
                    ToastUtil.showToastCenter("自定义标签达到上限");
                    return;

                }
                AddTagDialog();

                break;
            case R.id.next_tv:

                String num="[1][234567890]\\d{9}";

                //判断运单号是不是正确的
                String num1= "^[A-Za-z0-9]+$";

                String expressnum = this.expressnum.getText().toString().trim();
                String phonenum = phone_num.getText().toString().trim();
                String receivername = receiver_name.getText().toString().trim();
                if (expressnum.equals("")) {

                    ToastUtil.showToastCenter("运单号为空");
                    return;
                }

                if(expressnum.matches(num1)==false){

                    ToastUtil.showToastCenter("运单号不正确");
                    return;

                }

                if (phonenum.equals("")) {
                    ToastUtil.showToastCenter("手机号为空");
                    return;

                }

                //测试的时候这里先去掉，之后记得打开 4.24
               if (phonenum.matches(num)==false){
                    ToastUtil.showToastCenter("输入号码不正确");
                    return;
                }



                if (selectname.equals("")) {
                    ToastUtil.showToastCenter("请选择一个签收标签");
                    return;

                }

                //这里不用fragment和fragment之间的数据跳转了 。（以后记得删掉 4.21）
                SignMakePhotoAndVoiceFragment fragment = SignMakePhotoAndVoiceFragment.newInstance(expressnum,
                        receivername, phonenum, selectname);
                fragment.setTargetFragment(this, REQUEST_CODE);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fy, fragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.returnIm_rl:
            case R.id.returnIm:
                getFragmentManager().popBackStack();

                break;

            case R.id.scan_qr:

                startActivityForResult(new Intent(getActivity(),CaptureActivity.class),QRCode_Action);


                break;
        }

    }




    @Override
    public void onInputComplete(String text) {

        /***
         *
         * 之前隐藏添加和显示的逻辑都是显示在这里的，但是貌似并不会走这些判断，不止其原因 4.16
         *
         * 以后有空回来看看这里的逻辑 4.16
         *
         */

//        if (mTagList.size() > 11) {
//            // add_tag.setVisibility(View.GONE);
//            add_tag.setEnabled(false);
//            ToastUtil.showToastCenter("最多添加三个自定义标签");
//            return;
//
//        }

        newString = text;
        mTagList.add(newString.toString());
        AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1,String.valueOf(mTagList.size()+1));


    }

    @Override
    public void delectimagecallback(XzhengRelativelayout xz) {

        /***
         *
         * 留着以后真正调后台同步标签的时候用
         * 要求后台在添加标签的时候返回一个tag标签的id 4.26
         *
         */
        //ToastUtil.showToast("--当前的tag--"+xz.getTag().toString());
        tag_vessel.removeView(xz);
        for (int j = 0; j < mTagList.size(); j++) {
            Log.v("==", xz.getTextTostring() + "==" + mTagList.get(j).toString());
            if (xz.getTextTostring().replaceAll(" ", " ")
                    .equals(mTagList.get(j).toString().replaceAll(" ", " "))) {
                mTagList.remove(j);
            }
        }



    }


    //fragment的的下一个传值到上一个就不用，但是之前项目重写onactivityforresult会覆盖fragment之间的传值问题，值的注意
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else if (requestCode == REQUEST_CODE) {
            LogUtil.e("----" + data);

        }else if (requestCode==QRCode_Action){
            String qrcode = data.getStringExtra("qrcode");
            if (qrcode.toString().trim()!=null){
                expressnum.setText(qrcode);
                expressnum.setSelection(qrcode.length());
            }
        }

    }


    //listview的关闭动画
    private void animateOpen(XiongListview view) {
        view.setVisibility(View.VISIBLE);
        // 这个应该是伸展动画，就是下拉的效果
        ValueAnimator animator = createDropAnimator(view, 0, mFoldedViewMeasureHeight);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }
        });
        animator.start();
    }


    //listview的展开动画
    private void animateClose(final XiongListview view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                isAnimating = false;
            }
        });
        animator.start();
    }


    private void showIbtn() {
        if (isFold) {
            im1.setImageResource(R.drawable.push_im);
        } else {
            im1.setImageResource(R.drawable.pull_im);
        }
        isFold = !isFold;
    }

    private ValueAnimator createDropAnimator(final View view, int start, int end) {
        showIbtn();
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
