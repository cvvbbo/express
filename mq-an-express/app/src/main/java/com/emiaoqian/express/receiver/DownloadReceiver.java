package com.emiaoqian.express.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;


import com.emiaoqian.express.application.MyApplication;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.ToastUtil;

import java.io.File;

/**
 * Created by xiong on 2017/9/7.
 */

public class DownloadReceiver extends BroadcastReceiver {

    private DownloadManager manager ;
    @Override
    public void onReceive(Context context, Intent intent) {
        manager =(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            //通过downloadId去查询下载的文件名
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.e("---",downloadId+"");
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor myDownload = manager.query(query);
            if (myDownload.moveToFirst()) {
                String fileName=null;

                if (Build.VERSION.SDK_INT>Build.VERSION_CODES.N) {
                    int columnIndex = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    LogUtil.e(columnIndex+"");
                    String string = myDownload.getString(columnIndex);
                    LogUtil.e(string+"");
                    //这个是国外大神发现的骚操作
                    //https://stackoverflow.com/questions/38839688/downloadmanager-column-local-filename-deprecated
                    String replace = string.replace("file://", "");
                    LogUtil.e(replace+"");
                    fileName=replace;

                }else {

                    //在7.0的系统这里会报错！！！
                    int fileNameIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                     fileName = myDownload.getString(fileNameIdx);
                    LogUtil.e(fileName+"");
                }

                if (fileName==null){
                    ToastUtil.showToast("服务器出错了~请移步去应用市场下载");
                    return;
                }
                installAPK(fileName,context);
            }
        }
    }

    //安装APK
    private void installAPK(String  filePath,Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.N){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);//广播里面操作需要加上这句，存在于一个独立的栈里
            Uri contentUri = FileProvider.getUriForFile(MyApplication.mcontext, MyApplication.mcontext.getPackageName() + ".fileprovider", new File(filePath));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        }else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//广播里面操作需要加上这句，存在于一个独立的栈里
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}