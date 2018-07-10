package com.emiaoqian.express.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by xiong on 2018/4/17.
 */

public class MakephotoUtils {

    private static Bitmap afterbitmap;



    //保存图片，这个是保存bitmap的图片从相册
    public static File saveImageofalbum(Context m, Bitmap bmp, String photoname) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = photoname;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //100%品质就是不会压缩
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /***下面这个注释掉，这个api的意思是在系统的图库中再拷贝一份自己的刚才保存的图片 4.17**/
//        // 其次把文件插入到系统图库(网上的大部分方案还需要这个 3.27)
//        try {
//            MediaStore.Images.Media.insertImage(m.getContentResolver(),
//                    file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        //因为这个方法是为了临时保存的，所以看看不刷新会怎么样
        m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));

        return file;

    }


    //压缩图片的方法
    public static Bitmap compressImage(Activity m, String filepath) {
        int height = m.getWindowManager().getDefaultDisplay().getHeight();
        int width = m.getWindowManager().getDefaultDisplay().getWidth();
        Point p = new Point();
        m.getWindowManager().getDefaultDisplay().getSize(p);
        width = p.x;
        height = p.y;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //下面这个是获取不到大小的，因为加载进内存的大小为0
        Bitmap bitmap = BitmapFactory.decodeFile(filepath);
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
        //Log.e("--压缩之后", afterbitmap.getByteCount() + " ");
        return afterbitmap;
    }


    //保存图片，这个是保存bitmap的图片。
    public static  void saveImage(Context m, Bitmap bmp, String photoname) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = photoname;
        File file = new File(appDir, fileName);
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

//        try {
//            MediaStore.Images.Media.insertImage(m.getContentResolver(),
//                    file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));
    }

    public static void deleimage(Context m,String fileName) {

        File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        File file = new File(appDir, fileName);
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = m.getContentResolver();
        String where = MediaStore.Images.Media.DATA + "='" + file + "'";
        //删除图片
        mContentResolver.delete(uri, where, null);

        m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));


    }


    public static void lubanImage(final Context m, final String filewpath, String targefile , final String filename){
        Luban.with(m)
                .load(filewpath)
                // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setTargetDir(targefile)                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
//                        afterbitmap = BitmapFactory.decodeFile(file.getPath());
//                        LogUtil.e(afterbitmap+"");
                        LogUtil.e(file+"");


                        //File oldfile = new File(file.getParent(), file.getName());
                        File newfile = new File(file.getParent(),filename);
                        file.renameTo(newfile);
                        m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(file)));
                        m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(newfile)));

                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        LogUtil.e(e+"--");
                    }
                }).launch();    //启动压缩
    }

    public static Bitmap CompressImage(Context context,File file){
        try {
          return new Compressor(context).compressToBitmap(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public  static void Compress2(Context m,File tagfile,String newname){
        File file = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
        try {
           File compressedImage = new Compressor(m)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
    //                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
    //                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                   //图片写入的路径
                    .setDestinationDirectoryPath(file.getPath())
                   //图片原来的路径（就是还没有放到目标文件夹时候的路径）
                    .compressToFile(tagfile);

            //如果是相同路径的是先复制一张图片然后在改名，如果是不同路径的则直接改名即可
//            if (tagfile.getParent().equals(file.getParent())){
//                String name="123.jpg";
//                saveImage(m,BitmapFactory.decodeFile(tagfile.getPath()),name);
//
//                File filepath = new File(file, name);
//                File newfile = new File(file, newname);
//                filepath.renameTo(newfile);
//            }else {
//                File filepath = new File(file, tagfile.getName());
//                File newfile = new File(file, newname);
//                filepath.renameTo(newfile);
//            }

            File filepath = new File(file, tagfile.getName());
            File newfile = new File(file, newname);
            filepath.renameTo(newfile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));
    }


    public  static void Compress3(Context m,File tagfile,String newname){
        File file = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
        //如果是相同路径的是先复制一张图片然后在改名，如果是不同路径的则直接改名即可
            if (tagfile.getParent().equals(file.getParent())){
                String name="123.jpg";
                File replace = new File(file, name);
                copyFile3(tagfile.getPath(),replace.getPath());
                try {
                    File compressedImage = new Compressor(m)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(file.getPath())
                            //图片原来的路径（就是还没有放到目标文件夹时候的路径）
                            .compressToFile(replace);


                    File filepath = new File(file, replace.getName());
                    File newfile = new File(file, newname);
                    filepath.renameTo(newfile);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else {

                try {
                    File compressedImage = new Compressor(m)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(file.getPath())
                            //图片原来的路径（就是还没有放到目标文件夹时候的路径）
                            .compressToFile(tagfile);


                    File filepath = new File(file, tagfile.getName());
                    File newfile = new File(file, newname);
                    filepath.renameTo(newfile);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));
    }

    // 方法三
    public static void copyFile3(String srcPath, String destPath)  {
        // 打开输入流
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(srcPath);
            // 打开输出流
            FileOutputStream fos = new FileOutputStream(destPath);

            // 读取和写入信息
            int len = 0;
            // 创建一个字节数组，当做缓冲区
            byte[] b = new byte[1024];
            while ((len = fis.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            // 关闭流  先开后关  后开先关
            fos.close(); // 后开先关
            fis.close(); // 先开后关
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
