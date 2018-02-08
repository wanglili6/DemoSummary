package com.example.wll.ceshitablayout.homePageFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.PrintMainActivity;
import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.baiDuMap.MapShowActivity;
import com.example.wll.ceshitablayout.baiDuMap.MapTraceActivity;
import com.example.wll.ceshitablayout.tabLayout.CommonTabActivity;
import com.example.wll.ceshitablayout.tabLayout.SegmentTabActivity;
import com.example.wll.ceshitablayout.tabLayout.SlidingTabActivity;
import com.flyco.tablayout.SlidingTabLayout;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wll on 2017/12/9.
 */

public class HomeFragment extends Fragment {
    @BindView(R.id.btn_ceshi)
    Button btnCeshi;
    @BindView(R.id.btn_dayin)
    Button btnDayin;
    Unbinder unbinder;
    @BindView(R.id.btn_map)
    Button btnMap;
    @BindView(R.id.btn_map_guiji)
    Button btnMapGuiji;
    @BindView(R.id.btn_commontabactivity)
    Button btnCommontabactivity;
    @BindView(R.id.btn_segmenttabactivity)
    Button btnSegmenttabactivity;
    @BindView(R.id.btn_slidingtabactivity)
    Button btnSlidingtabactivity;
    private String TAG = "HomeFragment的log";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.home_fragment, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        initData();
        return inflate;
    }

    /**
     * 初始化数据
     */
    private void initData() {

        /**
         * 打开默认二维码扫描界面
         */
        btnCeshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CaptureActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        btnDayin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PrintMainActivity.class);
                startActivity(intent);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapShowActivity.class);
                startActivity(intent);
            }
        });
        btnMapGuiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapTraceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 1) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    String substring = result.substring(0, 4);
                    LogUtils.d(substring);
                    if (substring.equals("http")) {
                        Toast.makeText(getContext(), "这个是地址", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getContext(), "这个是" + result, Toast.LENGTH_LONG).show();
                    }

                    LogUtils.d(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_commontabactivity, R.id.btn_segmenttabactivity, R.id.btn_slidingtabactivity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commontabactivity:
                Intent intent = new Intent();
                intent.setClass(getActivity(), CommonTabActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_segmenttabactivity:
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), SegmentTabActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_slidingtabactivity:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), SlidingTabActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
