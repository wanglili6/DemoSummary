package com.example.wll.ceshitablayout.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.example.wll.ceshitablayout.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SignView extends View {
    private Paint linePaint;// 画笔

    private ArrayList<Path> lines;// 写字的笔迹，支持多笔画
    private int lineCount;// 记录笔画数目

    private final int DEFAULT_LINE_WIDTH = 10;// 默认笔画宽度

    private int lineColor = Color.BLACK;// 默认字迹颜色（黑色）
    private float lineWidth = DEFAULT_LINE_WIDTH;// 笔画宽度

    public SignView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();// 普通初始化
        initLinePpaint();
    }

    public SignView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.SignView);

            parseTyepdArray(a);
        }
        init();// 普通初始化
        initLinePpaint();
    }

    public SignView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.SignView);
            parseTyepdArray(a);
        }
        init();// 普通初始化
        initLinePpaint();
    }

    private void parseTyepdArray(TypedArray a) {

        lineColor = a.getColor(R.styleable.SignView_lineColor, Color.RED);
        lineWidth = a.getDimension(R.styleable.SignView_lineWidth, 25);
        a.recycle();
    }

    private void init() {
        lines = new ArrayList<Path>();
    }

    /**
     * 初始化画笔
     */
    private void initLinePpaint() {
        linePaint = new Paint();
        linePaint.setColor(lineColor);// 画笔颜色
        linePaint.setStrokeWidth(lineWidth);// 画笔宽度
        linePaint.setStrokeCap(Cap.ROUND);// 设置笔迹的起始、结束为圆形
        linePaint.setPathEffect(new CornerPathEffect(50));// PahtEfect指笔迹的风格，CornerPathEffect在拐角处添加弧度，弧度半径50像素点
        linePaint.setStyle(Style.STROKE);// 设置画笔风格
        linePaint.setAntiAlias(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Path path = new Path();
            path.moveTo(event.getX(), event.getY());
            lines.add(path);
            lineCount = lines.size();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            lines.get(lineCount - 1).lineTo(event.getX(), event.getY());
            invalidate();
        } else {
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lines != null && lines.size() > 0) {
            for (Path path : lines) {
                canvas.drawPath(path, linePaint);
            }
        }
    }

    // 开放设置画笔颜色和宽度的接口

    /**
     * 开放设置画笔颜色的接口
     *
     * @param lineColor
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        linePaint.setColor(lineColor);
    }

    /**
     * 开放设置画笔宽度的接口
     *
     * @param lineWidth
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        linePaint.setStrokeWidth(lineWidth);
    }

    /**
     * 清空输入
     */
    public void clearPath() {
        lines.removeAll(lines);
        invalidate();
    }

    /**
     * 将图片保存到文件
     *
     * @param signName
     */
    public boolean saveImageToFile(String filePath, String signName) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String localFile = file.getAbsolutePath() + "/" + signName;
            File f = new File(localFile);

            FileOutputStream fos = new FileOutputStream(f);
            getImage().compress(CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将View保存为Bitmap
     */
    public Bitmap getImage() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        // 绘制背景
        Drawable bgDrawable = getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        // 绘制View视图内容
        draw(canvas);
        return bitmap;
    }

}
