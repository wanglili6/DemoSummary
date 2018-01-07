package com.example.wll.ceshitablayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.fence.AlarmPoint;
import com.baidu.trace.api.fence.FenceAlarmPushInfo;
import com.baidu.trace.api.fence.MonitoredAction;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.example.wll.ceshitablayout.adapter.MyPagerAdapter;
import com.example.wll.ceshitablayout.base.BaseActivity;
import com.example.wll.ceshitablayout.constant.MyApplication;
import com.example.wll.ceshitablayout.constant.TabEntity;
import com.example.wll.ceshitablayout.constant.UserMsg;
import com.example.wll.ceshitablayout.homePageFragment.HomeFragment;
import com.example.wll.ceshitablayout.homePageFragment.MoreFragment;
import com.example.wll.ceshitablayout.homePageFragment.NewsFragment;
import com.example.wll.ceshitablayout.homePageFragment.PersonFragment;
import com.example.wll.ceshitablayout.utils.PreferencesUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private CommonTabLayout tabLayout;
    private ViewPager viewpager;
    /*文本信息*/
    private String[] mTitles = {"首页", "消息", "联系人", "更多"};
    /*未选择时的icon*/
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    /*选择时的icon*/
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};
    ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    ArrayList<Fragment> mFragments = new ArrayList<>();
    private LBSTraceClient mTraceClient;
    private OnTraceListener mTraceListener;
    private Trace mTrace;


    @Override
    public void widgetClick(View v) {

    }


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void initParms(Bundle parms) {
        initData();
    }

    /**
     * 初始化
     */
    private void initData() {
        tabLayout = (CommonTabLayout) findViewById(R.id.tl_2);
        viewpager = (ViewPager) findViewById(R.id.vp_2);
        MyApplication.add(this);
        String sdk = Build.VERSION.SDK;
        int sdkInt = Build.VERSION.SDK_INT;
        Log.i("-=----", "onCreate: " + sdk);
        Log.i("-=--fcdsfs--", "onCreate: " + sdkInt);

  /*添加数据集*/
        HomeFragment homeFragment = new HomeFragment();
        PersonFragment personFragment = new PersonFragment();
        NewsFragment newsFragment = new NewsFragment();
        MoreFragment moreFragment = new MoreFragment();
        for (int i = 0; i < mTitles.length; i++) {
            String mTitle = mTitles[i];
            if (mTitle.equals("首页")) {
                mFragments.add(homeFragment);
            }
            if (mTitle.equals("消息")) {
                mFragments.add(newsFragment);
            }
            if (mTitle.equals("联系人")) {
                mFragments.add(personFragment);
            }
            if (mTitle.equals("更多")) {
                mFragments.add(moreFragment);
            }
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), MainActivity.this, mFragments, mTitles);
        viewpager.setAdapter(myPagerAdapter);
        myPagerAdapter.notifyDataSetChanged();

        tabLayout.setTabData(mTabEntities);
        tabLayout.showDot(2);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewpager.setCurrentItem(position);
                if (position == 2) {
//                    Intent intent = new Intent(MainActivity.this, UpdateService.class);
//                    intent.putExtra("apkUrl", "http://51growup.com/app-debug.apk");
//                    startService(intent);
                    tabLayout.hideMsg(2);
                } else if (position == 1) {
                    tabLayout.hideMsg(1);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // GPS
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS是否正常启动
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsAlertMess();
//            return;
        } else {
            String id = PreferencesUtils.getString(MainActivity.this, UserMsg.UserId);
            String name = PreferencesUtils.getString(MainActivity.this, UserMsg.UserName);
            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name)) {

            } else {
                initTraceLocation(id, name);
            }
        }
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    /**
     * 轨迹采集
     *
     * @param id
     * @param name
     */
    private void initTraceLocation(String id, String name) {
        // 轨迹服务ID
        long serviceId = 157276;
        // 设备标识
        String entityName = id + "_" + name;
        // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
        boolean isNeedObjectStorage = false;
        // 初始化轨迹服务
        mTrace = new Trace(serviceId, entityName, isNeedObjectStorage);
        // 初始化轨迹服务客户端
        mTraceClient = new LBSTraceClient(getApplicationContext());
        // 定位周期(单位:秒)
        int gatherInterval = 5;
        // 打包回传周期(单位:秒)
        int packInterval = 10;

        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);
        // 初始化轨迹服务监听器

        mTraceListener = new OnTraceListener() {
            @Override
            public void onBindServiceCallback(int i, String s) {

            }

            // 开启服务回调
            @Override
            public void onStartTraceCallback(int status, String message) {
                LogUtils.i("开启服务回调" + message);
                LogUtils.i("开启服务回调" + status);
            }

            // 停止服务回调
            @Override
            public void onStopTraceCallback(int status, String message) {
                LogUtils.i("停止服务回调" + message);
                LogUtils.i("停止服务回调" + status);
            }

            // 开启采集回调
            @Override
            public void onStartGatherCallback(int status, String message) {
                LogUtils.d("开启采集回调" + message);
                LogUtils.d("开启采集回调" + status);
            }

            // 停止采集回调
            @Override
            public void onStopGatherCallback(int status, String message) {
                LogUtils.i("停止采集回调" + message);
                LogUtils.i("停止采集回调" + status);
            }

            // 推送回调
            @Override
            public void onPushCallback(byte messageNo, PushMessage message) {
                LogUtils.d("预警推送" + messageNo + "信息" + message);
                FenceAlarmPushInfo alarmPushInfo = message.getFenceAlarmPushInfo();
                LogUtils.d("预警推送围栏id" + alarmPushInfo.getFenceId());
                LogUtils.d("预警推送围栏动作" + alarmPushInfo.getMonitoredAction());
                //获取围栏id
                long fenceId = alarmPushInfo.getFenceId();
                String monitoredPerson = alarmPushInfo.getMonitoredPerson();//获取监控对象标识
                String fenceName = alarmPushInfo.getFenceName();//获取围栏名称
                alarmPushInfo.getPrePoint();//获取上一个点经度信息
                AlarmPoint alarmPoin = alarmPushInfo.getCurrentPoint();//获取报警点经纬度等信息
                alarmPoin.getCreateTime();//获取此位置上传到服务端时间
                alarmPoin.getLocTime();//获取定位产生的原始时间
                MonitoredAction monitoredAction = alarmPushInfo.getMonitoredAction();
                LogUtils.d("电子围栏监控" + "围栏id" + fenceId + "    " + "监控对象  " + monitoredPerson + "围栏名称  " + fenceName);
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

    /**
     * 开启GPS
     */
    public void gpsAlertMess() {
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(
                this);
        alert.setTitle("GPS定位").setMessage("请开启GPS定位导航!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 返回开启GPS导航设置界面
                        Intent intent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    }
                });
        alert.setCancelable(false);
        alert.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mTraceClient.stopGather(mTraceListener);
            mTraceClient.stopTrace(mTrace, mTraceListener);
            LogUtils.i("停止鹰眼轨迹服务");
        } catch (Exception e) {

        }
    }
}
