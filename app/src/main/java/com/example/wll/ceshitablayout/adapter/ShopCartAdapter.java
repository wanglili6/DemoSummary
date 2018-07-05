package com.example.wll.ceshitablayout.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.bean.AnimationBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wll on 2018/7/4.
 */

public class ShopCartAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<AnimationBean> mList;
    private PathMeasure mPathMeasure;
    private RelativeLayout mRootRl;
    private ImageView mCarImageView;
    private float[] mCurrentPosition = new float[2];

    public ShopCartAdapter(Context mContext, List<AnimationBean> mList, RelativeLayout mRootRl, ImageView mCarImageView) {
        this.mContext = mContext;
        this.mList = mList;
        this.mRootRl = mRootRl;
        this.mCarImageView = mCarImageView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.shop_cart_itream, null);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        AnimationBean animationBean = mList.get(i);
        holder.tvName.setText(animationBean.getName() + "");
        holder.imgCommondity.setImageBitmap(animationBean.getBitmap());
        holder.imgCommondity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoodToCar(holder.imgCommondity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_commondity)
        ImageView imgCommondity;
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void addGoodToCar(final ImageView imageView) {
        //拿到商品的图片
        final ImageView mview = new ImageView(mContext);
        //获取到图片,用于绘制贝塞尔曲线(一定要设置,不然显示不出来)
        mview.setImageDrawable(imageView.getDrawable());
        //设置这个承载图片的容器,以及设置大小
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(50, 50);
        //添加进去
        mRootRl.addView(mview, layoutParams);

        //二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLoc = new int[2];
        mRootRl.getLocationInWindow(parentLoc);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        imageView.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        mCarImageView.getLocationInWindow(endLoc);

        float startX = startLoc[0] - parentLoc[0] + imageView.getWidth() / 2;
        float startY = startLoc[1] - parentLoc[1] + imageView.getHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLoc[0] + mCarImageView.getWidth() / 5;
        float toY = endLoc[1] - parentLoc[1];

        //开始绘制贝塞尔曲线
        Path path = new Path();
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(path, false);

        //属性动画
        float length = mPathMeasure.getLength();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, length);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                //给图片设置位移坐标
                mview.setTranslationX(mCurrentPosition[0]);
                mview.setTranslationY(mCurrentPosition[1]);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 购物车的数量加1

                // 把移动的图片imageview从父布局里移除
                mRootRl.removeView(mview);

                //shopImg 开始一个放大动画
                Animation scaleAnim = AnimationUtils.loadAnimation(mContext, R.anim.shop_car_scale);
                mCarImageView.startAnimation(scaleAnim);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }
}
