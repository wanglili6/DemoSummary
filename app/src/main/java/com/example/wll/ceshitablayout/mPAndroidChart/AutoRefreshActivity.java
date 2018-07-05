package com.example.wll.ceshitablayout.mPAndroidChart;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.mannerg.LineChartManager;
import com.github.mikephil.charting.charts.LineChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AutoRefreshActivity extends AppCompatActivity {

    @BindView(R.id.line_chart)
    LineChart lineChart;
    private int rgb;
    ArrayList<String> xValueList = new ArrayList<>();
    ArrayList<Integer> yValueList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == 1) {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                String format = simpleDateFormat.format(date);
                xValueList.add(format);
                Random random = new Random();
                int i1 = random.nextInt(10);
                yValueList.add(i1);
                //管理类
                //单条
                LineChartManager lineChartManager = new LineChartManager(lineChart, AutoRefreshActivity.this);
                lineChartManager.setAnimation(false);
                lineChartManager.setData(xValueList, yValueList, "wll", rgb, 4);

            }
        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_refresh);
        ButterKnife.bind(this);
        //延时操作
        //添加x轴数据
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            xValueList.add("11.3" + i);
        }
        for (int a = 0; a < xValueList.size(); a++) {
            int i1 = random.nextInt(10);
            yValueList.add(a * i1);
        }
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        rgb = Color.rgb(r, g, b);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, 0, 5000);


    }

}
