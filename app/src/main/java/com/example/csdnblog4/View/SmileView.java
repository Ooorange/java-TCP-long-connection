package com.example.csdnblog4.View;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * onMeasure 确定view以及childview的大小
 * onLayout 确定childView的位置
 * onSizeChanged 确定View的大小
 * 在绘制图形前要先将画布旋转移动,
 * Created by orange on 16/5/27.
 */
public class SmileView extends View{

    private int mHeight;
    private int mWidth;
    private Paint mPaint;
    private TimeInterpolator interpolator;
    private ValueAnimator valueAnimator;
    private float valueAnimatorValue;
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

    public void initAnimator(){
        interpolator=new DecelerateInterpolator();
        if (valueAnimator!=null&&valueAnimator.isRunning()){
            valueAnimator.cancel();
            valueAnimator.start();
        }else {
            valueAnimator=ValueAnimator.ofFloat(0,855).setDuration(5000);
            valueAnimator.setInterpolator(interpolator);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    valueAnimatorValue= (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.start();
        }
    }

    //绘制旋转过程中的嘴巴以及眼镜
    private void drawSmile(Canvas canvas){
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(15);
        mPaint.setColor(Color.rgb(97,195, 109));
        float point=Math.min(mHeight,mWidth)*0.2f/2;
        float rf= point*(float) Math.sqrt(2);
        RectF rectF=new RectF(-rf,-rf,rf,rf);

        canvas.save();
        //画布旋转的中心点在画布的圆点,顺时针旋转,此时x,y坐标轴也会旋转
        if (valueAnimatorValue>=135){
            canvas.rotate(valueAnimatorValue-135);
        }

        //绘制嘴巴
        float sweepAngle=0,startAngle=0;
        if (valueAnimatorValue<135){
            startAngle = valueAnimatorValue +5;
            sweepAngle = 170+valueAnimatorValue/3;
        }else if (valueAnimatorValue<270){
            startAngle = 135+5;
            sweepAngle = 170+valueAnimatorValue/3;
        }else if (valueAnimatorValue<630){
            startAngle = 135+5;
            sweepAngle = 260-(valueAnimatorValue-270)/5;
        }else if (valueAnimatorValue<720){
            startAngle = 135-(valueAnimatorValue-630)/2+5;
            sweepAngle = 260-(valueAnimatorValue-270)/5;
        }else{
            startAngle = 135-(valueAnimatorValue-630)/2-(valueAnimatorValue-720)/6+5;
            sweepAngle = 170;
        }

        canvas.drawArc(rectF, startAngle, sweepAngle, false, mPaint);

        //绘制眼镜
        canvas.drawPoints(new float[]{point, -point, -point, -point}, mPaint);
        canvas.drawCircle(point, -point, 0.5f, mPaint);
        canvas.drawCircle(-point,-point,0.5f,mPaint);
        canvas.restore();
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

//        //绘制矩形
//        mPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(-mWidth / 8, -mHeight / 8, mWidth / 8, mHeight / 8, mPaint);
//
//        //绘制矩形,将画布平移,之前已经绘制的地方不会在改变位置
//        canvas.translate(200, 200);
//        mPaint.setColor(Color.BLUE);
//        canvas.drawRect(-mWidth / 8, -mHeight / 8, mWidth / 8, mHeight / 8, mPaint);
//
//        //绘制矩形,错切
//        canvas.skew(1.0f, 0.5f);
//        mPaint.setColor(Color.RED);
//        canvas.drawRect(-mWidth / 8, -mHeight / 8, mWidth / 8, mHeight / 8, mPaint);
//
//        //绘制圆,测试画布的保存与恢复save(),restore(),restore()在save()之后调用,可以在save()与restore()之间对canvas的改变丢弃
//        canvas.translate(-200,-200);
//        canvas.drawCircle(0, 0, 30, mPaint);
//
//        canvas.save();
//
//        canvas.translate(200, 200);
//        mPaint.setColor(Color.YELLOW);
//        canvas.drawCircle(0, 0, 50, mPaint);
//
//        canvas.restore();
//        canvas.drawCircle(0,0,50,mPaint);//可以看到之前对canvas的位移操作被丢弃了,restore()之后canvas位置还是save()之前的状态

//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.GREEN);
//        mPaint.setStrokeWidth(10);
//        float point=Math.min(mHeight, mWidth)*0.2f/2;
//        float rf=point*(float)Math.sqrt(2);
//        RectF rectF=new RectF(-rf,-rf,rf,rf);
//        canvas.drawPoints(new float[]{point, -point, -point, -point}, mPaint);
//        canvas.drawArc(rectF,0,180,false,mPaint);

        drawSmile(canvas);
    }

    private void initPaint(){
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        initAnimator();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mHeight=h;
        this.mWidth=w;
    }
}
