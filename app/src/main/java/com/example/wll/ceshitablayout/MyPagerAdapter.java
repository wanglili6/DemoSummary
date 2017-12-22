package com.example.wll.ceshitablayout;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by wll on 2017/12/5.
 */

class MyPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private ArrayList<Fragment> mFragments;
    private  String[] mTitles;
    private  FragmentManager fm;

    public MyPagerAdapter(FragmentManager fm, Context context, ArrayList<Fragment> mFragments, String[] mTitles) {
        super(fm);
        this.context = context;
        this.mFragments = mFragments;
        this.mTitles = mTitles;
    }


    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }



}