package com.emiaoqian.express.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.emiaoqian.express.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by xiong on 2018/3/26.
 *  这个是展示大图的类
 *
 *
 */

public class PhotoviewActivity extends AppCompatActivity {



    private String filename;
    private PhotoView photoview;

    private PhotoViewAttacher attacher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view);
        PushAgent.getInstance(this).onAppStart();
        photoview = (PhotoView) findViewById(R.id.im);
        ImageView returnIm= (ImageView) findViewById(R.id.returnIm);
        returnIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //根目录
        File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");

        Intent intent = getIntent();
        String photoname = intent.getStringExtra("photoname");
        if (photoname!=null){
            if (photoname.equals("package_img.png")){
                filename=photoname;

            }else if (photoname.equals("position_img.png")){
                filename=photoname;
            }else if (photoname.equals("personal_sign.png")){
                filename=photoname;
            }
        }


        File path=new File(appDir,filename);

        Bitmap bitmap = BitmapFactory.decodeFile(path.getPath());
        photoview.setImageBitmap(bitmap);
        attacher = new PhotoViewAttacher(photoview);
        attacher.update();


    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
