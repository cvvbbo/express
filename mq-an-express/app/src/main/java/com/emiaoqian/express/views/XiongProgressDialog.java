package com.emiaoqian.express.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emiaoqian.express.R;

/**
 * Created by xiong on 2018/4/23.
 */

public class XiongProgressDialog {

    /**
     *
     *  使用方法
     *  //文字即为显示的内容
     Dialog mDialog = CustomProgressDialog.createLoadingDialog(this, "正在加载中...");
     mDialog.setCancelable(true);//允许返回
     mDialog.show();//显示

     就是普通dialog怎么用，他就怎么用

     作者：哇牛Aaron
     链接：https://www.jianshu.com/p/5b688c8e5a96
     來源：简书
     著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * https://www.jianshu.com/p/5b688c8e5a96
     * 得到自定义的progressDialog
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.load_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }

}




