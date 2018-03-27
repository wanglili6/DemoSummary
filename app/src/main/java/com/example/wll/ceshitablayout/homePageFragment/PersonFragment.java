package com.example.wll.ceshitablayout.homePageFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wll.ceshitablayout.mPAndroidChart.BarChartActivity;
import com.example.wll.ceshitablayout.mPAndroidChart.CombinedChartActivity;
import com.example.wll.ceshitablayout.mPAndroidChart.HorizitolBarChartActivity;
import com.example.wll.ceshitablayout.mPAndroidChart.LinechartActivity;
import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.mannerg.HoriztolBarChartManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wll on 2017/12/9.
 */

public class PersonFragment extends Fragment {
    @BindView(R.id.btn_ceshi)
    Button btnCeshi;
    @BindView(R.id.btn_line_chart)
    Button btnDayin;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.person_fragment, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        initData();
        return inflate;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        btnCeshi.setText("联系人");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_ceshi, R.id.btn_line_chart, R.id.btn_combined_chart, R.id.btn_bar_chart, R.id.btn_hbar_chart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ceshi:
                break;
            case R.id.btn_line_chart:
                Intent intent = new Intent();
                intent.setClass(getActivity(), LinechartActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_combined_chart:
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), CombinedChartActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_bar_chart:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), BarChartActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_hbar_chart:
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), HorizitolBarChartActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
