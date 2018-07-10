package com.emiaoqian.express.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emiaoqian.express.R;
import com.emiaoqian.express.activity.landscapeSignboardActivity;
import com.emiaoqian.express.interfaces.Myinterface;
import com.emiaoqian.express.utils.ToastUtil;
import com.emiaoqian.express.views.MydialogOnbutton1;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by xiong on 2018/4/1.
 * https://github.com/gcacace/android-signaturepad
 *
 *
 *
 */

public class SigntvDialogFragment extends DialogFragment implements View.OnClickListener{


    private LinearLayout layout;
    private TextView change_landscape;
    private TextView confirm_tv;
    private TextView cleartv;
    private ImageView closeim;
    private SignaturePad signaturepad;
    private TextView confirmtv;
    private TextView changelandscape;


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.sign_dialog_view,null);
//        return view;
//    }




    public static Myinterface.DismessForNetwork dismessfornetwork;
    private TextView tv;
    private RelativeLayout close_im_rl;

    public static void setDismessfornetworkcallback(Myinterface.DismessForNetwork dismessfornetwork){
        SigntvDialogFragment.dismessfornetwork=dismessfornetwork;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
 //       View view = getActivity().getLayoutInflater().inflate(R.layout.sign_dialog_view, null);
//        change_landscape = (TextView) view.findViewById(R.id.change_landscape);
//        confirm_tv = (TextView) view.findViewById(R.id.confirm_tv);
//
//        cleartv = (TextView)view.findViewById(R.id.clear_tv);
//        closeim = (ImageView)view.findViewById(R.id.close_im);
//        signaturepad = (SignaturePad) view.findViewById(R.id.signature_pad);
//
//
//        //竖屏切换横屏，思路就是转跳到新的activity中
//        change_landscape.setOnClickListener(this);
//        confirm_tv.setOnClickListener(this);
//        cleartv.setOnClickListener(this);
//        closeim.setOnClickListener(this);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(view);
//        AlertDialog alertDialog = builder.create();
//        Window dialogWindow = alertDialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width =  WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialogWindow.setAttributes(lp);
//        dialogWindow.setGravity(Gravity.BOTTOM);
//        return alertDialog;


        /**
         * 如果这里不加样式，将会和底部仍旧有写微小的距离。
         * https://www.jianshu.com/p/3d045ee00880
         *
         */
        Dialog dialog=new Dialog(getActivity(),R.style.BottomDialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.sign_dialog_view, null);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //change_landscape = (TextView) view.findViewById(R.id.change_landscape);
        confirm_tv = (TextView) view.findViewById(R.id.confirm_tv);
        close_im_rl = (RelativeLayout) view.findViewById(R.id.close_im_rl);

        close_im_rl.setOnClickListener(this);
        cleartv = (TextView)view.findViewById(R.id.clear_tv);
        closeim = (ImageView)view.findViewById(R.id.close_im);
        signaturepad = (SignaturePad) view.findViewById(R.id.signature_pad);
        tv = (TextView) view.findViewById(R.id.tv);

        signaturepad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                tv.setVisibility(View.GONE);
            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {
                tv.setVisibility(View.VISIBLE);

            }
        });



        //竖屏切换横屏，思路就是转跳到新的activity中
       // change_landscape.setOnClickListener(this);
        confirm_tv.setOnClickListener(this);
        cleartv.setOnClickListener(this);
        closeim.setOnClickListener(this);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width =  WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
        return dialog;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //切换到竖屏
//            case R.id.change_landscape:
//                signaturepad.clear();
//                Intent intent=new Intent(getActivity(),landscapeSignboardActivity.class);
//                startActivity(intent);
//
//                break;

            case R.id.close_im_rl:
                dismiss();
                break;

            case R.id.clear_tv:
                signaturepad.clear();
                break;

            case R.id.close_im:
                dismiss();
                break;
            case R.id.confirm_tv:
                //保存图片逻辑

                if (signaturepad.isEmpty()){
                    ToastUtil.showToastCenter("没有手写签名");
                    return;
                }
                Bitmap signatureBitmap = signaturepad.getSignatureBitmap();

                addPngSignatureToGallery(signatureBitmap);
                dismessfornetwork.DismessForNetworkcallback();

                dismiss();
                break;
        }

    }

    @Override
    public void onResume() {
        //Log.e("66onResume","SigntvDialogFragment");
        super.onResume();
    }

    @Override
    public void onPause() {
        //Log.e("66onPause","SigntvDialogFragment");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.e("66onDestroy","SigntvDialogFragment");
        super.onDestroy();
    }

    public boolean addPngSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            //File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));

            File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File photo = new File(appDir,"personal_sign.png");
            saveBitmapToPNG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    //这个是刷新图库的方法，就是实时刷新，如果不走这个方法，那么将看不到图片
    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }


    public void saveBitmapToPNG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
        stream.close();
    }


}
