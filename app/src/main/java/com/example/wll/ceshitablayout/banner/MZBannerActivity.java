package com.example.wll.ceshitablayout.banner;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wll.ceshitablayout.R;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MZBannerActivity extends AppCompatActivity {


    @BindView(R.id.banner)
    MZBannerView banner;
    @BindView(R.id.banner_normal)
    MZBannerView bannerNormal;
    private String TAG = "mzjjjj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mzbanner);
        ButterKnife.bind(this);
        banner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                Toast.makeText(MZBannerActivity.this, "click page:" + position, Toast.LENGTH_LONG).show();
            }
        });
        banner.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e(TAG, "----->addPageChangeLisnter:" + position + "positionOffset:" + positionOffset + "positionOffsetPixels:" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "addPageChangeLisnter:" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.img_1);
        list.add(R.mipmap.img_2);
        list.add(R.mipmap.img_3);
        list.add(R.mipmap.img_4);
        list.add(R.mipmap.img_5);

        List<Integer> bannerList = new ArrayList<>();
        bannerList.add(R.mipmap.img_1);
        bannerList.add(R.mipmap.img_2);
        bannerList.add(R.mipmap.img_3);
        bannerList.add(R.mipmap.img_4);
        bannerList.add(R.mipmap.img_5);
        banner.setIndicatorVisible(true);
        // 代码中更改indicator 的位置
        //mMZBanner.setIndicatorAlign(MZBannerView.IndicatorAlign.LEFT);
        //mMZBanner.setIndicatorPadding(10,0,0,150);
        banner.setPages(bannerList, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });

        bannerNormal.setPages(list, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
    }


    public static class BannerViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            // 数据绑定
            mImageView.setImageResource(data);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        banner.pause();
        bannerNormal.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.start();
        bannerNormal.start();
    }
}
