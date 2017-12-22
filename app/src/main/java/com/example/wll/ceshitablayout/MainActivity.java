package com.example.wll.ceshitablayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.wll.ceshitablayout.R.attr.title;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPression();
        tabLayout = (CommonTabLayout) findViewById(R.id.tl_2);
        viewpager = (ViewPager) findViewById(R.id.vp_2);

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
        tabLayout.showMsg(1, 3);
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
//                    Intent intent = new Intent(MainActivity.this, WordHtmlActivity.class);
//                    startActivity(intent);
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


    }

    /**
     * 动态添加权限
     */
    public void getPression() {
        int checkSelfPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
            //没有权限，申请权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.VIBRATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            //已经有了权限 ，不需要申请
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "已经授权成功了", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

}
