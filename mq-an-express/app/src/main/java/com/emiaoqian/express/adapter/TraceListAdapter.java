package com.emiaoqian.express.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emiaoqian.express.R;
import com.emiaoqian.express.bean.Trace;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiong on 2018/4/21.
 */



public class TraceListAdapter extends BaseAdapter {
    private Context context;
    private List<Trace> traceList = new ArrayList<>();
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;

    public TraceListAdapter(Context context, List<Trace> traceList) {
        this.context = context;
        this.traceList = traceList;
    }

    @Override
    public int getCount() {
        return traceList.size();
    }

    @Override
    public Trace getItem(int position) {
        return traceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Trace trace = getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trace, parent, false);
            //接受时间
            holder.tvAcceptTime = (TextView) convertView.findViewById(R.id.tvAcceptTime);
            //快件查询
            holder.tvAcceptStation = (TextView) convertView.findViewById(R.id.tvAcceptStation);
            //这个是最上面的那根线 4.21
            holder.tvTopLine = (TextView) convertView.findViewById(R.id.tvTopLine);
            //时间线上的小点 4.21
            holder.tvDot = (TextView) convertView.findViewById(R.id.tvDot);
            // 最新标志（就是最新的一条快递信息旁边有个最新的标志）
            holder.tv_new = (TextView) convertView.findViewById(R.id.tv_new);
            convertView.setTag(holder);
        }

        if (getItemViewType(position) == TYPE_TOP) {
            // 第一行头的竖线不显示
            holder.tvTopLine.setVisibility(View.INVISIBLE);
            holder.tv_new.setVisibility(View.VISIBLE);
            // 字体颜色加深
            holder.tvAcceptTime.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvAcceptStation.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_secord);
        } else if (getItemViewType(position) == TYPE_NORMAL) {
            holder.tvTopLine.setVisibility(View.VISIBLE);
            holder.tv_new.setVisibility(View.INVISIBLE);
            holder.tvAcceptTime.setTextColor(0xff999999);
            holder.tvAcceptStation.setTextColor(0xff999999);
            holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
        }

        holder.tvAcceptTime.setText(trace.getAcceptTime());
        holder.tvAcceptStation.setText(trace.getAcceptStation());
        return convertView;
    }

    // 多条目布局 头布局不显示最上面的线
    @Override
    public int getItemViewType(int id) {
        if (id == 0) {
            return TYPE_TOP;
        }
        return TYPE_NORMAL;
    }

    static class ViewHolder {
        public TextView tvAcceptTime, tvAcceptStation;
        public TextView tvTopLine, tvDot,tv_new;
    }
}

