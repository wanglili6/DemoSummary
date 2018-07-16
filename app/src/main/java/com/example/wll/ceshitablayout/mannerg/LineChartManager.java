package com.example.wll.ceshitablayout.mannerg;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.view.CustomMarkerView;
import com.example.wll.ceshitablayout.view.FastBrowserXValueFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phillip on 2018/3/26.
 */

public class LineChartManager {
    private LineChart mLineChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private boolean mAnimation = true;
    private Context mContext;

    public LineChartManager(LineChart mLineChart, Context mContext) {
        this.mLineChart = mLineChart;
        this.mContext = mContext;
        leftAxis = mLineChart.getAxisLeft();
        rightAxis = mLineChart.getAxisRight();
        xAxis = mLineChart.getXAxis();
        this.mContext = mContext;
    }

    /**
     * 设置折线图的属性
     */
    private void setChartProperties() {
        //设置描述文本不显示
        mLineChart.getDescription().setEnabled(false);
        //设置是否显示表格背景
        mLineChart.setDrawGridBackground(true);
        //设置是否可以触摸
        mLineChart.setTouchEnabled(true);
        mLineChart.setDragDecelerationFrictionCoef(0.9f);
        //设置是否可以拖拽
        mLineChart.setDragEnabled(true);
        //设置是否可以缩放
        mLineChart.setScaleEnabled(true);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setHighlightPerDragEnabled(true);
        mLineChart.setPinchZoom(true);
        //设置背景颜色
        mLineChart.setBackgroundColor(Color.parseColor("#66f0f0f0"));
        //设置从X轴出来的动画时间
        //mLineChart.animateX(1500);
        //设置XY轴动画
        if (mAnimation) {
            mLineChart.animateXY(1500, 1500, Easing.EasingOption.EaseInSine, Easing.EasingOption.EaseInSine);
        }
        CustomMarkerView marker = new CustomMarkerView(mContext, R.layout.marck_view);
        mLineChart.setMarker(marker);
    }

    /**
     * 设置X轴属性
     */
    private void setChartXAxis(LineChart mLineChart, List<String> xValueList) {
        //自定义设置横坐标
        IAxisValueFormatter xValueFormatter = new FastBrowserXValueFormatter(xValueList);
        //X轴
        XAxis xAxis = mLineChart.getXAxis();
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
        xAxis.setLabelCount(6);
        //设置最小间隔，防止当放大时出现重复标签
        xAxis.setGranularity(1f);
        //设置为true当一个页面显示条目过多，X轴值隔一个显示一个
        xAxis.setGranularityEnabled(true);
        //设置X轴的颜色
        xAxis.setAxisLineColor(Color.BLACK);
        //设置X轴的宽度
        xAxis.setAxisLineWidth(1f);
        mLineChart.invalidate();
    }

    /**
     * 设置Y轴的属性
     */
    private void setChartYAxis(LineChart mLineChart) {
        YAxis leftAxis = mLineChart.getAxisLeft();
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

        YAxis rightAxis = mLineChart.getAxisRight();
        //设置右边Y轴不显示
        rightAxis.setEnabled(false);
    }

    /**
     * 设置数据 单条
     *
     * @param xValueList 轴数据
     * @param yValueList y轴的所有数据
     * @param name       图例的名字
     * @param color      线的颜色
     * @param minXgeshu  页面显示的最小数
     */

    public void setData(ArrayList<String> xValueList, ArrayList<Integer> yValueList, String name, int color, int minXgeshu) {
        setChartProperties();
        setChartXAxis(mLineChart, xValueList);
        setChartYAxis(mLineChart);
        ArrayList<Entry> yValues = new ArrayList<>();
        for (int i = 0; i < xValueList.size(); i++) {
            yValues.add(new Entry(i, yValueList.get(i)));
        }

        LineDataSet set;
        if (mLineChart.getData() != null && mLineChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            set.setValues(yValues);
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            //设置值给图表
            set = new LineDataSet(yValues, name);
            //设置图标不显示
            set.setDrawIcons(false);
            //设置Y值使用左边Y轴的坐标值
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            //设置线的颜色
            set.setColors(color);
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
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.fade_red);
                set.setFillDrawable(drawable);
            } else {
                set.setFillColor(Color.RED);
            }

            LineData data = new LineData(set);
            //设置不显示数据点的值
            data.setDrawValues(true);
            mLineChart.setData(data);
            mLineChart.invalidate();
            //设置一页最大显示个数为6，超出部分就滑动
            float ratio = (float) xValueList.size() / (float) minXgeshu;
            //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
            mLineChart.zoom(ratio, 1f, 0, 0);
            //设置从X轴出来的动画时间
            //lineChart.animateX(1500);
            //设置XY轴动画
            mLineChart.animateXY(1500, 1500, Easing.EasingOption.EaseInSine, Easing.EasingOption.EaseInSine);
            CustomMarkerView marker = new CustomMarkerView(mContext, R.layout.marck_view);
            mLineChart.setMarker(marker);
        }
    }

    /**
     * 设置数据 多条
     *
     * @param xValueList    轴数据
     * @param allYValueList y轴的所有数据
     * @param nameList      图例的名字
     * @param colors        线的颜色
     * @param minXgeshu     页面显示的最小数
     */
    public void setData(ArrayList<String> xValueList, ArrayList<ArrayList<Integer>> allYValueList, ArrayList<String> nameList, ArrayList<Integer> colors, int minXgeshu) {
        setChartProperties();
        setChartXAxis(mLineChart, xValueList);
        setChartYAxis(mLineChart);
        //设置描述文本不显示
        Description desc = new Description();
        desc.setText("");
        mLineChart.setDescription(desc);
        mLineChart.resetTracking();

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        for (int z = 0; z < nameList.size(); z++) {
            ArrayList<Entry> values = new ArrayList<Entry>();
            for (int i = 0; i < xValueList.size(); i++) {

                values.add(new Entry(i, allYValueList.get(z).get(i)));
            }
            LineDataSet set = new LineDataSet(values, nameList.get(z));
            set.setDrawIcons(false);//设置图标不显示
            set.setAxisDependency(YAxis.AxisDependency.LEFT);//设置Y值使用左边Y轴的坐标值
//            //设置填充
//            //设置允许填充
//            set.setDrawFilled(true);
//            set.setFillAlpha(50);
            int color = colors.get(z);
            set.setColor(color);
            set.setCircleColor(color);//设置数据点颜色
            set.setCircleColorHole(Color.WHITE);//设置数据点中间填充颜色
            set.setLineWidth(1f);
            set.setCircleRadius(4f);
            set.setHighlightEnabled(true);//设置是否显示十字线
            set.setDrawCircleHole(true);//设置是否在数据点中间显示一个孔
            dataSets.add(set);
        }
        LineData data = new LineData(dataSets);
        data.setDrawValues(false);//设置显示数据点
        mLineChart.setData(data);
        mLineChart.invalidate();
        //设置一页最大显示个数为6，超出部分就滑动
        float ratio = (float) xValueList.size() / (float) minXgeshu;
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        mLineChart.zoom(ratio, 1f, 0, 0);
        //设置从X轴出来的动画时间
        //lineChart.animateX(1500);
        //设置XY轴动画
        mLineChart.animateXY(1500, 1500, Easing.EasingOption.EaseInSine, Easing.EasingOption.EaseInSine);
        CustomMarkerView marker = new CustomMarkerView(mContext, R.layout.marck_view);
        mLineChart.setMarker(marker);
    }

    public void setAnimation(boolean animation) {
        mAnimation = animation;
    }

}
