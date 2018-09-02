package com.example.wll.ceshitablayout.homePageFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telecom.InCallService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.ModeActivity;
import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.animation.ShopCartActivity;
import com.example.wll.ceshitablayout.myView.ClinpImageActivity;
import com.example.wll.ceshitablayout.myView.MyViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.zidingyi_view)
    Button zidingyiView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.news_fragment, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        LogUtils.i("这是消息的oncreate");
        initData();

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

    @OnClick({R.id.btn_ceshi, R.id.btn_dayin, R.id.zidingyi_view,R.id.shop_cart,R.id.cinp_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ceshi:
                Intent intent = new Intent();
                intent.setClass(getContext(), ModeActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_dayin:
                break;
            case R.id.zidingyi_view:
                Intent intentView = new Intent();
                intentView.setClass(getContext(), MyViewActivity.class);
                startActivity(intentView);
                break;
            case R.id.shop_cart:
                Intent intentshop = new Intent();
                intentshop.setClass(getContext(), ShopCartActivity.class);
                startActivity(intentshop);
                break;
            case R.id.cinp_img:
                startActivity(new Intent(getActivity(), ClinpImageActivity.class));
                break;
        }
    }
}
