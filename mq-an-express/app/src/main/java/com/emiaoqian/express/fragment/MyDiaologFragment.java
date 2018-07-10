package com.emiaoqian.express.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.emiaoqian.express.R;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.ToastUtil;


/**
 * Created by xiong on 2018/3/28.
 */


//下面这篇dialog是鸿洋的dialogfragment
public class MyDiaologFragment extends android.support.v4.app.DialogFragment {

    private Button cancel;
    private Button surebt;
    private EditText editText;

    public interface InputListener
    {
        void onInputComplete(String text);
    }

    public static InputListener inputListener;

    public static  void setOninputListener(InputListener inputListener){

        MyDiaologFragment.inputListener=inputListener;


    }



    //    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.dialog_view, null);
//        return view;
//    }




    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view, null);

        editText = (EditText) view.findViewById(R.id.id_txt_your_name);
        cancel = (Button) view.findViewById(R.id.cancel_bt);
        surebt = (Button) view.findViewById(R.id.id_sure_edit_name);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



            surebt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (editText.getText().length()>5){

                        ToastUtil.showToastCenter("不能超过5个字");

                    }else {
                        inputListener.onInputComplete(editText.getText().toString().trim());
                        dismiss();
                    }


                }
            });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

//        Dialog dialog=new Dialog(getContext());
//        dialog.setContentView(view);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);





        return builder.create();
       // return dialog;
    }


    @Override
    public void onResume() {
        super.onResume();

//        if (editText.getText().length()>5){
//
//            ToastUtil.showToastCenter("不能超过5个字呦");
//            surebt.setEnabled(false);
//        }else {
//            surebt.setEnabled(true);
//            surebt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    inputListener.onInputComplete(editText.getText().toString().trim());
//                    dismiss();
//                }
//            });
//        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);


    }
}
