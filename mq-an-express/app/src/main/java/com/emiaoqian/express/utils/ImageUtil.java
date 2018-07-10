package com.emiaoqian.express.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;


import com.emiaoqian.express.application.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageUtil {

    private final static String TAG = ImageUtil.class.getSimpleName();
    public static final String CONST_PICTURES = "/emiaoqian/pictures";
    public static final String CONST_PICTURE_EXT_TMP = ".tmp.jpg";
    public static final String CONST_PICTURE_EXT_SAVE = ".save.jpg";
    public static final String EXTRA_TEMP_OUTPUT_FULLNA = "EXTRA_TEMP_OUTPUT_FULLNA";
    private static boolean isDebug = true;

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    /**
     * go for Album.（从相册中获取）
     */
    public static final Intent choosePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return Intent.createChooser(intent, null);
    }

    /**
     * go for camera.
     *
     *    edit :2018.5.29 ,这个文件的路径要和fileprovider的路径一致
     */
    public static final Intent takeBigPicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getPictureFullName(CONST_PICTURE_EXT_TMP))));

//        (Environment.getExternalStorageDirectory().getAbsolutePath()
//        + "/test/" + "image.jpg");
        //File photoOutputFile = new File(getPictureFullName(CONST_PICTURE_EXT_TMP));
        //这个是真实的路径，但是7.0是把这个路径装饰一下，用别名代替这个目录6.7
        File photoOutputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/test/" + "image.jpg");
        intent.putExtra(EXTRA_TEMP_OUTPUT_FULLNA, photoOutputFile.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //Uri photoOutputUri = Uri.fromFile(photoOutputFile);
            Uri photoOutputUri = FileProvider.getUriForFile(
                    MyApplication.mcontext,
                    "com.emiaoqian.express.fileprovider",
                    photoOutputFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getPictureFullName(CONST_PICTURE_EXT_TMP))));
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    public static final String getPicturePath() {
        d(String.format("ExternalStorageState=%s", Environment.getExternalStorageState()));
        File path = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = new File(Environment.getExternalStorageDirectory().getPath() + CONST_PICTURES);
        } else {
            path = new File(MyApplication.mcontext.getFilesDir().toString() + CONST_PICTURES);
        }
        d(String.format("getPicturePath=%s", path.toString()));
        if (!path.exists()) {
            path.mkdirs();
        }
        return path.toString();
    }

    private static final String getPictureFullName(String ext) {
        String file = getPicturePath() + "/" + System.currentTimeMillis() + ext;
        d(String.format("getPictureFullName=%s", file));
        return file;
    }


    public static Bitmap compressImage(String filepath, AppCompatActivity appCompatActivity) {
        d(String.format("compressImage=%s", filepath));
        int height = appCompatActivity.getWindowManager().getDefaultDisplay().getHeight();
        int width = appCompatActivity.getWindowManager().getDefaultDisplay().getWidth();
        Point p = new Point();
        appCompatActivity.getWindowManager().getDefaultDisplay().getSize(p);
        width = p.x;
        height = p.y;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //下面这个是获取不到大小的，因为加载进内存的大小为0
        BitmapFactory.decodeFile(filepath);
        BitmapFactory.decodeFile(filepath, options);
        //Log.e("--压缩之前", bitmap.getByteCount() + " ");
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        int index = 1;
        if (outHeight > height || outWidth > width) {
            float heightRate = outHeight / height;
            float widthrate = outHeight / width;

            index = (int) Math.max(heightRate, widthrate);
        }
        options.inSampleSize = index;
        options.inJustDecodeBounds = false;
        Bitmap afterbitmap = BitmapFactory.decodeFile(filepath, options);
        return afterbitmap;
    }

    public static String saveImage(Context m, Bitmap bmp) {
        File file = new File(getPictureFullName(CONST_PICTURE_EXT_SAVE));
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //100%品质就是不会压缩，这里只是把图片保存到另一个地方
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));
        return file.getPath();
    }


    //检查路径
    public static final String retrievePath(Context context, Intent sourceIntent, Intent dataIntent) {
        String picPath = null;
        try {
            Uri uri;
            if (dataIntent != null) {
                uri = dataIntent.getData();
                if (uri != null) {
                    picPath = ContentUtil.getPath(context, uri);
                }
                if (isFileExists(picPath)) {
                    return picPath;
                }

                d(String.format("retrievePath failed from dataIntent:%s, extras:%s", dataIntent, dataIntent.getExtras()));
            }

            if (sourceIntent != null) {
                uri = sourceIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                if (uri != null) {
                    String scheme = uri.getScheme();
                    if (scheme != null && scheme.startsWith("file")) {
                        picPath = uri.getPath();
                    } else {
                        picPath = sourceIntent.getStringExtra(EXTRA_TEMP_OUTPUT_FULLNA);
                    }
                }
                if (!TextUtils.isEmpty(picPath)) {
                    File file = new File(picPath);
                    if (!file.exists() || !file.isFile()) {
                        d(String.format("retrievePath file not found from sourceIntent path:%s", picPath));
                    }
                }
            }
            return picPath;
        } finally {
            d("retrievePath(" + sourceIntent + "," + dataIntent + ") ret: " + picPath);
        }
    }

    private static final boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File f = new File(path);
        if (!f.exists()) {
            return false;
        }
        return true;
    }
}
