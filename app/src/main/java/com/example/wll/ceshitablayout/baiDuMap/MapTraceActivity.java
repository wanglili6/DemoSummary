package com.example.wll.ceshitablayout.baiDuMap;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.base.BaseActivity;
import com.example.wll.ceshitablayout.constant.UserMsg;
import com.example.wll.ceshitablayout.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 鹰眼轨迹
 */
public class MapTraceActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.rl_select)
    RelativeLayout rlSelect;
    @BindView(R.id.rl_title_bg)
    RelativeLayout rlTitleBg;
    @BindView(R.id.bmapView)
    TextureMapView bmapView;
    private BaiduMap map;

    // 轨迹服务ID
    long serviceId = 157276;
    // 设备标识
    String id = PreferencesUtils.getString(MapTraceActivity.this, UserMsg.UserId);
    String name = PreferencesUtils.getString(MapTraceActivity.this, UserMsg.UserName);
    String entityName = id + "_" + name;
    // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
    boolean isNeedObjectStorage = false;
    // 初始化轨迹服务
    Trace mTrace = new Trace(serviceId, entityName, isNeedObjectStorage);
    // 初始化轨迹服务客户端
    LBSTraceClient mTraceClient = new LBSTraceClient(getApplicationContext());
    // 定位周期(单位:秒)
    int gatherInterval = 5;
    // 打包回传周期(单位:秒)
    int packInterval = 10;
// 设置定位和打包周期

    @Override
    public void widgetClick(View v) {

    }


    @Override
    public void initParms(Bundle parms) {
        tvTitle.setText("地图");
        tvBack.setText("返回");
        tvBack.setVisibility(View.VISIBLE);
        setSteepStatusBar(false);
        map = bmapView.getMap();
        initTraceLocation();
    }

    /**
     *
     */
    private void initTraceLocation() {
        mTraceClient.setInterval(gatherInterval, packInterval);
// 初始化轨迹服务监听器
        OnTraceListener mTraceListener = new OnTraceListener() {
            @Override
            public void onBindServiceCallback(int i, String s) {

            }

            // 开启服务回调
            @Override
            public void onStartTraceCallback(int status, String message) {
                LogUtils.i(message);
                LogUtils.i(status);
            }

            // 停止服务回调
            @Override
            public void onStopTraceCallback(int status, String message) {
            }

            // 开启采集回调
            @Override
            public void onStartGatherCallback(int status, String message) {
                LogUtils.d(message);
                LogUtils.d(status);
            }

            // 停止采集回调
            @Override
            public void onStopGatherCallback(int status, String message) {
            }

            // 推送回调
            @Override
            public void onPushCallback(byte messageNo, PushMessage message) {
            }

            @Override
            public void onInitBOSCallback(int i, String s) {

            }
        };

        // 开启服务
        mTraceClient.startTrace(mTrace, mTraceListener);
        // 开启采集
        mTraceClient.startGather(mTraceListener);
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_map_trace;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        bmapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        bmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        bmapView.onPause();
    }
}
