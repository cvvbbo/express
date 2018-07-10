package com.emiaoqian.express.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emiaoqian.express.R;

/**
 * Created by xiong on 2018/4/19.
 */

public class MenuListviewadapter extends BaseAdapter {

    String[] menutitle;
    int[] iconims;

    public MenuListviewadapter(String[] menutitle,int[] iconims) {
        this.menutitle=menutitle;
        this.iconims=iconims;
    }

    @Override
    public int getCount() {
        return menutitle.length;
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
        View view = View.inflate(parent.getContext(), R.layout.menu_list_view, null);
        ImageView icon= (ImageView) view.findViewById(R.id.icon);
        TextView icon_name= (TextView) view.findViewById(R.id.icon_name);
        icon_name.setText(menutitle[position]);
        icon.setImageResource(iconims[position]);
        return view;
    }
}
