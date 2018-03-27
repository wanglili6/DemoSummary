package com.example.wll.ceshitablayout.mannerg;

import android.content.Context;
import android.graphics.Color;

import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.view.CustomMarkerView;
import com.example.wll.ceshitablayout.view.FastBrowserXValueFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phillip on 2018/3/26.
 * 横着的柱状图
 */

public class HoriztolBarChartManager {
    private HorizontalBarChart mBarChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private Context mContext;

    public HoriztolBarChartManager(HorizontalBarChart mBarChart, Context mContext) {
        this.mBarChart = mBarChart;
        this.mContext = mContext;
        leftAxis = mBarChart.getAxisLeft();
        rightAxis = mBarChart.getAxisRight();
        xAxis = mBarChart.getXAxis();
        this.mContext = mContext;
    }

    /**
     * 设置折线图的属性
     */
    private void setChartProperties() {
        //设置描述文本不显示
        mBarChart.getDescription().setEnabled(false);
        //设置是否显示表格背景
        mBarChart.setDrawGridBackground(true);
        //设置是否可以触摸
        mBarChart.setTouchEnabled(true);
        mBarChart.setDragDecelerationFrictionCoef(0.9f);
        //设置是否可以拖拽
        mBarChart.setDragEnabled(true);
        //设置是否可以缩放
        mBarChart.setScaleEnabled(true);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setHighlightPerDragEnabled(true);
        mBarChart.setPinchZoom(true);
        //设置背景颜色
        mBarChart.setBackgroundColor(Color.parseColor("#66f0f0f0"));
        //设置从X轴出来的动画时间
        //mBarChart.animateX(1500);
        //设置XY轴动画
        mBarChart.animateXY(1500, 1500, Easing.EasingOption.EaseInSine, Easing.EasingOption.EaseInSine);
        CustomMarkerView marker = new CustomMarkerView(mContext, R.layout.marck_view);
        mBarChart.setMarker(marker);
    }

    /**
     * 设置X轴属性
     */
    private void setChartXAxis(HorizontalBarChart mBarChart, List<String> xValueList) {
        //自定义设置横坐标
        IAxisValueFormatter xValueFormatter = new FastBrowserXValueFormatter(xValueList);
        //X轴
        XAxis xAxis = mBarChart.getXAxis();
        //设置线为虚线
//        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setDrawGridLines(true);
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
        mBarChart.invalidate();
    }

    /**
     * 设置Y轴的属性
     */
    private void setChartYAxis(HorizontalBarChart mBarChart) {
        YAxis leftAxis = mBarChart.getAxisLeft();
        //设置线为虚线
//        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置从Y轴发出横向直线(网格线)
        leftAxis.setDrawGridLines(true);
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

        YAxis rightAxis = mBarChart.getAxisRight();
        //设置右边Y轴不显示
        rightAxis.setEnabled(false);
    }

    /**
     * 得到柱状图
     *
     * @param barChartY Y轴值
     * @param barName   柱状图名字
     * @param barColor  柱状图颜色
     * @return
     */

    public void getBarData(List<String> xAxisValues, List<Float> barChartY, String barName, int barColor) {
        setChartProperties();
        setChartXAxis(mBarChart, xAxisValues);
        setChartYAxis(mBarChart);
        BarData barData = new BarData();
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < barChartY.size(); i++) {
            yValues.add(new BarEntry(i, barChartY.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(yValues, barName);
        barDataSet.setColor(barColor);
        barDataSet.setValueTextSize(10f);
        barDataSet.setValueTextColor(barColor);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barData.addDataSet(barDataSet);

        //以下是为了解决 柱状图 左右两边只显示了一半的问题 根据实际情况 而定
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum((float) (barChartY.size() - 0.5));
        mBarChart.setData(barData);

        //设置一页最大显示个数为6，超出部分就滑动
        float ratio = (float) xAxisValues.size() / (float) 8;
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        mBarChart.zoom(ratio, 1f, 0, 0);
        //设置从X轴出来的动画时间
        //lineChart.animateX(1500);
        //设置XY轴动画
        mBarChart.animateXY(1500, 1500, Easing.EasingOption.EaseInSine, Easing.EasingOption.EaseInSine);
        CustomMarkerView marker = new CustomMarkerView(mContext, R.layout.marck_view);
        mBarChart.setMarker(marker);
        //刷新数据
        mBarChart.invalidate();
    }


    /**
     * 得到柱状图(多条)
     *
     * @param barChartYs Y轴值
     * @param barNames   柱状图名字
     * @param barColors  柱状图颜色
     * @return
     */

    public void getBarData(List<String> xAxisValues, List<List<Float>> barChartYs, List<String> barNames, List<Integer> barColors) {
        setChartProperties();
        setChartXAxis(mBarChart, xAxisValues);
        setChartYAxis(mBarChart);
        List<IBarDataSet> lists = new ArrayList<>();
        for (int i = 0; i < barChartYs.size(); i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();

            for (int j = 0; j < barChartYs.get(i).size(); j++) {
                entries.add(new BarEntry(j, barChartYs.get(i).get(j)));
            }
            BarDataSet barDataSet = new BarDataSet(entries, barNames.get(i));

            barDataSet.setColor(barColors.get(i));
            barDataSet.setValueTextColor(barColors.get(i));
            barDataSet.setValueTextSize(10f);
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            lists.add(barDataSet);
        }
        BarData barData = new BarData(lists);

        int amount = barChartYs.size(); //需要显示柱状图的类别 数量
        float groupSpace = 0.12f; //柱状图组之间的间距
        float barSpace = (float) ((1 - 0.12) / amount / 10); // x4 DataSet
        float barWidth = (float) ((1 - 0.12) / amount / 10 * 9); // x4 DataSet

        // (0.2 + 0.02) * 4 + 0.12 = 1.00 即100% 按照百分百布局
        //柱状图宽度
        barData.setBarWidth(barWidth);
        //(起始点、柱状图组间距、柱状图之间间距)
        barData.groupBars(0, groupSpace, barSpace);
        mBarChart.setData(barData);

        //设置一页最大显示个数为6，超出部分就滑动
        float ratio = (float) xAxisValues.size() / (float) 8;
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        mBarChart.zoom(ratio, 1f, 0, 0);
        //设置从X轴出来的动画时间
        //lineChart.animateX(1500);
        //设置XY轴动画
        mBarChart.animateXY(1500, 1500, Easing.EasingOption.EaseInSine, Easing.EasingOption.EaseInSine);
        CustomMarkerView marker = new CustomMarkerView(mContext, R.layout.marck_view);
        mBarChart.setMarker(marker);
        //刷新数据
        mBarChart.invalidate();
    }


    //来点随机数吧
    public void setData(int count, float range) {
        //设置相关属性
        mBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setMaxVisibleValueCount(60);
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawGridBackground(false);

        //x轴
        XAxis xl = mBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        //y轴
        YAxis yl = mBarChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f);

        //y轴
        YAxis yr = mBarChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f);
        float barWidth = 9f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range);
            yVals1.add(new BarEntry(i * spaceForBar, val));
        }
        BarDataSet set1;
        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "DataSet 1");

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);
            mBarChart.setData(data);
        }
    }
}
