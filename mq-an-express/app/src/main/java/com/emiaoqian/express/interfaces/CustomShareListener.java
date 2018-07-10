package com.emiaoqian.express.interfaces;


import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.ToastUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;


/**
 * Created by xiong on 2017/10/19.
 */


//这个是友盟分享的
public class CustomShareListener implements UMShareListener {

    @Override
    public void onStart(SHARE_MEDIA platform) {

    }

    @Override
    public void onResult(SHARE_MEDIA platform) {

        if (platform.name().equals("WEIXIN_FAVORITE")) {
            //Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();

            ToastUtil.showToast("收藏成功啦");
        } else {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
               // Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                ToastUtil.showToast("分享成功");
            }

        }
    }

    @Override
    public void onError(SHARE_MEDIA platform, Throwable t) {
        if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                && platform != SHARE_MEDIA.EMAIL
                && platform != SHARE_MEDIA.FLICKR
                && platform != SHARE_MEDIA.FOURSQUARE
                && platform != SHARE_MEDIA.TUMBLR
                && platform != SHARE_MEDIA.POCKET
                && platform != SHARE_MEDIA.PINTEREST

                && platform != SHARE_MEDIA.INSTAGRAM
                && platform != SHARE_MEDIA.GOOGLEPLUS
                && platform != SHARE_MEDIA.YNOTE
                && platform != SHARE_MEDIA.EVERNOTE) {
           // Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            ToastUtil.showToast("分享失败");
            if (t != null) {
                LogUtil.d("throw", "throw:" + t.getMessage());
            }
        }

    }

    @Override
    public void onCancel(SHARE_MEDIA platform) {

    }
}
