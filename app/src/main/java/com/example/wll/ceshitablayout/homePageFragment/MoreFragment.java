package com.example.wll.ceshitablayout.homePageFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.trace.LBSTraceClient;
import com.example.wll.ceshitablayout.LoginActivity;
import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.constant.MyApplication;
import com.example.wll.ceshitablayout.constant.UserMsg;
import com.example.wll.ceshitablayout.pojoBean.UserInfo;
import com.example.wll.ceshitablayout.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wll on 2017/12/9.
 */

public class MoreFragment extends Fragment {
    @BindView(R.id.btn_ceshi)
    Button btnCeshi;
    @BindView(R.id.btn_dayin)
    Button btnDayin;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.more_fragment, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        initData();
        return inflate;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        btnCeshi.setText("更多");
        //退出
        btnDayin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtils.putString(getContext(), UserMsg.UserId, "");
                MyApplication.finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
