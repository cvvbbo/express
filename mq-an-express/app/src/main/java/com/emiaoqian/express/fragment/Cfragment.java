package com.emiaoqian.express.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.emiaoqian.express.R;
import com.emiaoqian.express.interfaces.Myinterface;
import com.emiaoqian.express.utils.LogUtil;

/**
 * Created by xiong on 2018/4/11.
 */

public class Cfragment extends Fragment {


    private Button bt1;
    private Button bt2;
    private View view;


    public static Myinterface.HiddenButtonSheet hiddenButtonSheet;

    public static void sethiddenButtonSheetcallback(Myinterface.HiddenButtonSheet hiddenButtonSheet){
        Cfragment.hiddenButtonSheet=hiddenButtonSheet;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.e("10086当前状态setUserVisibleHint"+getUserVisibleHint()+"--"+getClass().getSimpleName());
        if (isVisibleToUser){
            hiddenButtonSheet.HiddenButtonSheetcallback(true);
        }
    }

    //只有这个软件能调
    @Override
    public boolean getUserVisibleHint() {
        LogUtil.e("10086当前状态getUserVisibleHint()"+super.getUserVisibleHint()+"--"+getClass().getSimpleName());
        return super.getUserVisibleHint();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.d_fragment, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        LogUtil.e("10086onDestroy--"+getClass().getSimpleName());
        super.onDestroy();
    }

    @Override
    public void onPause() {
        LogUtil.e("10086onPause--"+getClass().getSimpleName());
        super.onPause();
    }

    @Override
    public void onResume() {
        LogUtil.e("10086onResume--"+getClass().getSimpleName());
        super.onResume();
    }
}
