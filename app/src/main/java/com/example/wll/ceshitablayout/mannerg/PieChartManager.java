package com.example.wll.ceshitablayout.mannerg;

import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phillip on 2018/3/29.
 */

public class PieChartManager {
    private PieChart mChart;
    private List<PieEntry> mEntries;
    private List<String> labels;
    private ArrayList<Integer> mPieColors;
    private int mValueColor;
    private float mTextSize;
    private PieDataSet.ValuePosition mValuePosition;

    /**
     * @param chart         控件
     * @param entries       数据
     * @param labels        图例
     * @param chartColor
     * @param textSize      字体大小
     * @param valueColor    字体的颜色
     * @param valuePosition
     */
    public PieChartManager(PieChart chart, List<PieEntry> entries, List<String> labels,
                           ArrayList<Integer> chartColor, float textSize, int valueColor, PieDataSet.ValuePosition valuePosition) {
        this.mChart = chart;
        this.mEntries = entries;
        this.labels = labels;
        this.mPieColors = chartColor;
        this.mTextSize = textSize;
        this.mValueColor = valueColor;
        this.mValuePosition = valuePosition;
        initPieChart();
    }


    private void initPieChart() {
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawCenterText(false);
        mChart.getDescription().setEnabled(false);
        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setDrawEntryLabels(true);
        setChartData();
        mChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(1f);
        l.setYOffset(0f);
        // entry label styling
        mChart.setEntryLabelColor(mValueColor);
        mChart.setEntryLabelTextSize(mTextSize);
        mChart.setExtraOffsets(10, 10, 10, 10);
    }

    public void setHoleDisenabled() {
        mChart.setDrawHoleEnabled(false);
    }

    /**
     * 中心圆是否可见
     *
     * @param holeColor   中心圆颜色
     * @param holeRadius  半径
     * @param transColor  透明圆颜色
     * @param transRadius 透明圆半径
     */
    public void setHoleEnabled(int holeColor, float holeRadius, int transColor, float transRadius) {
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(holeColor);
        mChart.setTransparentCircleColor(transColor);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(holeRadius);
        mChart.setTransparentCircleRadius(transRadius);
    }

    private void setChartData() {

        PieDataSet dataSet = new PieDataSet(mEntries, "月份");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(mPieColors);
        dataSet.setSelectionShift(0f);
        dataSet.setYValuePosition(mValuePosition);
        dataSet.setXValuePosition(mValuePosition);
        dataSet.setValueLineColor(mValueColor);
        dataSet.setSelectionShift(15f);
        dataSet.setValueLinePart1Length(0.6f);
        dataSet.setValueLineColor(mValueColor);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(mTextSize);
        data.setValueTextColor(mValueColor);
        data.setValueTextColor(mValueColor);
        mChart.setData(data);
        // undo all highlights
        mChart.highlightValues(null);
        mChart.invalidate();
    }

    /**
     * <p>说明文字是否可见</p>
     *
     * @param enabled true 可见,默认可见
     */
    public void setLegendEnabled(boolean enabled) {
        mChart.getLegend().setEnabled(enabled);
        mChart.invalidate();
    }

    public void setPercentValues(boolean showPercent) {
        mChart.setUsePercentValues(showPercent);
    }
}
