package com.example.wll.ceshitablayout.mPAndroidChart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.mannerg.BarChartManager;
import com.example.wll.ceshitablayout.mannerg.HoriztolBarChartManager;
import com.github.mikephil.charting.charts.HorizontalBarChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 横着的柱状图
 */
public class HorizitolBarChartActivity extends AppCompatActivity {

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
    @BindView(R.id.horizontal_chart)
    HorizontalBarChart horizontalChart;
    @BindView(R.id.horizontal_chart_2)
    HorizontalBarChart horizontalChart2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizitol_bar_chart);
        ButterKnife.bind(this);
        tvTitle.setText("横着柱状图");
        ivBack.setVisibility(View.VISIBLE);
        //x轴数据
        List<String> xData = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            xData.add(String.valueOf(i));
        }
        //y轴数据集合
        List<List<Float>> yBarDatas = new ArrayList<>();
        //4种直方图
        for (int i = 0; i < 2; i++) {
            //y轴数
            List<Float> yData = new ArrayList<>();
            for (int j = 0; j <= 20; j++) {
                yData.add((float) (Math.random() * 100));
            }
            yBarDatas.add(yData);
        }

        //名字集合
        List<String> barNames = new ArrayList<>();
        barNames.add("直方图一");
        barNames.add("直方图二");
        //颜色集合
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Random random = new Random();
            int r = random.nextInt(200);
            int g = random.nextInt(200);
            int b = random.nextInt(200);
            colors.add(Color.rgb(r, g, b));
        }

        //管理类
        //单条
        HoriztolBarChartManager barChartManager = new HoriztolBarChartManager(horizontalChart, this);
//        barChartManager.getBarData(xData, yBarDatas.get(0), "线性图", colors.get(0));
        barChartManager.setData(10, 50);
        //多条
        HoriztolBarChartManager barChartManager1 = new HoriztolBarChartManager(horizontalChart2, this);
        barChartManager1.getBarData(xData, yBarDatas, barNames, colors);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
