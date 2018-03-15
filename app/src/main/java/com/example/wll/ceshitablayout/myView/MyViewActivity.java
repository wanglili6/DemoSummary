package com.example.wll.ceshitablayout.myView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.view.PieChartView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyViewActivity extends AppCompatActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.rl_select)
    RelativeLayout rlSelect;
    @BindView(R.id.rl_title_bg)
    RelativeLayout rlTitleBg;
    @BindView(R.id.ll_paint)
    PieChartView llPaint;
    private LinkedHashMap kindsMap = new LinkedHashMap<String, Integer>();
    private ArrayList<Integer> colors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_view);
        ButterKnife.bind(this);
        kindsMap.put("苹果", 10);
        kindsMap.put("梨子", 30);
        kindsMap.put("香蕉", 10);
        kindsMap.put("葡萄", 30);
        kindsMap.put("哈密瓜", 10);
        kindsMap.put("猕猴桃", 30);
        kindsMap.put("草莓", 10);
        kindsMap.put("橙子", 30);
        kindsMap.put("火龙果", 10);
        kindsMap.put("椰子", 20);
        for (int i = 1; i <= 40; i++) {
            int r = (new Random().nextInt(100) + 10) * i;
            int g = (new Random().nextInt(100) + 10) * 3 * i;
            int b = (new Random().nextInt(100) + 10) * 2 * i;
            int color = Color.rgb(r, g, b);
            if (Math.abs(r - g) > 10 && Math.abs(r - b) > 10 && Math.abs(b - g) > 10) {
                colors.add(color);
            }
        }
        llPaint.setCenterTitle("水果大拼盘");
        llPaint.setDataMap(kindsMap);
        llPaint.setColors(colors);
        llPaint.setMinAngle(50);
        llPaint.startDraw();
    }
}
