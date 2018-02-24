package com.example.wll.ceshitablayout;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.apkfuns.logutils.LogUtils;
import com.example.wll.ceshitablayout.adapter.MyPagerAdapter;
import com.example.wll.ceshitablayout.constant.TabEntity;
import com.example.wll.ceshitablayout.homePageFragment.HomeFragment;
import com.example.wll.ceshitablayout.homePageFragment.MoreFragment;
import com.example.wll.ceshitablayout.homePageFragment.NewsFragment;
import com.example.wll.ceshitablayout.homePageFragment.PersonFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewMainActivity extends AppCompatActivity {

    @BindView(R.id.main_fragment)
    FrameLayout mainFragment;
    @BindView(R.id.bottom_tab1)
    CommonTabLayout bottomTab1;
    @BindView(R.id.bottom_tab_jiao)
    CommonTabLayout bottomTabJiao;
    @BindView(R.id.bottom_tab2)
    CommonTabLayout bottomTab2;
    /*文本信息*/
    private String[] mTitles = {"消息", "工作台", "", "通讯录", "我的"};
    /*未选择时的icon*/
    private int[] mIconUnselectIds = {
            R.mipmap.new_unselect, R.mipmap.work_unselect, R.mipmap.jiahao,
            R.mipmap.person_unselect, R.mipmap.my_unselect};
    /*选择时的icon*/
    private int[] mIconSelectIds = {
            R.mipmap.new_select, R.mipmap.work_select, R.mipmap.jiahao,
            R.mipmap.person_select, R.mipmap.my_select};
    ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    ArrayList<Fragment> mFragments = new ArrayList<>();
    private String TAG = "首页的log";
    HomeFragment homeFragment = new HomeFragment();
    PersonFragment personFragment = new PersonFragment();
    NewsFragment newsFragment = new NewsFragment();
    MoreFragment moreFragment = new MoreFragment();
    private int selectint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        ButterKnife.bind(this);

        /*添加数据集*/

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_fragment, newsFragment);
        transaction.add(R.id.main_fragment, homeFragment);
        transaction.add(R.id.main_fragment, personFragment);
        transaction.add(R.id.main_fragment, moreFragment);
        transaction.hide(newsFragment);
        transaction.hide(personFragment);
        transaction.hide(moreFragment);
        transaction.show(homeFragment);
        transaction.commit();
        for (int i = 0; i < mTitles.length; i++) {
            String mTitle = mTitles[i];
            if (mTitle.equals("消息")) {
                mFragments.add(homeFragment);
            }
            if (mTitle.equals("工作台")) {
                mFragments.add(newsFragment);
            }
            if (mTitle.equals("通讯录")) {
                mFragments.add(personFragment);
            }
            if (mTitle.equals("我的")) {
                mFragments.add(moreFragment);
            }
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        bottomTab1.setTabData(mTabEntities);
        bottomTab1.setCurrentTab(1);
        selectint = 1;
        bottomTab1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                Log.i(TAG, "onTabSelect: " + position);
                if (position == 0) {
                    selectint = 0;
                    transaction.show(newsFragment);
                    transaction.hide(personFragment);
                    transaction.hide(moreFragment);
                    transaction.hide(homeFragment);
                    transaction.commit();
                } else if (position == 1) {
                    selectint = 1;
                    transaction.hide(newsFragment);
                    transaction.hide(personFragment);
                    transaction.hide(moreFragment);
                    transaction.show(homeFragment);
                    transaction.commit();
                } else if (position == 3) {
                    selectint = 3;
                    transaction.hide(newsFragment);
                    transaction.show(personFragment);
                    transaction.hide(moreFragment);
                    transaction.hide(homeFragment);
                    transaction.commit();
                } else if (position == 4) {
                    selectint = 4;
                    transaction.hide(newsFragment);
                    transaction.hide(personFragment);
                    transaction.show(moreFragment);
                    transaction.hide(homeFragment);
                    transaction.commit();
                } else if (position == 2) {
                    bottomTab1.setCurrentTab(selectint);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


}
