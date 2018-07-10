package com.emiaoqian.express.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emiaoqian.express.R;
import com.emiaoqian.express.activity.MainActivity;
import com.emiaoqian.express.interfaces.Myinterface2;

import java.util.ArrayList;


/**
 * Created by xiong on 2018/4/3.
 */

public class XzhengRelativelayout extends RelativeLayout implements Myinterface2.showredcircle{


    /***
     *
     * 自定义控件里面的控件可以随意获取，感觉压根不用接口！！！4.11
     *
     *
     */

    private TextView textView;
    private ImageView imageview;

    private ArrayList<String> size=new ArrayList<>();

    private String mtag;


    public XzhengRelativelayout(Context context) {
        this(context,null);

    }

    public XzhengRelativelayout(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public XzhengRelativelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        MainActivity.setShowredcircle(this);
        initview(context);
    }

    private void initview(Context context) {
        View view = View.inflate(context, R.layout.my_text_view, null);
        addView(view);
        textView = (TextView) view.findViewById(R.id.tv);
        imageview = (ImageView) view.findViewById(R.id.im);




        imageview.setVisibility(View.GONE);
            //左上角的点击事件的操作 4.3


        imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteimage.delectimagecallback(XzhengRelativelayout.this);


            }
        });


    }

    public void setTextsize(float size){
        textView.setTextSize(size);
    }


    public void setTextTextColor(int Color){
        textView.setTextColor(Color);
    }

    public String getTextTostring(){

         return textView.getText().toString().trim();
    }


    public void setText(String text){
        textView.setText(text);
    }


    @Override
    public void showredcirclecallback() {

            imageview.setVisibility(VISIBLE);

    }

    @Override
    public int isshowcallback() {

        return imageview.getVisibility();
    }

    @Override
    public void hidecallback() {

            imageview.setVisibility(GONE);
    }


    public static Myinterface2.deleteimage deleteimage;
    public static void setDeleteimagecallback(Myinterface2.deleteimage deleteimage){
        XzhengRelativelayout.deleteimage=deleteimage;
    }


    //直接获取对应即可，简单直接
    public ImageView getImageview(){
        return imageview;
    }

    public TextView getTextView(){
        return textView;
    }


}
