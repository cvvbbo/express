package com.emiaoqian.express.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;


import com.emiaoqian.express.R;

/**
 * Created by xiong on 2018/5/24.
 */

public class Myservice extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher); // 这里使用的系统默认图标，可自行更换
        builder.setTicker("您有一条新消息！");
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("这是第一行标题栏");
        builder.setContentText("这里是第二行，用来显示主要内容");
        Notification build = builder.build();
        startForeground(1001, build);
        return super.onStartCommand(intent, flags, startId);
    }
}
