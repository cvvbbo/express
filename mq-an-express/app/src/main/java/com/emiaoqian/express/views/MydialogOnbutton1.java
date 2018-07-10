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

import java.io.File;


public class MydialogOnbutton1 extends Dialog {

	private Button album;
	private Button makephoto;
	private Button btexit;

	public AppCompatActivity a;

	public  Fragment b;

	public Context mcontext;

	public MydialogOnbutton1(Context context, boolean cancelable,
							 OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public MydialogOnbutton1(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}


	public MydialogOnbutton1(Context context, Fragment f) {
		super(context, R.style.Mydialog);
		a= (AppCompatActivity) context;
		b=f;

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
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用系统相机
//		Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//		//a.startActivityForResult(intent, 100);  //用户点击了从相机获取
//		b.startActivityForResult(intent,100);


		if (Build.VERSION.SDK_INT>Build.VERSION_CODES.N) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/test/" + "image.jpg");
			file.getParentFile().mkdirs();

			//改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
			Uri uri = FileProvider.getUriForFile(b.getActivity(), "com.emiaoqian.express.fileprovider", file);
			//添加权限
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			b.startActivityForResult(intent, 100);
		}else {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用系统相机
			Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			//a.startActivityForResult(intent, 100);  //用户点击了从相机获取
			b.startActivityForResult(intent,100);
		}
	}


	//从相册中获取
	public void getimagefromalbun() {
		Intent intent = new Intent();
		intent.setType("image/*");  // 开启Pictures画面Type设定为image
		intent.setAction(Intent.ACTION_GET_CONTENT); //使用Intent.ACTION_GET_CONTENT这个Action
		//a.startActivityForResult(intent, 102); //取得相片后返回到本画面
		b.startActivityForResult(intent,102);
	}
}
