package com.emiaoqian.express.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emiaoqian.express.R;
import com.emiaoqian.express.interfaces.Myinterface;
import com.emiaoqian.express.utils.ToastUtil;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.umeng.message.PushAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by xiong on 2018/4/1.
 */

public class landscapeSignboardActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView changeportrait;
    private TextView confirmtv;
    private SignaturePad signaturepad;
    private TextView cleartv;
    private ImageView closeim;

    private boolean ischange=false;
    private Handler handler=new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landscape_sign_dialog_view);
        PushAgent.getInstance(this).onAppStart();
        initialize();


        changeportrait.setOnClickListener(this);
        cleartv.setOnClickListener(this);
        closeim.setOnClickListener(this);
        confirmtv.setOnClickListener(this);
    }


    @Override
    protected void onRestart() {
        Log.e("66onRestart","landscapeSignboardActivity");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.e("66onResume","landscapeSignboardActivity");
        super.onResume();
    }


    @Override
    protected void onPause() {
        Log.e("66onPause","landscapeSignboardActivity");
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        if (ischange) {
            dismessSmallDialog.DismessSmallDialogcallback();
            ischange=false;
        }
        Log.e("66onDestroy","landscapeSignboardActivity");
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //切换到竖屏
            case R.id.change_portrait:
                finish();

                break;
            case R.id.confirm_tv:
                if (signaturepad.isEmpty()){
                    ToastUtil.showToastCenter("没有手写签名板");
                    return;
                }
                ischange=true;
                Bitmap signatureBitmap = signaturepad.getSignatureBitmap();
                addPngSignatureToGallery(signatureBitmap);
                finish();
                break;

            case R.id.clear_tv:
                signaturepad.clear();
                break;

            case R.id.close_im:
                finish();
                break;


        }

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
       sendBroadcast(mediaScanIntent);
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


    public static Myinterface.DismessSmallDialog dismessSmallDialog;
    public static void setDismessSmallDialogCallback(Myinterface.DismessSmallDialog dismessSmallDialog){
        landscapeSignboardActivity.dismessSmallDialog=dismessSmallDialog;
    }


    private void initialize() {

        changeportrait = (TextView) findViewById(R.id.change_portrait);
        confirmtv = (TextView) findViewById(R.id.confirm_tv);
        signaturepad = (SignaturePad) findViewById(R.id.signature_pad);
        cleartv = (TextView) findViewById(R.id.clear_tv);
        closeim = (ImageView) findViewById(R.id.close_im);
    }


}
