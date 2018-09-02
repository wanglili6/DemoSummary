package com.example.wll.ceshitablayout.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.example.wll.ceshitablayout.R;

/**
 * 遮挡性圆角自定义imageView
 */

public class ClinpImageView extends android.support.v7.widget.AppCompatImageView {
    private float mCornerXValue;
    private float mCornerYValue;
    private float mBorderValue;
    private int mBorderColor;

    Bitmap mDestBitmap;
    Bitmap mSrcBitmap;
    Canvas mDestCanvas;
    Canvas mSrcCanvas;
    Paint mPaintClip;
    Paint mNomalPaint;
    Paint mPaintBoard;
    RectF mRoundRectClip;
    RectF mRoundRectBorder;

    public ClinpImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义参数
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundImageView);
        // board颜色
        mBorderColor = typedArray.getColor(R.styleable.RoundImageView_round_border_color, Color.argb(0, 0, 0, 0));
        // 获取X方向曲率
        mCornerXValue = typedArray.getDimension(R.styleable.RoundImageView_corner_x, 0);
        // 获取Y方向曲率
        mCornerYValue = typedArray.getDimension(R.styleable.RoundImageView_corner_y, 0);
        // board宽
        mBorderValue = typedArray.getDimension(R.styleable.RoundImageView_round_border_width, 0);
        // 用完需要recycle
        typedArray.recycle();

        initPaints();
    }

    private void initPaints() {
        // 创建普通画笔
        if (mNomalPaint == null) {
            mNomalPaint = new Paint();
            mNomalPaint.setAntiAlias(true);
        }

        // 创建遮罩画笔
        if (mPaintClip == null) {
            mPaintClip = new Paint();
            mPaintClip.setAntiAlias(true);
            mPaintClip.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaintClip.setAntiAlias(true);
        }

        // 创建board的画笔
        if (mPaintBoard == null) {
            mPaintBoard = new Paint();
            mPaintBoard.setColor(mBorderColor);
            mPaintBoard.setAntiAlias(true);
            mPaintBoard.setStyle(Paint.Style.STROKE);
            mPaintBoard.setStrokeWidth(mBorderValue);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // 创建遮罩图片和画布
        if (mDestBitmap == null) {
            mDestBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mSrcBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mDestCanvas = new Canvas(mDestBitmap);
            mSrcCanvas = new Canvas(mSrcBitmap);
        }
        // 获取imageview原先的图片
        super.onDraw(mDestCanvas);

        // 创建矩形，表示圆角矩形
        if (mRoundRectClip == null) {
            mRoundRectClip = new RectF(mBorderValue, mBorderValue, getWidth() - mBorderValue, getHeight() - mBorderValue);
        }
        // 绘制圆角矩形
        mSrcCanvas.drawRoundRect(mRoundRectClip, mCornerXValue, mCornerYValue, mNomalPaint);

        // 使用遮罩画笔扣除原图中的圆角矩形外面的部分
        mDestCanvas.drawBitmap(mSrcBitmap, 0, 0, mPaintClip);

        // 创建board的矩形
        if (mRoundRectBorder == null) {
            mRoundRectBorder = new RectF(mBorderValue / 2, mBorderValue / 2, getWidth() - mBorderValue / 2, getHeight() - mBorderValue / 2);
        }
        // 绘制board
        mDestCanvas.drawRoundRect(mRoundRectBorder, mCornerXValue, mCornerYValue, mPaintBoard);

        // 绘制最终的圆角带有board的图
        canvas.drawBitmap(mDestBitmap, 0, 0, mNomalPaint);
    }
}
