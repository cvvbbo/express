package com.emiaoqian.express.utils;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.emiaoqian.express.application.MyApplication;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by xiong on 2018/5/30.
 * 文件下载
 * https://blog.csdn.net/lgzaaron/article/details/51680213
 *
 *
 */

public class UpDataUtil {


    private static final int REQUEST_CODE_INSTALL = 1000;
    private static final String DOWNLOAD_FILE_NAME = "app-release.apk";
    Handler h =new Handler();
    private String mDesc;
    private String mDownloadurl;
    private String desc;
    private DownloadManager manager ;
    private long downloadId;
    private String downurl;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };


    Context mcontext;



    public UpDataUtil(Context mcontext){
        manager =(DownloadManager)mcontext.getSystemService(mcontext.DOWNLOAD_SERVICE);
        this.mcontext=mcontext;
    }


    public  void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(mcontext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions((Activity) mcontext, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }



    public int getVersioncode(){
        PackageManager packageManager = mcontext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mcontext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;

    }


    public  void checkVersion(){

       // {"appName":"express-home" , "appv":"v1", "timestamp":"1111111111111", "sign":"11","test_key":"MIAOQIAN_API_TEST"}

        long l = System.currentTimeMillis();
        String str = String.valueOf(l);
        HashMap<String,String> datas=new HashMap<>();
        datas.put("appName","express-home");
        datas.put("appv","v1");
        datas.put("timestamp",str);
        String s = EncodeBuilder.javaToJSONNewPager1(datas);
        String sign = EncodeBuilder.newString2(s);
        datas.put("sign",sign);

        httphelper.create().NewdataHomePage2(Constants.UPDATA, datas, new httphelper.httpcallback() {
            @Override
            public void success(String s) {

                LogUtil.e(s);

                JSONObject jonObject;
                try {
                    jonObject = new JSONObject(s);
                    String versionname = jonObject.getString("versionname");
                    String versioncode= jonObject.getString("versioncode");
                    desc = jonObject.getString("desc");
                    downurl = jonObject.getString("downloadurl");
                    //注意json的格式不要写错了，不同的数据之间用逗号隔开
                    System.out.println("版本的名字是："+versionname+" 版本号是："+versioncode+" 内容是："+desc+" 下载地址是："+ downurl);
                    int versioncode1 = getVersioncode();
                    if (getVersioncode()<Integer.valueOf(versioncode)) {
                        showDiowlog();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("fail————————");
                }

            }

            @Override
            public void fail(Exception e) {

            }
        });


//        httphelper.create().doget("http://192.168.3.51:8080/haha.txt", new httphelper.httpcallback() {
//            @Override
//            public void success(String s) {
//                LogUtil.e(s);
//
//                JSONObject jonObject;
//                try {
//                    jonObject = new JSONObject(s);
//                    int versionname = jonObject.getInt("versionname");
//                    int versioncode= jonObject.getInt("versioncode");
//                    desc = jonObject.getString("desc");
//                    downurl = jonObject.getString("downloadurl");
//                    //注意json的格式不要写错了，不同的数据之间用逗号隔开
//                    System.out.println("版本的名字是："+versionname+" 版本号是："+versioncode+" 内容是："+desc+" 下载地址是："+ downurl);
//                    getVersioncode();
//                    if (getVersioncode()<versioncode) {
//                        showDiowlog();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("fail————————");
//                }
//
//
//            }
//
//            @Override
//            public void fail(Exception e) {
//                LogUtil.e(e+"");
//
//
//            }
//        });

    }





    public void showDiowlog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(mcontext);
        dialog.setTitle("更新提示");
        dialog.setMessage(desc);
        dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载
                downurl();
            }
        });
        dialog.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //进入主界面!!!!!
                dialog.dismiss();

            }
        });
        //最后这个一定要加上！！！
        dialog.show();
    }


    public void downurl(){
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        query.setFilterByStatus(DownloadManager.STATUS_RUNNING);//正在下载
        Cursor c = manager.query(query);
        if(c.moveToNext()){
            //正在下载中，不重新下载
        }else {
            DownloadManager.Request down = new DownloadManager.Request(Uri.parse(downurl));
            //设置允许使用的网络类型，这里是移动网络和wifi都可以
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            //显示在下载界面，即下载后的文件在系统下载管理里显示
            down.setVisibleInDownloadsUi(true);
            //设置下载标题
            down.setTitle("快递加");
            //显示Notification
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            //设置下载后文件存放的位置，在SDCard/Android/data/你的应用的包名/files/目录下面
            // down.setDestinationInExternalFilesDir(this, null, DOWNLOAD_FILE_NAME);
            //放在sd卡里面，测试可行！！2017.9.30


//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
//                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/", DOWNLOAD_FILE_NAME);
//                down.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/", DOWNLOAD_FILE_NAME);
//                Uri uri = FileProvider.getUriForFile(MyApplication.mcontext, "com.emiaoqian.express.fileprovider", file);
//
//            }else {

            //// TODO: 2018/5/30  8.0的手机中这个路径是没问题的，但是坑就在不能安装
                //down.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory() + "/msqd/", DOWNLOAD_FILE_NAME);
            down.setDestinationInExternalPublicDir("/test/", DOWNLOAD_FILE_NAME);


               // Environment.DIRECTORY_DOWNLOADS
           // }

            //将下载请求放入队列,返回值为downloadId
            downloadId = manager.enqueue(down);
            //intofirstActivity();
        }

    }
}
