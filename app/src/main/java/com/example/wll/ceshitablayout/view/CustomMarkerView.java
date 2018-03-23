package com.example.wll.ceshitablayout.view;

import android.content.Context;
import android.widget.TextView;

import com.example.wll.ceshitablayout.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;


/**
 * Created by phillip on 2018/3/23.
 */

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tv_marck_view);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText("" + e.getY()); // set the entry-value as the display text
    }

    @Override
    public MPPointF getOffset() {
        //设置在下方方,并且在左边
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
