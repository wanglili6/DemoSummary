package com.example.wll.ceshitablayout.mPAndroidChart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.mannerg.PieChartManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PieChartActivity extends AppCompatActivity {

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
    PieChart chart1;
    @BindView(R.id.chart2)
    PieChart chart2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        ButterKnife.bind(this);
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<PieEntry> entries1 = new ArrayList<PieEntry>();

//颜色集合
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<String> lables = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Random random = new Random();
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            colors.add(Color.rgb(r, g, b));
            lables.add(i + "月");
        }
        for (int i = 1; i <= 5; i++) {
            int all = 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10;
            int data = i * new Random().nextInt(10) + 1;
            /**
             * 参数一:比例
             * 图例的名字
             * 携带的数据---赋的值
             */
            PieEntry pieEntry = new PieEntry(i, lables.get(i - 1), data);
            LogUtils.d("数据" + data);
            entries.add(pieEntry);
        }

        for (int i = 1; i <= 10; i++) {
            int all = 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10;

            /**
             * 参数一:比例
             * 图例的名字
             * 携带的数据
             */
            PieEntry pieEntry = new PieEntry(i, lables.get(i - 1), i > 5);
            entries1.add(pieEntry);
        }


        /**
         * 下面注释的位置与上面显示一样，为向外文字显示
         */
        PieChartManager pieChartEntity = new PieChartManager(chart1, entries, lables, colors, 12f, Color.GRAY, PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieChartEntity.setHoleEnabled(Color.TRANSPARENT, 40f, Color.parseColor("#11ffffff"), 50f);
        pieChartEntity.setLegendEnabled(true);
        pieChartEntity.setPercentValues(true);

        PieChartManager pieChartEntity1 = new PieChartManager(chart2, entries1, lables, colors, 12f, Color.GRAY, PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieChartEntity1.setHoleEnabled(Color.TRANSPARENT, 40f, Color.parseColor("#11ffffff"), 50f);
        pieChartEntity1.setLegendEnabled(true);
        pieChartEntity1.setPercentValues(true);
        chart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                e.getData();
                LogUtils.d("改变的数据:" + e.getData().toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
