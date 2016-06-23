package com.orange.blog.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.orange.blog.R;

/**
 * Created by orange on 16/2/25.
 */
public class VideoControlSurfaceView extends SurfaceView {
    float xAxisDistance;//x轴的滑动距离
    float yAxisDistance;//y轴的滑动距离
    ViewGroup mDecorView;//当前activity的布局
    ViewGroup mMediaView;//音量,亮度的根布局

    private onSurfaceViewMotionEventListener mMotionEventListener;
    public VideoControlSurfaceView(Context context) {
        super(context);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoControlSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public VideoControlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoControlSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void setonSurfaceViewMotionEventListener(onSurfaceViewMotionEventListener listener){
        this.mMotionEventListener=listener;
    }
    public void init(Context context){
        LayoutInflater layoutInflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.media_control_view,null);
    }


    //实现控制音量,亮度,进度
    float mCurX=0;
    float mCurY=0;
    float mLastX=0;
    float mLastY=0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mCurX=event.getX();
                mCurY=event.getY();

                break;
            case MotionEvent.ACTION_MOVE:


                break;
            case MotionEvent.ACTION_UP:
                xAxisDistance=event.getX()-mLastX;
                yAxisDistance=event.getY()-mLastY;
                Log.d("orangeTouched", "OnVideoControl"+xAxisDistance+","+yAxisDistance+",viewWidth:"+getWidth());
                if (Math.abs(yAxisDistance)-Math.abs(xAxisDistance)<0){//x轴滑动距离比y大,说明是水平滑动
                    if (mMotionEventListener!=null)//控制进度
                        mMotionEventListener.onSurfaceViewMotionChanged(0,xAxisDistance/getWidth());
                }else if (event.getX()<getWidth()*0.4){//左半边滑动
                    if (Math.abs(yAxisDistance)>Math.abs(xAxisDistance)){//而且是垂直滑动,说明是想控制亮度
                        if (mMotionEventListener!=null){
                            mMotionEventListener.onSurfaceViewMotionChanged(1,-yAxisDistance/getHeight());
                        }
                    }
                }else if (event.getX()>getWidth()*0.7){//右半边滑动
                    Log.d("orange","event.getX()>getWidth()*0.7");
                    if (Math.abs(yAxisDistance)>Math.abs(xAxisDistance)){//而且是垂直滑动,说明是想控制音量
                        if (mMotionEventListener!=null){
                            mMotionEventListener.onSurfaceViewMotionChanged(2,-yAxisDistance/getHeight());
                        }
                    }
                }
                break;
        }
        mLastX=mCurX;
        mLastY=mCurY;
        return true;
    }


    public interface onSurfaceViewMotionEventListener{
        /**
         * 手势滑动回调
         * @param type 0:水平滑动,控制播放进度,1:左边垂直滑动,控制亮度 ,2:右边垂直滑动,控制音量
         * @param percent  滑动距离占控件的比例
         */
        void onSurfaceViewMotionChanged(int type,float percent);
    }
}
