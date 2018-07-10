package com.emiaoqian.express.utils;

import android.media.MediaRecorder;
import android.os.Handler;

import java.io.File;
import java.io.IOException;

/**
 * Created by xiong on 2018/3/26.
 *
 *   这个是录音类
 *
 *
 */

public class AudioRecorderUtil {
    private final String TAG = AudioRecorderUtil.class.getName();
    public static final int MAX_LENGTH = 60000;
    private String filePath;
    private String folderPath;
    private MediaRecorder mMediaRecorder;
    private int maxLength;
    private long startTime;
    private long endTime;
    private AudioRecorderUtil.OnAudioStatusUpdateListener audioStatusUpdateListener;
    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            AudioRecorderUtil.this.updateMicStatus();
        }
    };
    private int BASE = 1;
    private int SPACE = 100;

    public AudioRecorderUtil(String folderPath) {
        File path = new File(folderPath);
        if(!path.exists()) {
            path.mkdirs();
        }

        this.folderPath = folderPath;
        //设置时间为59s，其实就是60秒 3.30
        this.maxLength = '\uea59';
    }

    //原来这个是个空类型的方法 6.13
    public boolean start() {
        if(this.mMediaRecorder == null) {
            this.mMediaRecorder = new MediaRecorder();
        } else {
            try {
                this.mMediaRecorder.stop();
                this.mMediaRecorder.reset();
                this.mMediaRecorder.release();
            } catch (Exception var2) {
                this.mMediaRecorder.reset();

            }
        }

        this.mHandler.removeCallbacks(this.mUpdateMicStatusTimer);

        try {
//            this.mMediaRecorder.setAudioSource(1);
//            this.mMediaRecorder.setOutputFormat(0);
//            this.mMediaRecorder.setAudioEncoder(1);
            //这个是在网页上能播放的mp3格式，虽然后缀名是一样的，但是编码不一样，就会造成这可能不是一个mp3文件在网页中，虽然在各大主流播放器里面能够播放
            //
            this.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            this.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            this.filePath = this.folderPath + File.separator + "message_voice" + ".mp3";
            this.mMediaRecorder.setOutputFile(this.filePath);
            this.mMediaRecorder.setMaxDuration(this.maxLength);
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    if(what == 800) {
                        AudioRecorderUtil.this.stop();
                    }

                }
            });
            this.mMediaRecorder.start();
            this.startTime = System.currentTimeMillis();
            this.updateMicStatus();
            if(this.audioStatusUpdateListener != null) {
                this.audioStatusUpdateListener.onStart();
            }
        } catch (IllegalStateException var3) {
            if(this.audioStatusUpdateListener != null) {
                this.audioStatusUpdateListener.onError(var3);
            }

            this.cancel();
        } catch (IOException var4) {
            if(this.audioStatusUpdateListener != null) {
                this.audioStatusUpdateListener.onError(var4);
            }

            this.cancel();
            //这个是后来自己加的，因为6.0的动态权限问题 6.13
            return false;
        }
        //这个是后来自己加的，因为6.0的动态权限问题 6.13
        return true;

    }

    public long getSumTime() {
        return this.startTime == 0L?0L:System.currentTimeMillis() - this.startTime;
    }

    public long stop() {
        if(this.mMediaRecorder == null) {
            return 0L;
        } else {
            this.endTime = System.currentTimeMillis();

            try {
                this.mMediaRecorder.stop();
                this.mMediaRecorder.reset();
                this.mMediaRecorder.release();
                this.mMediaRecorder = null;
                if(this.audioStatusUpdateListener != null) {
                    this.audioStatusUpdateListener.onStop(this.filePath);
                }
            } catch (RuntimeException var3) {
                this.mMediaRecorder.reset();
                this.mMediaRecorder.release();
                this.mMediaRecorder = null;
                File file = new File(this.filePath);
                if(file.exists()) {
                    file.delete();
                }

                if(this.audioStatusUpdateListener != null) {
                    this.audioStatusUpdateListener.onError(var3);
                }
            }

            this.filePath = "";
            return this.endTime - this.startTime;
        }
    }

    /***
     * 这个原来的方法是空方法，但是为了后来的动态权限，改成了boolean类型了。
     *
     * @return
     */
    public boolean cancel() {
        try {
            this.mMediaRecorder.stop();
            this.mMediaRecorder.reset();
            this.mMediaRecorder.release();
            this.mMediaRecorder = null;
        } catch (RuntimeException var2) {
//            this.mMediaRecorder.reset();
//            this.mMediaRecorder.release();
//            this.mMediaRecorder = null;
            ToastUtil.showToastCenter("录音相关权限获取失败,->请去设置中打开相关权限");
            return false;
        }

        File file = new File(this.filePath);
        if(file.exists()) {
            file.delete();
        }

        this.filePath = "";
        if(this.audioStatusUpdateListener != null) {
            this.audioStatusUpdateListener.onCancel();
        }
        return true;

    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    private void updateMicStatus() {
        if(this.mMediaRecorder != null) {
            double ratio = (double)this.mMediaRecorder.getMaxAmplitude() / (double)this.BASE;
            double db = 0.0D;
            if(ratio > 1.0D) {
                db = 20.0D * Math.log10(ratio);
                if(null != this.audioStatusUpdateListener) {
                    this.audioStatusUpdateListener.onProgress(db, System.currentTimeMillis() - this.startTime);
                }
            }

            this.mHandler.postDelayed(this.mUpdateMicStatusTimer, (long)this.SPACE);
        }

    }

    public void setOnAudioStatusUpdateListener(AudioRecorderUtil.OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    public interface OnAudioStatusUpdateListener {
        void onStart();

        void onProgress(double var1, long var3);

        void onError(Exception var1);

        void onCancel();

        void onStop(String var1);
    }
}
