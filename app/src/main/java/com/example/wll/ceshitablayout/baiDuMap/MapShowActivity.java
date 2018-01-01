package com.example.wll.ceshitablayout.baiDuMap;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.api.entity.EntityInfo;
import com.baidu.trace.api.entity.EntityListRequest;
import com.baidu.trace.api.entity.EntityListResponse;
import com.baidu.trace.api.entity.FilterCondition;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.StatusCodes;
import com.example.wll.ceshitablayout.R;

import com.example.wll.ceshitablayout.base.BaseActivity;
import com.example.wll.ceshitablayout.baiDuMap.clusterutil.clustering.Cluster;
import com.example.wll.ceshitablayout.baiDuMap.clusterutil.clustering.ClusterItem;
import com.example.wll.ceshitablayout.baiDuMap.clusterutil.clustering.ClusterManager;
import com.example.wll.ceshitablayout.utils.MapUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 实时定位
 */
public class MapShowActivity extends BaseActivity {

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
    private BaiduMap mBaiduMap;

    public LocationClient mLocationClient = null;

    List<EntityInfo> entitiesList = new ArrayList<>();
    //鹰眼服务ID
    long serviceId = 157276;
    //活跃时间，UNIX时间戳（指定该字段时，返回从该时间点之后仍有位置变动的entity的实时点集合）
    int activeTime = (int) (System.currentTimeMillis() / 1000 - 5 * 60);
    //分页大小
    int pageSize = 1000;
    //分页索引
    int pageIndex = 1;
    int tag = 5;
    // 返回结果坐标类型
    CoordType coordTypeOutput = CoordType.bd09ll;
    private ClusterManager mClusterManager;
    private Marker overlay;
    private MapUtil mapUtil;

    @Override
    public void initParms(Bundle parms) {
        tvTitle.setText("地图");
        tvBack.setText("返回");
        tvBack.setVisibility(View.VISIBLE);
        setSteepStatusBar(false);

        mBaiduMap = bmapView.getMap();
        mapUtil = MapUtil.getInstance();
        // 定义点聚合管理类ClusterManager
        init();
        mClusterManager = new ClusterManager<MyItem>(MapShowActivity.this, mBaiduMap);

        // 设置maker点击时的响应
//        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
//            @Override
//            public void onTouch(MotionEvent motionEvent) {
//                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
//                    mapView.requestDisallowInterceptTouchEvent(false);
//                }else {
//                    mapView.requestDisallowInterceptTouchEvent(true);
//                }
//            }
//        });
//地图被点击时的操作

        bmapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    bmapView.requestDisallowInterceptTouchEvent(false);
                } else {
                    bmapView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });


//地图点击事件
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });


        //聚合点的点击事件
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                //获取点击的聚合点的坐标
                Collection<MyItem> items = cluster.getItems();
                if (items!=null){
                    for (MyItem item : items) {
                        String name = item.getName();
                        LogUtils.i("聚合点的点击事件"+name);
                    }

                }
                return true;


            }
        });

        //单个的点击事件
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                String name = item.mBundle.getString("name");
                LogUtils.i("单个的点击事件___"+name);
                String id = item.mBundle.getString("id");
                LogUtils.i("单个的点击事件___"+id);

                return false;
            }
        });
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        //点击聚合Mark
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
    }


    private void init() {
        FilterCondition filterCondition = new FilterCondition();
        filterCondition.setActiveTime(activeTime);
        LBSTraceClient mTraceClient = new LBSTraceClient(getApplicationContext());

        // 创建Entity列表请求实例
        EntityListRequest request = new EntityListRequest(tag, serviceId, filterCondition, coordTypeOutput, pageIndex, pageSize);
        mTraceClient.queryEntityList(request, entityListener);

    }

    // 初始化监听器
    OnEntityListener entityListener = new OnEntityListener() {

        @Override
        public void onEntityListCallback(EntityListResponse response) {
            if (StatusCodes.SUCCESS != response.getStatus()) {
                return;
            }
            List<EntityInfo> entities = response.getEntities();
            if (entities.size() > 0) {
                LogUtils.i(entities.size());
                entitiesList.clear();
                entitiesList.addAll(entities);
                //添加mack
                addMarkers();
                double latitude = entities.get(0).getLatestLocation().getLocation().latitude;
                double longitude = entities.get(0).getLatestLocation().getLocation().longitude;
                MapStatus build = new MapStatus.Builder().target(new LatLng(latitude, longitude)).zoom(16).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(build));
//                String string = PreferencesUtils.getString(MapShowActivity.this, Constants.LAST_LOCATION);
//                if (!TextUtils.isEmpty(string)) {
//                    String[] split = string.split(";");
//                    if (split != null) {
//                        if (split.length != 0) {
//                            double latitude = Double.parseDouble(split[0]);
//                            double longitude = Double.parseDouble(split[1]);
//                            LatLng cenpt = new LatLng(latitude, longitude);
//                            //定义地图状态
//                            MapStatus mMapStatus = new MapStatus.Builder()
//                                    .target(cenpt)
//                                    .zoom(16)
//                                    .build();
//                            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//                            //改变地图状态
//                            mBaiduMap.setMapStatus(mMapStatusUpdate);
//                        }
//                    }
//                }


            }


        }

    };

    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {
        if (entitiesList.size() > 0) {
            List<MyItem> items = new ArrayList<>();
            for (int i = 0; i < entitiesList.size(); i++) {
                EntityInfo entityInfo = entitiesList.get(i);
                Bundle mBundle = new Bundle();
                LatLng latLng = new LatLng(entityInfo.getLatestLocation().getLocation().getLatitude(),
                        entityInfo.getLatestLocation().getLocation().getLongitude());
                String name = entityInfo.getEntityName();
                String[] split = name.split("_");
                mBundle.putString("id", split[0]);
                mBundle.putString("name", split[1]);

                MyItem myItem = new MyItem(latLng, name, mBundle);
                items.add(myItem);
            }
            mClusterManager.addItems(items);
        }
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_map_show;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this);
    }

    @Override
    public void setListener() {
        rlBack.setOnClickListener(this);
    }

    @Override
    public void doBusiness(Context mContext) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        bmapView.onDestroy();
        // 当不需要定位图层时关闭定位图
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap.clear();
        overlay = null;
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

    public class MyItem implements ClusterItem {
        private LatLng mPosition;
        private String name;
        private Bundle mBundle;

        public MyItem(LatLng mPosition, String name, Bundle mBundle) {
            this.mPosition = mPosition;
            this.name = name;
            this.mBundle = mBundle;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            View view;
            view = View.inflate(MapShowActivity.this, R.layout.item_lon, null);
            TextView textView = (TextView) view.findViewById(R.id.icon_title);
            textView.setText(name);
            return BitmapDescriptorFactory
                    .fromView(view);
        }

        public LatLng getmPosition() {
            return mPosition;
        }

        public String getName() {
            return name;
        }

        public Bundle getmBundle() {
            return mBundle;
        }


        public void setmPosition(LatLng mPosition) {
            this.mPosition = mPosition;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setmBundle(Bundle mBundle) {
            this.mBundle = mBundle;
        }


    }

}
