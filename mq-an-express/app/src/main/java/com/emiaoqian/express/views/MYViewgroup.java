package com.emiaoqian.express.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emiaoqian.express.R;
import com.github.gcacace.signaturepad.views.SignaturePad;

/**
 * Created by xiong on 2018/5/21.
 */

public class MYViewgroup extends RelativeLayout {

    private Context mcontext;
    private TextView tv;
    private SignaturePad signaturePad;

    public MYViewgroup(Context context) {
        this(context,null);
    }

    public MYViewgroup(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public MYViewgroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        context=mcontext;
    }

    private  void initview(){
        View view = View.inflate(mcontext, R.layout.my_board, null);
        tv = (TextView) view.findViewById(R.id.tv);
        signaturePad = (SignaturePad) view.findViewById(R.id.signature_pad);
        addView(view);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }
}
