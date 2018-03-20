package com.example.wll.ceshitablayout;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by newnet on 2018/3/16.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    ArrayList<DateBean>list;
    Context context;

    public MyAdapter(ArrayList<DateBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =View.inflate(context, R.layout.item3,null);
        return new MyHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
      holder.tvname.setText(list.get(position).getName());
      holder.ivpic.setImageDrawable(context.getDrawable(list.get(position).getId()));
        holder.tvtime.setText(list.get(position).getTvtime());




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView tvname;
        TextView tvtime;
        ImageView ivpic;
        public MyHolder(View itemView) {
            super(itemView);
            tvname = (TextView) itemView.findViewById(R.id.tvname);
            tvtime = (TextView) itemView.findViewById(R.id.tvtime);
            ivpic  = (ImageView) itemView.findViewById(R.id.ivpic);
        }
    }
    public static String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        result = format.format(new Date(timeStamp));
        return result;
    }
}
