package com.example.wll.ceshitablayout;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.view.CustomMarkerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
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
    ArrayList<String> xValueList = new ArrayList<>();
    ArrayList<Integer> yValueList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);
        ButterKnife.bind(this);
        //设置折线图的属性
        setChartProperties();
        //添加x轴数据
        for (int i = 0; i < 10; i++) {
            xValueList.add(i + "月");
            yValueList.add(i * 10);
        }
        setChartXAxis();
        setChartYAxis();
        setData(xValueList, yValueList);
        updateLegendOrientation(Legend.LegendVerticalAlignment.BOTTOM, Legend.LegendHorizontalAlignment.RIGHT, Legend.LegendOrientation.HORIZONTAL);
    }

    private void setChartProperties() {
        //设置描述文本不显示
        lineChart.getDescription().setEnabled(false);
        //设置是否显示表格背景
        lineChart.setDrawGridBackground(true);
        //设置是否可以触摸
        lineChart.setTouchEnabled(true);
        lineChart.setDragDecelerationFrictionCoef(0.9f);
        //设置是否可以拖拽
        lineChart.setDragEnabled(true);
        //设置是否可以缩放
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setPinchZoom(true);
        //设置背景颜色
        lineChart.setBackgroundColor(Color.parseColor("#66f0f0f0"));
        //设置一页最大显示个数为6，超出部分就滑动
        float ratio = (float) xValueList.size() / (float) 6;
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        lineChart.zoom(ratio, 1f, 0, 0);
        //设置从X轴出来的动画时间
        //lineChart.animateX(1500);
        //设置XY轴动画
        lineChart.animateXY(1500, 1500, Easing.EasingOption.EaseInSine, Easing.EasingOption.EaseInSine);
        CustomMarkerView marker = new CustomMarkerView(this, R.layout.marck_view);
        lineChart.setMarker(marker);
    }

    /**
     * 设置X轴属性
     */
    private void setChartXAxis() {
        //自定义设置横坐标
        IAxisValueFormatter xValueFormatter = new FastBrowserXValueFormatter(xValueList);
        //X轴
        XAxis xAxis = lineChart.getXAxis();
        //设置线为虚线
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置字体大小10sp
        xAxis.setTextSize(10f);
        //设置X轴字体颜色
        xAxis.setTextColor(Color.BLACK);
        //设置从X轴发出横线
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.parseColor("#f0f0f0"));
        //设置网格线宽度
        xAxis.setGridLineWidth(1);
        //设置显示X轴
        xAxis.setDrawAxisLine(true);
        //设置X轴显示的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置自定义X轴值
        xAxis.setValueFormatter(xValueFormatter);
        //一个界面显示6个Lable，那么这里要设置11个
        xAxis.setLabelCount(10);
        //设置最小间隔，防止当放大时出现重复标签
        xAxis.setGranularity(1f);
        //设置为true当一个页面显示条目过多，X轴值隔一个显示一个
        xAxis.setGranularityEnabled(true);
        //设置X轴的颜色
        xAxis.setAxisLineColor(Color.BLACK);
        //设置X轴的宽度
        xAxis.setAxisLineWidth(1f);
        lineChart.invalidate();
    }

    private void setChartYAxis() {
        YAxis leftAxis = lineChart.getAxisLeft();
        //设置线为虚线
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置从Y轴发出横向直线(网格线)
//        leftAxis.setDrawGridLines(true);
        //设置网格线的颜色
        leftAxis.setGridColor(Color.parseColor("#f0f0f0"));
        //设置网格线宽度
        leftAxis.setGridLineWidth(1);
        //设置Y轴最小值是0，从0开始
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularityEnabled(true);
        //设置最小间隔，防止当放大时出现重复标签
        leftAxis.setGranularity(1f);
        //如果沿着轴线的线应该被绘制，则将其设置为true,隐藏Y轴
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setTextSize(10f);
        leftAxis.setTextColor(Color.BLACK);
        //设置左边X轴显示
        leftAxis.setEnabled(true);
        //设置Y轴的颜色
        leftAxis.setAxisLineColor(Color.BLACK);
        //设置Y轴的宽度
        leftAxis.setAxisLineWidth(1f);

        YAxis rightAxis = lineChart.getAxisRight();
        //设置右边Y轴不显示
        rightAxis.setEnabled(false);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    private void setData(ArrayList<String> xValueList, ArrayList<Integer> yValueList) {
        ArrayList<Entry> yValues = new ArrayList<>();
        for (int i = 0; i < xValueList.size(); i++) {
            yValues.add(new Entry(i, yValueList.get(i)));
        }

        LineDataSet set;
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set.setValues(yValues);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            //设置值给图表
            set = new LineDataSet(yValues, "图标");
            //设置图标不显示
            set.setDrawIcons(false);
            //设置Y值使用左边Y轴的坐标值
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            //设置线的颜色
            set.setColors(getResources().getColor(R.color.colorPrimaryDark));
            //设置数据点圆形的颜色
            set.setCircleColor(Color.GREEN);
            //设置填充圆形中间的颜色
            set.setCircleColorHole(Color.WHITE);
            //设置折线宽度
            set.setLineWidth(1f);
            //设置折现点圆点半径
            set.setCircleRadius(4f);
            //设置十字线颜色
            //set.setHighLightColor(Color.parseColor("#47DBCC"));
            //设置显示十字线，必须显示十字线，否则MarkerView不生效
            set.setHighlightEnabled(true);
            //设置是否在数据点中间显示一个孔
            set.setDrawCircleHole(true);

            //设置填充
            //设置允许填充
            set.setDrawFilled(true);
            set.setFillAlpha(50);
            //设置背景渐变
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                //设置渐变
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set.setFillDrawable(drawable);
            } else {
                set.setFillColor(Color.RED);
            }

            LineData data = new LineData(set);
            //设置不显示数据点的值
            data.setDrawValues(true);

            lineChart.setData(data);
            lineChart.invalidate();
        }
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

    private class FastBrowserXValueFormatter implements IAxisValueFormatter {

        public FastBrowserXValueFormatter(List<String> xValueList) {
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            LogUtils.d("折线图", "----->getFormattedValue: " + value);
            return xValueList.get((int) value % xValueList.size());
        }
    }


}
