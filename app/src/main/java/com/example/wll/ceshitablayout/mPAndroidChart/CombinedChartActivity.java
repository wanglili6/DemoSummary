package com.example.wll.ceshitablayout.mPAndroidChart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.mannerg.CombinedChartManager;
import com.github.mikephil.charting.charts.CombinedChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 组合柱状图和折线图
 */
public class CombinedChartActivity extends AppCompatActivity {

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
    @BindView(R.id.chart1)
    CombinedChart chart1;
    @BindView(R.id.chart2)
    CombinedChart chart2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combined_chart);
        ButterKnife.bind(this);
        tvTitle.setText("柱状图折线图组合");
        ivBack.setVisibility(View.VISIBLE);
        //x轴数据
        List<String> xData = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
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
        //y轴数据集合
        List<List<Float>> yLineDatas = new ArrayList<>();
        //4种直方图
        for (int i = 0; i < 2; i++) {
            //y轴数
            List<Float> yData = new ArrayList<>();
            for (int j = 0; j <= 20; j++) {
                yData.add((float) (Math.random() * 100));
            }
            yLineDatas.add(yData);
        }
        //名字集合
        List<String> barNames = new ArrayList<>();
        barNames.add("直方图一");
        barNames.add("直方图二");
        //颜色集合
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Random random = new Random();
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            colors.add(Color.rgb(r, g, b));
        }
        //竖状图管理类

        List<String> lineNames = new ArrayList<>();
        lineNames.add("折线图一");
        lineNames.add("折线图二");


        //管理类
        //单条
        CombinedChartManager combineChartManager1 = new CombinedChartManager(chart1, this);
        combineChartManager1.showCombinedChart(xData, yBarDatas.get(0), yLineDatas.get(0),
                "直方图", "线性图", colors.get(0), colors.get(1));
        //多条
        CombinedChartManager combineChartManager2 = new CombinedChartManager(chart2, this);
        combineChartManager2.showCombinedChart(xData, yBarDatas, yLineDatas, barNames, lineNames,
                colors, colors);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
