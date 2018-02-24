package com.example.wll.ceshitablayout.homePageFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.ModeActivity;
import com.example.wll.ceshitablayout.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wll on 2017/12/9.
 */

public class NewsFragment extends Fragment {
    @BindView(R.id.btn_ceshi)
    Button btnCeshi;
    @BindView(R.id.btn_dayin)
    Button btnDayin;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.news_fragment, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        LogUtils.i("这是消息的oncreate");
        initData();
        btnCeshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ModeActivity.class);
                startActivity(intent);
            }
        });
        return inflate;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            LogUtils.i("消息走了吗  hidden");
        } else {
            LogUtils.i("消息走了吗  !!!!hidden");
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        btnCeshi.setText("消息");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
