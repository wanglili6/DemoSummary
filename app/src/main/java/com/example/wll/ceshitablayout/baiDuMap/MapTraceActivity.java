package com.example.wll.ceshitablayout.baiDuMap;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.TransportMode;
import com.example.wll.ceshitablayout.MainActivity;
import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.base.BaseActivity;
import com.example.wll.ceshitablayout.constant.UserMsg;
import com.example.wll.ceshitablayout.utils.MapUtil;
import com.example.wll.ceshitablayout.utils.PreferencesUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    MapView bmapView;
    private BaiduMap map;
    // 分页大小
    final int pageSize = 5000;
    // 分页索引
    int pageIndex = 1;
    /**
     * 轨迹点集合
     */
    private List<com.baidu.mapapi.model.LatLng> trackPoints = new ArrayList<>();

    /**
     * 轨迹排序规则
     */
    private SortType sortType = SortType.asc;
    private OnTrackListener mTrackListener;
    private HistoryTrackRequest historyTrackRequest;
    MapUtil mapUtil = null;

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
        mapUtil = MapUtil.getInstance();
        mapUtil.init(bmapView);
        mapUtil.setCenter();
        initDrawGuiji();
        getHistoryTrackRequest();

        // 查询历史轨迹
    }

    @NonNull
    private void getHistoryTrackRequest() {
        // 请求标识
        int tag = 1;
        // 轨迹服务ID
        long serviceId = 157276;
        // 设备标识
        String id = PreferencesUtils.getString(MapTraceActivity.this, UserMsg.UserId);
        String name = PreferencesUtils.getString(MapTraceActivity.this, UserMsg.UserName);
        String entityName = id + "_" + name;
        // 初始化轨迹服务客户端
        LBSTraceClient mTraceClient = new LBSTraceClient(getApplicationContext());
        // 创建历史轨迹请求实例
        historyTrackRequest = new HistoryTrackRequest(tag, serviceId, entityName);

        //设置轨迹查询起止时间
        // 开始时间(单位：秒)
        long startTime = 0;
        long endTime = 0;
        try {
            startTime = getTimesmorning("2017-12-28 00:00:00");
            endTime = getTimesnight("2017-12-28 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 结束时间(单位：秒)
        // 设置开始时间
        historyTrackRequest.setStartTime(startTime);
        // 设置结束时间
        historyTrackRequest.setEndTime(endTime);


//        // 设置需要纠偏
//        historyTrackRequest.setProcessed(true);
//        // 创建纠偏选项实例
//        ProcessOption processOption = new ProcessOption();
//        // 设置需要去噪
//        processOption.setNeedDenoise(true);
//        // 设置需要抽稀
//        processOption.setNeedVacuate(true);
//        // 设置需要绑路
//        processOption.setNeedMapMatch(true);
//        // 设置精度过滤值(定位精度大于100米的过滤掉)
//        processOption.setRadiusThreshold(20);
//        // 设置交通方式为驾车
//        processOption.setTransportMode(TransportMode.driving);
//        // 设置纠偏选项
//        historyTrackRequest.setProcessOption(processOption);
//        // 设置里程填充方式为驾车
//        historyTrackRequest.setSupplementMode(SupplementMode.driving);
        mTraceClient.queryHistoryTrack(historyTrackRequest, mTrackListener);

    }

    private void initDrawGuiji() {
        // 初始化轨迹监听器
        // 历史轨迹回调
        mTrackListener = new OnTrackListener() {
            // 历史轨迹回调
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                LogUtils.i(response.getMessage() + "===");
                int total = response.getTotal();
                LogUtils.d("daxa" + total);
                List<TrackPoint> Points = response.getTrackPoints();

                if (Points != null) {
                    LogUtils.d("aaaaaaaaaaaaaaaaaaaaaa" + Points.size());
                    for (int i = 1; i < Points.size(); i++) {
                        trackPoints.add(MapUtil.convertTrace2Map(Points.get(i).getLocation()));
                        LogUtils.d("aaaaaaaaaaaaaaaaaaaaaa" + Points.get(i).getLocation().getLatitude() + "aa" + Points.get(i).getLocation().getLongitude());
                    }
                }
                if (total > pageSize * pageIndex) {
                    historyTrackRequest.setPageIndex(++pageIndex);
                    try {
                        getHistoryTrackRequest();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mapUtil.drawHistoryTrack(trackPoints, sortType);

                }

            }
        };
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

    //获取当天0点
    public static long getTimesmorning(String str) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = df.parse(str);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long timestamp = cal.getTimeInMillis() / 1000;
        LogUtils.i(timestamp);
        return timestamp;
    }

    //获得当天24点时间
    public static long getTimesnight(String str) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = df.parse(str);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long timestamp = cal.getTimeInMillis() / 1000;
        LogUtils.i(timestamp);
        return timestamp;
    }
}
