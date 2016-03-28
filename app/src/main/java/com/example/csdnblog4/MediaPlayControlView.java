package com.example.csdnblog4;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by orange on 16/2/25.
 */
public class MediaPlayControlView extends View {
    Paint paint;//画笔
    Rect rectForCenterImage;
    int totalCount;//总块数
    Bitmap centerImageBitmap;//中间的图片
    int itemSplitSize;//块之间的分割间距
    int showCount;//需要显示的快数
    int arcWidth;//圆环的宽度
    int volumerBackColor;//所有块的背景
    int volumeColor;//显示块的背景
    Canvas mCanvas;//画布
    public MediaPlayControlView(Context context) {
        this(context, null);
    }

    public MediaPlayControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context,attrs,0);
    }

    public MediaPlayControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs,defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MediaPlayControlView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context,attrs,defStyleAttr);
    }
    public void initView(Context context,AttributeSet attrs, int defStyleAttr){
        paint=new Paint();
        rectForCenterImage=new Rect();
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.MediaPlayControlView,defStyleAttr,0);
        totalCount=typedArray.getInt(R.styleable.MediaPlayControlView_totalCount,15);
        centerImageBitmap=BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(R.styleable.MediaPlayControlView_centerImage, 0));
        itemSplitSize=typedArray.getInt(R.styleable.MediaPlayControlView_itemSplitSize, 20);
        showCount=typedArray.getInt(R.styleable.MediaPlayControlView_showCount, 7);
        arcWidth=typedArray.getInt(R.styleable.MediaPlayControlView_arcWidth, 20);
        volumerBackColor=typedArray.getColor(R.styleable.MediaPlayControlView_volumerBackColor, Color.YELLOW);
        volumeColor=typedArray.getColor(R.styleable.MediaPlayControlView_volumeColor,Color.GRAY);
        typedArray.recycle();
        int b;
        int a;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius=getWidth()/2-arcWidth/2;//获得画圆弧的半径,20为圆弧的宽度
        paint.setStrokeCap(Paint.Cap.ROUND);//圆形画笔
        paint.setAntiAlias(true);//消除锯齿
        paint.setStrokeWidth(arcWidth);
        paint.setStyle(Paint.Style.STROKE);//空心
        mCanvas=canvas;
        drawOval(radius);
        int interCircleRadius=radius-arcWidth/2;
        rectForCenterImage.left= (int) (arcWidth+(interCircleRadius-interCircleRadius*1.0f/Math.sqrt(2)));
        rectForCenterImage.top=(int) (arcWidth+(interCircleRadius-interCircleRadius*1.0f/Math.sqrt(2)));
        rectForCenterImage.right= (int) (rectForCenterImage.left +2*interCircleRadius*1.0f/Math.sqrt(2));
        rectForCenterImage.bottom=(int) (rectForCenterImage.left +2*interCircleRadius*1.0f/Math.sqrt(2));

        mCanvas.drawBitmap(centerImageBitmap, null, rectForCenterImage, paint);
        super.onDraw(canvas);
    }
    private void drawOval(int radius){
        int center=getWidth()/2;
        float itemSize=(360*1.0f-itemSplitSize*totalCount)/totalCount;//设置splitSize=10,总数TotalCount为10
        RectF rectF=new RectF(center-radius,center-radius,center+radius,center+radius);
        paint.setColor(volumerBackColor);
        for (int i=0;i<totalCount;i++){//全部的快快
            mCanvas.drawArc(rectF,i*(itemSize+itemSplitSize),itemSize,false,paint);
        }

        paint.setColor(volumeColor);
        for (int i=0;i<showCount;i++){//设置高亮的快快
            mCanvas.drawArc(rectF,i*(itemSize+itemSplitSize),itemSize,false,paint);
        }
    }/**
     *设置显示数量
     */
    public void setShowCount(int add){
        if (add+showCount>totalCount){
            this.showCount=totalCount;
            add=0;
        }
        if (add+showCount<0){
            add=0;
            this.showCount=0;
        }

        this.showCount=add+showCount;
        postInvalidate();
    }

    /**
     * 设置中心图片
     * @param centerPic
     */
    public void setCenterImage(Bitmap centerPic){
        if (centerPic!=null) {
            centerImageBitmap=centerPic;
            postInvalidate();
        }
    }

    /**
     * 设置最大块数
     * @param maxArc
     */
    public void setMaxCount(int maxArc){
        if (maxArc>=15)
            return;
        if (maxArc<=2){
            maxArc=2;
        }
        this.totalCount=maxArc;
    }
    public int getMaxCount(){
        return totalCount;
    }

    /**
     * 获得当前的显示数量
     * @return
     */
    public int getCurCount(){
        return showCount;
    }
}
