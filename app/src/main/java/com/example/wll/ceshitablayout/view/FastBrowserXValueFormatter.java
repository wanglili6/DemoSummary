package com.example.wll.ceshitablayout.view;

import com.apkfuns.logutils.LogUtils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * Created by phillip on 2018/3/26.
 */

public class FastBrowserXValueFormatter implements IAxisValueFormatter {
    List<String> xValueList;

    public FastBrowserXValueFormatter(List<String> xValueList) {
        this.xValueList = xValueList;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        LogUtils.d("折线图", "----->getFormattedValue: " + value);
        return xValueList.get((int) value % xValueList.size());
    }
}
