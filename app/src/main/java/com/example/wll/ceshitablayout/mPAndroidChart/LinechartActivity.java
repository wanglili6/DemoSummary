package com.example.wll.ceshitablayout.mPAndroidChart;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.mannerg.CombinedChartManager;
import com.example.wll.ceshitablayout.mannerg.LineChartManager;
import com.example.wll.ceshitablayout.view.CustomMarkerView;
import com.example.wll.ceshitablayout.view.FastBrowserXValueFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 折线图
 */
public class LinechartActivity extends AppCompatActivity {

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
    @BindView(R.id.line_chart)
    LineChart lineChart;
    @BindView(R.id.line_chart_2)
    LineChart lineChart2;
    ArrayList<String> xValueList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<Integer> yValueList = new ArrayList<>();
    ArrayList<Integer> colorls = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);
        ButterKnife.bind(this);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("折线图");
        xValueList.clear();
        //添加x轴数据
        for (int i = 0; i < 10; i++) {
            xValueList.add(i + "月");
        }
        for (int i = 0; i < 4; i++) {
            Random random = new Random();
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            colorls.add(Color.rgb(r, g, b));
        }

        //设置多个
        ArrayList<ArrayList<Integer>> allYValueList = new ArrayList<>();
        nameList.clear();
        nameList.add("采购金额");
        nameList.add("采购量");
        nameList.add("退货金额");
        nameList.add("退货量");
        allYValueList.clear();
        Random random = new Random();
        for (int i = 0; i < nameList.size(); i++) {
            ArrayList<Integer> element = new ArrayList<>();
            for (int a = 1; a < 11; a++) {
                int i1 = random.nextInt(10);
                element.add(a * i1);
            }

            allYValueList.add(i, element);
        }


        //管理类
        //单条
        LineChartManager lineChartManager = new LineChartManager(lineChart, this);
        lineChartManager.setData(xValueList, allYValueList.get(0), "wll", colorls.get(0), 8);
        //多条
        LineChartManager lineChartManager1 = new LineChartManager(lineChart2, this);
        lineChartManager1.setData(xValueList, allYValueList, nameList, colorls, 8);
        //设置标签
        updateLegendOrientation(Legend.LegendVerticalAlignment.BOTTOM, Legend.LegendHorizontalAlignment.RIGHT, Legend.LegendOrientation.HORIZONTAL);

    }


    /**
     * <p>图例说明</p>
     *
     * @param vertical    垂直方向位置 默认底部
     * @param horizontal  水平方向位置 默认右边
     * @param orientation 显示方向 默认水平展示
     */

    public void updateLegendOrientation(Legend.LegendVerticalAlignment vertical, Legend.LegendHorizontalAlignment horizontal, Legend.LegendOrientation orientation) {
        Legend l = lineChart.getLegend();
        l.setVerticalAlignment(vertical);
        l.setHorizontalAlignment(horizontal);
        l.setOrientation(orientation);
        l.setDrawInside(false);

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }


}
