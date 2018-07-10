package com.emiaoqian.express.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by xiong on 2018/4/21.
 *
 *   动态计算listview的高度
 *   https://blog.csdn.net/loveliu030107/article/details/49447509
 *
 */

public class XiongListview extends ListView {

    public XiongListview(Context context) {
        super(context);
    }

    public XiongListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
