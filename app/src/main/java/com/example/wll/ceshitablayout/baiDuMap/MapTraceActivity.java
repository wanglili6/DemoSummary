package com.example.wll.ceshitablayout.baiDuMap;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.Constant;
import com.apkfuns.logutils.LogUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.api.fence.CreateFenceRequest;
import com.baidu.trace.api.fence.CreateFenceResponse;
import com.baidu.trace.api.fence.DeleteFenceResponse;
import com.baidu.trace.api.fence.FenceListResponse;
import com.baidu.trace.api.fence.HistoryAlarmResponse;
import com.baidu.trace.api.fence.MonitoredStatusByLocationResponse;
import com.baidu.trace.api.fence.MonitoredStatusResponse;
import com.baidu.trace.api.fence.OnFenceListener;
import com.baidu.trace.api.fence.UpdateFenceResponse;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.TransportMode;
import com.example.wll.ceshitablayout.MainActivity;
import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.base.BaseActivity;
import com.example.wll.ceshitablayout.constant.Constants;
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
import java.util.Timer;
import java.util.TimerTask;

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
    @BindView(R.id.tv_guiji)
    TextView tvGuiJi;
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
    List<TrackPoint> mapPoints = new ArrayList<>();

    /**
     * 轨迹排序规则
     */
    private SortType sortType = SortType.asc;
    private OnTrackListener mTrackListener;
    private HistoryTrackRequest historyTrackRequest;
    MapUtil mapUtil = null;
    private Marker mMarkerD;

    int recLen = 1;
    private LBSTraceClient mTraceClient;

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_guiji:
                GuiJiHuiFang(mapPoints);
                break;
        }
    }


    @Override
    public void initParms(Bundle parms) {
        tvTitle.setText("地图");
        tvBack.setText("返回");
        tvBack.setVisibility(View.VISIBLE);
        setSteepStatusBar(false);
        map = bmapView.getMap();
        mapUtil = MapUtil.getInstance();
        mTraceClient = new LBSTraceClient(getApplicationContext());
        mapUtil.init(bmapView);
        //设置中心点
        mapUtil.setCenter();
        // 查询历史轨迹
        initDrawGuiji();
        creatyuanxingDianziWeilan();
//        creatduobianxingDianziWeilan();
        getHistoryTrackRequest();

    }

    /**
     * 创建多边形电子围栏
     */
    private void creatduobianxingDianziWeilan() {
        // 请求标识
        int tag = 11;
// 轨迹服务ID
        long serviceId = 157276;
// 围栏名称
        String fenceName = "server_polygon_fence";
// 监控对象
        String monitoredPerson = "30_zmz";//对应的指定用户
// 多边形顶点集
        List<com.baidu.trace.model.LatLng> vertexes = new ArrayList();
        vertexes.add(new com.baidu.trace.model.LatLng(40.0581750000, 116.3067370000));
        vertexes.add(new com.baidu.trace.model.LatLng(40.0583410000, 116.3079580000));
        vertexes.add(new com.baidu.trace.model.LatLng(40.0554970000, 116.3093600000));
        vertexes.add(new com.baidu.trace.model.LatLng(40.0554140000, 116.3078150000));
// 去噪精度
        int denoise = 100;
// 坐标类型
        CoordType coordType = CoordType.bd09ll;

// 创建服务端多边形围栏请求实例
        CreateFenceRequest request = CreateFenceRequest.buildServerPolygonRequest(tag,
                serviceId, fenceName, monitoredPerson, vertexes, denoise, coordType);

// 初始化围栏监听器
        OnFenceListener mFenceListener = new OnFenceListener() {
            @Override
            public void onCreateFenceCallback(CreateFenceResponse createFenceResponse) {
                LogUtils.i(createFenceResponse.getMessage());
                LogUtils.i(createFenceResponse.getStatus());
                if (createFenceResponse.getMessage().equals("成功")) {
                    //定义多边形的五个顶点
                    com.baidu.mapapi.model.LatLng pt1 = new com.baidu.mapapi.model.LatLng(40.0581750000, 116.3067370000);
                    com.baidu.mapapi.model.LatLng pt2 = new com.baidu.mapapi.model.LatLng(40.0583410000, 116.3079580000);
                    com.baidu.mapapi.model.LatLng pt3 = new com.baidu.mapapi.model.LatLng(40.0554970000, 116.3093600000);
                    com.baidu.mapapi.model.LatLng pt4 = new com.baidu.mapapi.model.LatLng(40.0554140000, 116.3078150000);
                    List<com.baidu.mapapi.model.LatLng> pts = new ArrayList();
                    pts.add(pt1);
                    pts.add(pt2);
                    pts.add(pt3);
                    pts.add(pt4);

//构建用户绘制多边形的Option对象
                    OverlayOptions polygonOption = new PolygonOptions()
                            .points(pts)
                            .stroke(new Stroke(5, 0xAA00FF00))
                            .fillColor(0xAAFFFF00);

//在地图上添加多边形Option，用于显示
                    map.addOverlay(polygonOption);
                } else {
                    showToast("创建围栏失败");
                }
            }

            @Override
            public void onUpdateFenceCallback(UpdateFenceResponse updateFenceResponse) {

            }

            @Override
            public void onDeleteFenceCallback(DeleteFenceResponse deleteFenceResponse) {

            }

            @Override
            public void onFenceListCallback(FenceListResponse fenceListResponse) {

            }

            @Override
            public void onMonitoredStatusCallback(MonitoredStatusResponse monitoredStatusResponse) {

            }

            @Override
            public void onMonitoredStatusByLocationCallback(MonitoredStatusByLocationResponse monitoredStatusByLocationResponse) {

            }

            @Override
            public void onHistoryAlarmCallback(HistoryAlarmResponse historyAlarmResponse) {

            }

        };


// 创建服务端多边形围栏
        mTraceClient.createFence(request, mFenceListener);
    }

    /**
     * 创建电子围栏
     */
    private void creatyuanxingDianziWeilan() {
        // 请求标识
        int tag = 3;
// 轨迹服务ID
        long serviceId = 157276;
// 围栏名称
        String fenceName = "local_circle";
// 监控对象
        String monitoredPerson = "myTrace";//对应的是本身
        String string = PreferencesUtils.getString(MapTraceActivity.this, com.example.wll.ceshitablayout.utils.Constants.LAST_LOCATION, "");
        if (!TextUtils.isEmpty(string)) {
            String[] split = string.split(";");
            final double lat = Double.parseDouble(split[0]);
            final double loug = Double.parseDouble(split[1]);
// 围栏圆心

            com.baidu.trace.model.LatLng center = new com.baidu.trace.model.LatLng(lat, loug);

            // 围栏半径（单位 : 米）
            double radius = 2000;
// 去噪精度
            int denoise = 100;
// 坐标类型
            CoordType coordType = CoordType.bd09ll;


// 创建本地圆形围栏请求实例
            CreateFenceRequest localCircleFenceRequest = CreateFenceRequest.buildLocalCircleRequest(tag, serviceId, fenceName, monitoredPerson, center, radius, denoise, coordType);


// 初始化围栏监听器
            OnFenceListener mFenceListener = new OnFenceListener() {
                // 创建围栏回调
                @Override
                public void onCreateFenceCallback(CreateFenceResponse response) {
                    LogUtils.i(response.getStatus());
                    LogUtils.i(response.getMessage());
                    if (response.getMessage().equals("成功")) {
                        //设置颜色和透明度，均使用16进制显示，0xAARRGGBB，如 0xAA000000 其中AA是透明度，000000为颜色
                        LatLng llCircle = new LatLng(lat, loug);
                        OverlayOptions ooCircle = new CircleOptions().fillColor(Color.parseColor("#609CDAB1"))
                                .center(llCircle).stroke(new Stroke(5, Color.parseColor("#9cdab1"))).radius(2000);
                        map.addOverlay(ooCircle);
                    } else {
                        showToast("创建围栏失败");
                    }
                }

                // 更新围栏回调
                @Override
                public void onUpdateFenceCallback(UpdateFenceResponse response) {
                }

                // 删除围栏回调
                @Override
                public void onDeleteFenceCallback(DeleteFenceResponse response) {
                    LogUtils.i(response.getMessage());
                    LogUtils.i(response.getStatus());
                }

                // 围栏列表回调
                @Override
                public void onFenceListCallback(FenceListResponse response) {
                    LogUtils.i(response.getMessage());
                    LogUtils.i(response.getStatus());
                }

                // 监控状态回调
                @Override
                public void onMonitoredStatusCallback(MonitoredStatusResponse
                                                              response) {
                    LogUtils.i(response.getMessage());
                    LogUtils.i(response.getStatus());
                }

                // 指定位置监控状态回调
                @Override
                public void onMonitoredStatusByLocationCallback(MonitoredStatusByLocationResponse response) {
                    LogUtils.i(response.getMessage());
                    LogUtils.i(response.getStatus());
                }

                // 历史报警回调
                @Override
                public void onHistoryAlarmCallback(HistoryAlarmResponse response) {
                    LogUtils.i(response.getMessage());
                    LogUtils.i(response.getStatus());
                }
            };


// 创建本地圆形围栏
            mTraceClient.createFence(localCircleFenceRequest, mFenceListener);
        }

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
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long l = System.currentTimeMillis();
        String format = df.format(l);
        try {
            startTime = getTimesmorning(format + " 00:00:00");
            endTime = getTimesnight(format + " 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 结束时间(单位：秒)
        // 设置开始时间
        historyTrackRequest.setStartTime(startTime);
        // 设置结束时间
        historyTrackRequest.setEndTime(endTime);

        // 设置需要纠偏
        historyTrackRequest.setProcessed(true);
        // 创建纠偏选项实例
        ProcessOption processOption = new ProcessOption();
        // 设置需要去噪
        processOption.setNeedDenoise(true);
        // 设置需要抽稀
        processOption.setNeedVacuate(true);
        // 设置需要绑路
        processOption.setNeedMapMatch(true);
        // 设置精度过滤值(定位精度大于100米的过滤掉)
        processOption.setRadiusThreshold(200);
        // 设置交通方式为步行
        processOption.setTransportMode(TransportMode.walking);
        // 设置纠偏选项
        historyTrackRequest.setProcessOption(processOption);
        //
        historyTrackRequest.setSupplementMode(SupplementMode.no_supplement);
        mTraceClient.queryHistoryTrack(historyTrackRequest, mTrackListener);

    }

    /**
     * 历史轨迹回调
     */
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
                final List<TrackPoint> Points = response.getTrackPoints();
                if (Points != null) {
                    mapPoints.clear();
                    mapPoints.addAll(Points);
                    for (int i = 0; i < Points.size(); i++) {
                        trackPoints.add(MapUtil.convertTrace2Map(Points.get(i).getLocation()));
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
                    try {
                        mapUtil.drawHistoryTrack(trackPoints, sortType);
                    } catch (Exception e) {

                    }

                }

            }
        };
    }

    /**
     * 轨迹回放
     *
     * @param points
     */
    private void GuiJiHuiFang(final List<TrackPoint> points) {
        if (points != null) {
            final Timer timer = new Timer();
            recLen = 0;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recLen++;

                            if (recLen >= points.size()) {
                                timer.cancel();
                                mMarkerD.remove();
                            } else {
                                if (mMarkerD != null) {
                                    mMarkerD.remove();
                                }
                                BitmapDescriptor bitmapDescriptor4 = BitmapDescriptorFactory.fromResource(R.mipmap.map_guiji);
                                MarkerOptions ooD = new MarkerOptions()
                                        .position(new LatLng(points.get(recLen).getLocation().getLatitude(), points.get(recLen).getLocation().getLongitude()))
                                        .icon(bitmapDescriptor4)
                                        .zIndex(0)
                                        .period(10);
                                mMarkerD = (Marker) (map.addOverlay(ooD));
                            }
                        }
                    });
                }
            }, 1000, 200);
        }
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
        rlBack.setOnClickListener(this);
        tvGuiJi.setOnClickListener(this);

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        bmapView.onDestroy();
        map.clear();
        mMarkerD = null;
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
