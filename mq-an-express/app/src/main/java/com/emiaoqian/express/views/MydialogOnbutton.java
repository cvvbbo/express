package com.emiaoqian.express.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.emiaoqian.express.R;
import com.emiaoqian.express.application.MyApplication;

import java.io.File;


public class MydialogOnbutton extends Dialog {

	private Button album;
	private Button makephoto;
	private Button btexit;

	public AppCompatActivity a;



	public Context mcontext;

	public MydialogOnbutton(Context context, boolean cancelable,
                            OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public MydialogOnbutton(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}


	public MydialogOnbutton(Context context) {
		super(context, R.style.Mydialog);
		a= (AppCompatActivity) context;

		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_choose_dialog);
		Window window = getWindow();
		LayoutParams attributes = window.getAttributes();
		attributes.gravity= Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		attributes.height= ViewGroup.LayoutParams.WRAP_CONTENT;
		attributes.width= ViewGroup.LayoutParams.MATCH_PARENT;
		window.setAttributes(attributes);
		initialize();
	}


	private void initialize() {

		//相册
		album = (Button) findViewById(R.id.album);
		//照片
		makephoto = (Button) findViewById(R.id.makephoto);
		//取消按钮
		btexit = (Button) findViewById(R.id.btexit);


		btexit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();

			}
		});

		makephoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

                doTakePhoto();
				dismiss();
			}
		});


		//相册
		album.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getimagefromalbun();

				dismiss();
			}
		});
	}


	//拍照中获取
    private void doTakePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用系统相机

		//安卓7.0的拍照 5.2
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			Uri photoOutputUri = FileProvider.getUriForFile(
					MyApplication.mcontext,
					MyApplication.mcontext.getPackageName() + ".fileprovider",
					new File(Environment.getExternalStorageDirectory(), "image.jpg"));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri);
			a.startActivityForResult(intent, 100);

		}else {
			//Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用系统相机
			Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			a.startActivityForResult(intent, 100);  //用户点击了从相机获取
		}

    }


    //从相册中获取
    public void getimagefromalbun() {
        Intent intent = new Intent();
        intent.setType("image/*");  // 开启Pictures画面Type设定为image
        intent.setAction(Intent.ACTION_GET_CONTENT); //使用Intent.ACTION_GET_CONTENT这个Action
        a.startActivityForResult(intent, 102); //取得相片后返回到本画面

    }
}
