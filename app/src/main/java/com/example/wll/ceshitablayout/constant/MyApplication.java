package com.example.wll.ceshitablayout.constant;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.apkfuns.log2file.LogFileEngineFactory;
import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.example.wll.ceshitablayout.MainActivity;
import com.example.wll.ceshitablayout.utils.*;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by wll on 2017/12/9.
 */

public class MyApplication extends MultiDexApplication {
    public static int screenWidth = 0;
    protected static Context sContext;
    public static int screenHeight = 0;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    public static List<Activity> activitylist = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        //初始化OKhttpUtils
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        //初始化zxinger二维码
        ZXingLibrary.initDisplayOpinion(this);

        LogUtils.getLogConfig()
                .configAllowLog(true)
                .configTagPrefix("LogUtilsDemo")
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")
                .configShowBorders(true)
//                .configMethodOffset(1)
                .configLevel(LogLevel.TYPE_VERBOSE);
        LogUtils.getLog2FileConfig().configLog2FileEnable(true)
                .configLog2FilePath("/sdcard/LogUtils/logs/")
                .configLog2FileNameFormat("Hi-%d{yyyyMMdd}-1.txt")
                .configLog2FileLevel(LogLevel.TYPE_VERBOSE)
                .configLogFileEngine(new LogFileEngineFactory());
        SDKInitializer.initialize(getApplicationContext());
        context();
        initLocation();

    }

    /**
     * 获取屏幕尺寸
     */
    private void getScreenSize() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
    }

    public static void add(Activity activity) {
        activitylist.add(activity);
    }

    public static void finish() {
        for (Activity activity : activitylist) {
            activity.finish();
        }
    }
    /**
     * 全局context
     *
     * @return context
     */
    public static synchronized MyApplication context() {
        return (MyApplication) sContext;
    }

    /**
     * 地图定位配置
     */
    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true


        option.setIsNeedLocationDescribe(true);
        //可选，是否需要位置描述信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的位置信息，此处必须为true


        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);

        mLocationClient.start();
    }

    /**
     * 定位监听
     */
    public class MyLocationListener extends BDAbstractLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            String jwd = latitude + ";" + longitude;
            LogUtils.i(jwd);
            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准


            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息

            String locationDescribe = location.getLocationDescribe();    //获取位置描述信息
            LogUtils.i(locationDescribe);
            List<Poi> poiList = location.getPoiList();
            //获取周边POI信息
            //POI信息包括POI ID、名称等，具体信息请参照类参考中POI类的相关说明
            PreferencesUtils.putString(MyApplication.context(), com.example.wll.ceshitablayout.utils.Constants.LAST_LOCATION, jwd);

        }
    }



}