package com.emiaoqian.express.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.emiaoqian.express.R;
import com.emiaoqian.express.utils.LogUtil;
import com.emiaoqian.express.utils.MakephotoUtils;
import com.emiaoqian.express.utils.ToastUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;


/**
 * Created by xiong on 2018/3/28.
 */


//下面这篇dialog是鸿洋的dialogfragment
public class SavephotoDiaologFragment extends android.support.v4.app.DialogFragment {

    public static SavephotoDiaologFragment newstance(String url){
        SavephotoDiaologFragment fragment=new SavephotoDiaologFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ListView lv;

    @Override
    public void onResume() {
        //Log.e("66onResume","SigntvDialogFragment");
        super.onResume();
        int width = getActivity().getResources().getDimensionPixelOffset(R.dimen.x150);
        int height = getActivity().getResources().getDimensionPixelOffset(R.dimen.x100);
//        getDialog().getWindow().setLayout(width,height);
        //点击外部不可取消
       // getDialog().setCanceledOnTouchOutside(false);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        window.setLayout(width,height);
        windowParams.dimAmount = 0.5f;
        window.setAttributes(windowParams);



    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.save_webview_dialog_view,null);
        lv = (ListView) view.findViewById(R.id.lv);
        lv.setAdapter(new Listviewadapter());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getArguments()!=null&&position==0){
                    final String url = getArguments().getString("url");
                    Picasso.with(getActivity())
                            .load(url)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    String[] str = url.split("/");
                                    String fileName = str[str.length - 1];
                                    //点一定要转义，不然不识别
                                    String[] split = fileName.split("\\.");
                                    LogUtil.e(split+"");
                                    String realname = split[0];
                                    //MakephotoUtils.saveImage(getActivity(),bitmap,fileName);
                                    File file = MakephotoUtils.saveImageofalbum(getActivity(), bitmap, realname+".png");
                                    if (file.exists()){
                                        ToastUtil.showToastCenter("保存成功");
                                    }else {
                                        ToastUtil.showToastCenter("保存失败");
                                    }
                                    dismiss();
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                }else {
                    dismiss();
                }
            }
        });

        builder.setView(view);




        return builder.create();
       // return dialog;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);


    }

    class Listviewadapter extends BaseAdapter{

        private TextView tv;

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(parent.getContext(), R.layout.my_dialog_item_view, null);
            tv = (TextView) view.findViewById(R.id.tv);
            if (position==0){
                tv.setText("保存图片");
            }else if (position==1){
                tv.setText("取消");
            }
            return view;
        }
    }
}
