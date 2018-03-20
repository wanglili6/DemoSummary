package com.example.wll.ceshitablayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.apkfuns.logutils.LogUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main3Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<DateBean>list=new ArrayList<>();
    private MyAdapter adapter;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // handler类接收数据
   Handler handler = new Handler() {
      public void handleMessage(Message msg) {
                      if (msg.what == 1) {

                          try {
                              updata();
                          } catch (ParseException e) {
                              e.printStackTrace();
                          }
                      }
                  };
   };
    private static SimpleDateFormat format;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        list.add(new DateBean("电脑", R.mipmap.ic_launcher, 1521275400, "1天1时8分19s"));
        list.add(new DateBean("电视", R.mipmap.ic_launcher, 1521253800, "1天1时8分19s"));
        list.add(new DateBean("电冰箱", R.mipmap.ic_launcher, 1521196200, "1天1时8分19s"));
        list.add(new DateBean("手机", R.mipmap.ic_launcher, 1521369000, "1天1时8分19s"));
        list.add(new DateBean("平板", R.mipmap.ic_launcher, 1521336600, "1天1时8分19s"));
        list.add(new DateBean("电话", R.mipmap.ic_launcher, 1521304200, "1天1时8分19s"));
        list.add(new DateBean("电脑", R.mipmap.ic_launcher, 1521275400, "1天1时8分19s"));
        list.add(new DateBean("电视", R.mipmap.ic_launcher, 1521253800, "1天1时8分19s"));
        list.add(new DateBean("电冰箱", R.mipmap.ic_launcher, 1521196200, "1天1时8分19s"));
        list.add(new DateBean("手机", R.mipmap.ic_launcher, 1521369000, "1天1时8分19s"));
        list.add(new DateBean("平板", R.mipmap.ic_launcher, 1521336600, "1天1时8分19s"));
        list.add(new DateBean("电话", R.mipmap.ic_launcher, 1521304200, "1天1时8分19s"));
        list.add(new DateBean("电脑", R.mipmap.ic_launcher, 1521275400, "1天1时8分19s"));
        list.add(new DateBean("电视", R.mipmap.ic_launcher, 1521253800, "1天1时8分19s"));
        list.add(new DateBean("电冰箱", R.mipmap.ic_launcher, 1521196200, "1天1时8分19s"));
        list.add(new DateBean("手机", R.mipmap.ic_launcher, 1521369000, "1天1时8分19s"));
        list.add(new DateBean("平板", R.mipmap.ic_launcher, 1521336600, "1天1时8分19s"));
        list.add(new DateBean("电话", R.mipmap.ic_launcher, 1521304200, "1天1时8分19s"));
        list.add(new DateBean("电脑", R.mipmap.ic_launcher, 1521275400, "1天1时8分19s"));
        list.add(new DateBean("电视", R.mipmap.ic_launcher, 1521253800, "1天1时8分19s"));
        list.add(new DateBean("电冰箱", R.mipmap.ic_launcher, 1521196200, "1天1时8分19s"));
        list.add(new DateBean("手机", R.mipmap.ic_launcher, 1521369000, "1天1时8分19s"));
        list.add(new DateBean("平板", R.mipmap.ic_launcher, 1521336600, "1天1时8分19s"));
        list.add(new DateBean("电话", R.mipmap.ic_launcher, 1521304200, "1天1时8分19s"));
        recyclerView = (RecyclerView) findViewById(R.id.rRecyclerView);

        LinearLayoutManager mrg = new LinearLayoutManager(Main3Activity.this);
        mrg.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mrg);
        adapter = new MyAdapter(list, Main3Activity.this);
        recyclerView.setAdapter(adapter);
        thread = new Thread(new ThreadShow());
        thread.start();
    }


    private void updata() throws ParseException {
        long l = System.currentTimeMillis()/1000;
        String s = formatData("yyyy-MM-dd HH:mm:ss", l);
        for (int i=0; i<list.size();i++){
            String s1 = formatData("yyyy-MM-dd HH:mm:ss", list.get(i).getTime());
            Date d1 = df.parse(s1);
            Date d2 = df.parse(s);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            if(diff<0){
                continue;
            }
            long days = diff / (1000 * 60 * 60 * 24);//天
            long hours = (diff - days * (1000 * 60 * 60 * 24))
                    / (1000 * 60 * 60);//时
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    / (1000 * 60);//分
            long millisecond =(diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    % (1000*60);//秒
            long second = millisecond / 1000;
            if(days==0){
                list.get(i).setTvtime( hours + "小时" + minutes + "分"+second+"秒");
            }
            if(days==0&&hours==0){
                list.get(i).setTvtime( minutes + "分"+second+"秒");
            }
            if(days==0&&hours==0&&minutes==0){
                list.get(i).setTvtime(second+"秒");
            }
            if(days==0&&hours==0&&minutes==0&&second==0){
                list.get(i).setTvtime("活动结束");
            }
            if(days!=0&&hours!=0&&minutes!=0&&second!=0){
                list.get(i).setTvtime("" + days + "天" + hours + "小时" + minutes + "分"+second+"秒");
            }

            LogUtils.d("时间间隔"+ list.get(i).getTvtime());
        }
        adapter.notifyDataSetChanged();
    }
    public static String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        String result = "";
        format = new SimpleDateFormat(dataFormat);
        result = format.format(new Date(timeStamp));
        return result;
    }

    // 线程类
    class ThreadShow implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (Exception e) {

                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(thread!=null){
            thread.interrupt();
            System.gc();
        }
    }
}
