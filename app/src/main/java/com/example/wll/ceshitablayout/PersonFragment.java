package com.example.wll.ceshitablayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wll on 2017/12/9.
 */

public class PersonFragment extends Fragment {
    @BindView(R.id.btn_ceshi)
    Button btnCeshi;
    @BindView(R.id.btn_dayin)
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
}
