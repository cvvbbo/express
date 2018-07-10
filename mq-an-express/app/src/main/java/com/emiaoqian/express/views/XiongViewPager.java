package com.emiaoqian.express.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by xiong on 2018/4/12.
 */

public class XiongViewPager extends ViewPager {


    private boolean isCanScroll = true;

    public XiongViewPager(Context context) {

        super(context);

    }

    public XiongViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public void setNoScroll(boolean noScroll) {

        this.isCanScroll = noScroll;

    }

    @Override

    public void scrollTo(int x, int y) {

        super.scrollTo(x, y);

    }

    @Override

    public boolean onTouchEvent(MotionEvent arg0) {

        if (isCanScroll){

            return false;

        }else{

            return super.onTouchEvent(arg0);

        }

    }

    @Override

    public boolean onInterceptTouchEvent(MotionEvent arg0) {

        if (isCanScroll){

            return false;

        }else{

            return super.onInterceptTouchEvent(arg0);

        }

    }

}