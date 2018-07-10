package com.emiaoqian.express.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.emiaoqian.express.R;
import com.emiaoqian.express.utils.ToastUtil;
import com.github.gcacace.signaturepad.views.SignaturePad;

/**
 * Created by xiong on 2018/4/20.
 */

public class InputExpressNumDialogFragment extends DialogFragment {


    private Handler handler=new Handler();
    private EditText express_num;
    private TextView confirm_tv;

    public interface InputnumListener
    {
        void onInpunumtComplete(String text);
    }


    public static InputnumListener inputnumListener;
    public static void setonInpunumtCompletecallback(InputnumListener inputnumListener){
        InputExpressNumDialogFragment.inputnumListener=inputnumListener;
    }


    @Override
    public void onResume() {
        super.onResume();
        int width = getActivity().getResources().getDimensionPixelOffset(R.dimen.x300);
        int height = getActivity().getResources().getDimensionPixelOffset(R.dimen.x200);
        //估计这个方法是在oncreate中走的 ，这个需要的宽度可以一步一步试出来 4.20
        //当然也可以使用recyclerview中的设置布局的骚操作 4.20
        getDialog().getWindow().setLayout(width, height);
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        View view = getActivity().getLayoutInflater().inflate(R.layout.input_num_view, null);



        express_num = (EditText) view.findViewById(R.id.express_num);

        confirm_tv = (TextView) view.findViewById(R.id.confirm_tv);

        //强制弹出输入框，有时候需要延迟加载出来！！
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(express_num, 0);
            }
        },300);



        //设置左边的图片 4.20
        final Drawable drawable=getResources().getDrawable(R.drawable.clear);
        int offset = getActivity().getResources().getDimensionPixelOffset(R.dimen.x15);
        drawable.setBounds(0,0,offset,offset);
        express_num.setCompoundDrawables(null,null,drawable,null);

        //右边删除图标的处理 4.20
        express_num.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = express_num.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > express_num.getWidth()
                        - express_num.getPaddingRight()
                        - drawable.getIntrinsicWidth()){
                    express_num.setText("");
                }
                return false;
            }
        });

        confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String num1= "^[A-Za-z0-9]+$";
                if(express_num.getText().toString().trim().matches(num1)==false){

                    ToastUtil.showToastCenter("运单号不正确");
                    return;

                }

                if (express_num.getText().toString().trim().equals("")){
                    ToastUtil.showToastCenter("请输入订单号");
                    return;
                }
                inputnumListener.onInpunumtComplete(express_num.getText().toString().trim());
                dismiss();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }
}
