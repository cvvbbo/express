package com.emiaoqian.express.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emiaoqian.express.R;

/**
 * Created by xiong on 2018/5/3.
 */

public class MyImageview extends RelativeLayout {


    private ImageView smallim;
    private ImageView realim;
    private RelativeLayout photo_rl;
    private TextView tv;

    public MyImageview(Context context) {
        this(context,null);
    }

    public MyImageview(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public MyImageview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context){

        View view = View.inflate(context, R.layout.my_two_image, null);
        addView(view);
        photo_rl = (RelativeLayout) view.findViewById(R.id.photo_rl);
        smallim = (ImageView) view.findViewById(R.id.im2);
        realim = (ImageView) view.findViewById(R.id.real_im);
        tv = (TextView) view.findViewById(R.id.tv);
        smallim.setVisibility(GONE);
        realim.setVisibility(GONE);

    }



    public  ImageView Getsmallim(){
        return smallim;
    }

    public ImageView Getrealim(){
        return realim;
    }

    public RelativeLayout Getrelativelayout(){

        return photo_rl;
    }

    public TextView Gettv(){
        return tv;
    }
}
