package com.example.csdnblog4.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * onMeasure 确定view以及childview的大小
 * onLayout 确定childView的位置
 * onSizeChanged 确定View的大小
 * Created by orange on 16/5/27.
 */
public class SmileView extends View{

    private int mHeight;
    private int mWidth;
    private Paint mPaint;
    public SmileView(Context context) {
        super(context);
        initPaint();
    }

    public SmileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public SmileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmileView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mWidth / 2, mHeight / 2);//将画布坐标原点放置到中心位置
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10);
        canvas.drawPoint(0, 0, mPaint);

        //绘制点
        canvas.drawPoints(new float[]{mWidth / 2 * 0.8f, 0, 0, -mHeight / 2 * 0.8f, -mWidth / 2 * 0.8f, 0, 0, mHeight / 2 * 0.8f}, mPaint);//右,上,左,下

        //绘制十字架
        mPaint.setStrokeWidth(1);
        canvas.drawLines(new float[]{mWidth / 2 * 0.8f, 0, -mWidth / 2 * 0.8f, 0, 0, -mHeight / 2 * 0.8f, 0, mHeight / 2 * 0.8f}, mPaint);

        //绘制箭头
        canvas.drawLines(new float[]{mWidth / 2 * 0.8f * 0.95f, mWidth / 2 * 0.8f * 0.05f,
                mWidth / 2 * 0.8f, 0,
                mWidth / 2 * 0.8f * 0.95f, -mWidth / 2 * 0.8f * 0.05f,
                mWidth / 2 * 0.8f, 0,
                0, mHeight / 2 * 0.8f,
                -mWidth / 2 * 0.8f * 0.05f, mHeight / 2 * 0.8f * 0.96f,
                0, mHeight / 2 * 0.8f,
                mWidth / 2 * 0.8f * 0.05f, mHeight / 2 * 0.8f * 0.96f,
        }, mPaint);

        //绘制矩形
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(-mWidth / 8, -mHeight / 8, mWidth / 8, mHeight / 8, mPaint);

        //绘制矩形,将画布平移,之前已经绘制的地方不会在改变位置
//        canvas.translate(200, 200);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(-mWidth / 8, -mHeight / 8, mWidth / 8, mHeight / 8, mPaint);

        //绘制矩形,错切
//        canvas.skew(1.0f, 0.5f);
        mPaint.setColor(Color.RED);
        canvas.drawRect(-mWidth / 8, -mHeight / 8, mWidth / 8, mHeight / 8, mPaint);

        //绘制圆,测试画布的保存与恢复save(),restore(),restore()在save()之后调用,可以在save()与restore()之间对canvas的改变丢弃
//        canvas.translate(-200,-200);
        canvas.drawCircle(0, 0, 30, mPaint);

        canvas.save();

        canvas.translate(200, 200);
        mPaint.setColor(Color.YELLOW);
        canvas.drawCircle(0, 0, 50, mPaint);

        canvas.restore();
        canvas.drawCircle(0,0,50,mPaint);//可以看到之前对canvas的位移操作被丢弃了,restore()之后canvas位置还是save()之前的状态


    }

    private void initPaint(){
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mHeight=h;
        this.mWidth=w;
    }
}
