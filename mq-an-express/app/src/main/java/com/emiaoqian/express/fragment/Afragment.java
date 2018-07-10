package com.emiaoqian.express.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.emiaoqian.express.R;
import com.emiaoqian.express.interfaces.Myinterface;
import com.emiaoqian.express.utils.LogUtil;

/**
 * Created by xiong on 2018/4/11.
 */

public class Afragment extends Fragment implements View.OnClickListener ,AMap.InfoWindowAdapter{


    private MapView mMapView;
    private ImageView zoomin;
    private ImageView zoomout;
    private AMap aMap;
    MyLocationStyle myLocationStyle;

    //后面这个是记录初始的坐标位置的！！ 4.13
    private LatLng mStartPosition;
    private ImageView locationBt;
    private UiSettings uiSettings;

    private LocationSource.OnLocationChangedListener mListener;
    private LatLng latLng;



    private boolean isfollow=true;






    public static Myinterface.HiddenButtonSheet hiddenButtonSheet;
    private TextureMapView mTextureMapView;
    private View view;
    private int width;

    public static void sethiddenButtonSheetcallback(Myinterface.HiddenButtonSheet hiddenButtonSheet){
        Afragment.hiddenButtonSheet=hiddenButtonSheet;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            hiddenButtonSheet.HiddenButtonSheetcallback(false);
        }
        LogUtil.e("--Afragment隐藏的状态是否相同-",isVisibleToUser+"");
        LogUtil.e("10086当前状态setUserVisibleHint"+getUserVisibleHint()+"--"+getClass().getSimpleName());
    }


    //只有这个无状态
    @Override
    public boolean getUserVisibleHint() {
        LogUtil.e("10086当前状态getUserVisibleHint()"+super.getUserVisibleHint()+"--"+getClass().getSimpleName());
        return super.getUserVisibleHint();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        view = inflater.inflate(R.layout.a_fragment, null);
//
//        //mMapView = (MapView) view.findViewById(R.id.map);
//
//        mTextureMapView = (TextureMapView) view.findViewById(R.id.map);
//        zoomin = (ImageView) view.findViewById(R.id.zoomin);
//        zoomout = (ImageView) view.findViewById(R.id.zoomout);
//
//        locationBt = (ImageView) view.findViewById(R.id.location_bt);
//
//        locationBt.setOnClickListener(this);
//
//        //mMapView.onCreate(savedInstanceState);
//
//        mTextureMapView.onCreate(savedInstanceState);
//
//        //初始化地图控制器对象
////        if (aMap == null) {
////            aMap = mMapView.getMap();
////        }
//
//        if (aMap == null) {
//            aMap = mTextureMapView.getMap();
//        }
//
//        afterinit();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LogUtil.e("----10086---"+getClass().getSimpleName()+"--onCreateView--");
//        if(view != null){
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if(parent != null){
//                //存在就将其删除
//                parent.removeView(view);
//            }
//        }

//
        view = inflater.inflate(R.layout.a_fragment, null);

        //mMapView = (MapView) view.findViewById(R.id.map);

        mTextureMapView = (TextureMapView) view.findViewById(R.id.map);
        zoomin = (ImageView) view.findViewById(R.id.zoomin);
        zoomout = (ImageView) view.findViewById(R.id.zoomout);

        locationBt = (ImageView) view.findViewById(R.id.location_bt);

        locationBt.setOnClickListener(this);

        //mMapView.onCreate(savedInstanceState);

        mTextureMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象
//        if (aMap == null) {
//            aMap = mMapView.getMap();
//        }

        if (aMap == null) {
            aMap = mTextureMapView.getMap();
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        afterinit();



    }

    private void afterinit() {
        width = getActivity().getResources().getDimensionPixelOffset(R.dimen.x150);
        zoomin.setOnClickListener(this);
        zoomout.setOnClickListener(this);

        //静止自带的缩放按钮
        uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);

        //设置默认的定位按钮不显示 4.12
        uiSettings.setMyLocationButtonEnabled(false);


        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类

        //下面这个模式能让定位自己点的坐标会到正常的模式(但是不跟随自己)。
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);

        //点的位置的方向不改变，并且定位自己（但是是连续定位）
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);

        myLocationStyle = this.myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位

        this.myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。


        // Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.a);


        /**
         *
         * 自定义定位自己所在的坐标点。4.9  ,如果是自定义的点这里就设置了自定义的图片，经度圈之类的！！
         *
         * 因为下面的aMap.setMyLocationStyle这个方法
         *
         */
        this.myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                //下面是把精度圈设置为透明
                .fromResource(R.drawable.aa)).strokeColor(Color.argb(0, 0, 0, 0)).radiusFillColor(Color.argb(0, 0, 0, 0));


        aMap.setMyLocationStyle(this.myLocationStyle);//设置定位蓝点的Style


        Spannable spannable1 = Spannable.Factory.getInstance().newSpannable("一小时内取件\n准时奖0.5元");
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#FF0000"));
        spannable1.setSpan(foregroundColorSpan, 10, 12, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        // mTextView1.setText(spannable1);


        aMap.setInfoWindowAdapter(this);
        //高德地图有专门自己停止定位的方法 4.26
        //currentMarker.hideInfoWindow();//这个是隐藏infowindow窗口的方法 4.26
        //设置跟随地图移动的market

        Marker marker = aMap.addMarker(new MarkerOptions()
                .title("hahah")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bb)).draggable(true));

        marker.setPositionByPixels(width, width);
        marker.showInfoWindow();// 设置默认显示一个infowinfow
        //设置market不可店家
        marker.setClickable(false);


        //这个是官方网站文档里面带。。自带的定位去不掉就是因为这个4.12
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。

        /***
         *
         * 这个在高德上有定位的回调，用于显示自己的围标的位置4.9
         *
         */

        //即使自定义了小蓝点这个还是不能够去掉，否则报错！！ 4.13
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。


        //这个方法是网站的api指南里面的
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                    if (isfollow) {
                        //这个类是以前包里面的类，但是并不是在这里用，
                        AMapLocation aMapLocation = new AMapLocation(location);

                        latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                        //17目前正好
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        aMap.setPointToCenter(width, width);

                    }

            }
        });

        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                isfollow=false;
            }
        });
    }

    public Bitmap aa(int drawableid){
        int with = getResources().getDimensionPixelOffset(R.dimen.x33);
        int height = getResources().getDimensionPixelOffset(R.dimen.x50);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableid);
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(bitmap, with, height);
        return bitmap1;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zoomin:

                LogUtil.e("-233zoomin-");
                aMap.animateCamera(CameraUpdateFactory.zoomIn());

                break;
            case R.id.zoomout:

                LogUtil.e("--233zoomout--");
                aMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;

            case R.id.location_bt:


                isfollow=true;

                //先记录初始化的点，然后赋值到这里 4.13
                mStartPosition=latLng;
                CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                        mStartPosition, 15);
                aMap.animateCamera(cameraUpate);
                //改变中心点在手机屏幕中心显示的位置（因为会在地图上加一些悬浮的控件，本来点位点是在屏幕中心的，但是悬浮的控件遮住了中心点）
                //所以要改变定位点在屏幕中心显示的位置 4.13
                aMap.setPointToCenter(width,width);

                break;

        }
    }


    @Override
    public void onDestroy() {

        LogUtil.e("10086onDestroy--"+getClass().getSimpleName());
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
       // mMapView.onDestroy();

        mTextureMapView.onDestroy();

    }

    @Override
    public void onResume() {

        LogUtil.e("10086onResume--"+getClass().getSimpleName());

        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
       // mMapView.onResume();

        mTextureMapView.onResume();
        LogUtil.e(aMap+"---");


    }

    @Override
    public void onPause() {

        LogUtil.e("10086onPause--"+getClass().getSimpleName());

        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
       // mMapView.onPause();

        mTextureMapView.onPause();
        LogUtil.e(aMap+"---");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mMapView.onSaveInstanceState(outState);

        mTextureMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        LogUtil.e("--10086--"+"onDestroyView"+getClass().getSimpleName());
        aMap=null;
       // mMapView.onDestroy();
       mTextureMapView.onDestroy();

//        ViewGroup mGroup = (ViewGroup) view.getParent();
//        if (mGroup != null) {
//            /**
//             * 但是removeAllViewsInLayout() 需要先测量当前的布局, 一旦调用该方法,只能移除已经自身布局中已计算好的所包含的子view
//             *
//             * https://blog.csdn.net/stzy00/article/details/43966149
//             *
//             * 下面这里已经是强制转为了父布局了，所以只是移除了父布局的宽高 4.16
//             *
//             */
//            mGroup.removeAllViewsInLayout();
//        }

        super.onDestroyView();

    }

    @Override
    public View getInfoWindow(Marker marker) {
//        View infoContent = getActivity().getLayoutInflater().inflate(
//                R.layout.custom_info_contents, null);
//        render(marker, infoContent);
//        return infoContent;

        View infoWindow = getActivity().getLayoutInflater().inflate(
                R.layout.info_window, null);
        render(marker,infoWindow);
        return infoWindow;
    }

    //提供了一个给默认信息窗口定制内容的方法。如果用自定义的布局，不用管这个方法。
    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }

    public void render(Marker marker, View view) {
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_time_info = (TextView)view.findViewById(R.id.tv_time_info);
        TextView tv_distance =(TextView) view.findViewById(R.id.tv_distance);

    }
}


