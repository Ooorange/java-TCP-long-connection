package com.example.csdnblog4;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by orange on 16/2/18.
 */
public class VideoPlayActivity extends FragmentActivity implements SurfaceHolder.Callback{
    Button btStop,btPlay,btPause;
    TextView tvVolume,tvPlayPos;
    MediaPlayer mediaPlayer;
    SurfaceHolder surfaceHolder;
    VideoControlSurfaceView surfaceView;
    SeekBar playSeekBar,volumSeekBar;
    AudioManager volumManager;
    WindowManager mWindowManager;
    int thousand=1000;
    float progress;//播放 seekBar的进度
    int mMaxVolume;//最大音量
    int mCurVolume=-1;//当前音量
    String videoTotalTime;//视频总时长
    int videoTotalTimeInt;//视频总时长
    MediaPlayControlView mMediaPlayControlView;//
    ImageView ivStart,ivVideoThumbnail;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            postDelayed(runnable,0);
            super.handleMessage(msg);
        }
    };
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 1000);
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    progress = (float) mediaPlayer.getCurrentPosition() / (float) videoTotalTimeInt;
                    playSeekBar.setProgress(Math.round(progress * 100.0f / 1));
                    tvPlayPos.setText("播放进度:" + TimeUtils.formatTime((long) mediaPlayer.getCurrentPosition()) + "/" + videoTotalTime);
                }

            }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_one);
        volumManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume=volumManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        initView();
        initListener();
        initHolder();
        Log.d("orangePath", "onCreate");
    }

    private void initHolder() {
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);
//        surfaceHolder.setFixedSize(200,320);
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//Surface类型
    }

    private void initListener() {
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                ivStart.setVisibility(View.VISIBLE);
            }
        });
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivStart.setVisibility(View.GONE);
                ivVideoThumbnail.setVisibility(View.GONE);
                mediaPlayer.start();
                if (!handler.hasMessages(0)){
                    handler.sendEmptyMessage(0);
                }

            }
        });
        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                ivStart.setVisibility(View.VISIBLE);
            }
        });
        playSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo((int) ((float) seekBar.getProgress() / 100f * videoTotalTimeInt));
//                mediaPlayer.start();
            }
        });
        ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                ivStart.setVisibility(View.GONE);
                ivVideoThumbnail.setVisibility(View.GONE);
                if (!handler.hasMessages(0)){
                    handler.sendEmptyMessage(0);
                }
            }
        });
        volumSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mCurVolume < 0) {
                    mCurVolume = volumManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if (mCurVolume < 0) {
                        mCurVolume = 0;
                    }
                }
                int index = (int) (seekBar.getProgress() * 1.0f / 100 * mMaxVolume);
                if (index > mMaxVolume) {
                    index = mMaxVolume;
                } else if (index < 0) {
                    index = 0;
                }
                volumManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
                tvVolume.setText("音量" + seekBar.getProgress() + "%");
            }
        });
        surfaceView.setonSurfaceViewMotionEventListener(new VideoControlSurfaceView.onSurfaceViewMotionEventListener() {
            @Override
            public void onSurfaceViewMotionChanged(int type, float percent) {
                Log.d("orangeType",""+type+",percent"+percent);
                switch (type){
                    case 0://进度
                        if(percent<0){
                            thousand=-1000;
                        }else {
                            thousand=1000;
                        }
                        if (Constance.ZEROPOINTEIGHT>Math.abs(percent)&&Math.abs(percent)>Constance.ZEROPOINTFOUR) {//快进10s
                            if (mediaPlayer.getCurrentPosition() + 20 * thousand < videoTotalTimeInt) {
                                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10 * thousand );
                            }else {
                                mediaPlayer.seekTo(videoTotalTimeInt);
                                mediaPlayer.pause();
                            }
                        }else if (Math.abs(percent)>Constance.ZEROPOINTEIGHT){//快进30s
                            if (mediaPlayer.getCurrentPosition() + 40 * thousand < videoTotalTimeInt) {
                                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 30 * thousand );
                            }else {
                                mediaPlayer.seekTo(videoTotalTimeInt);
                                mediaPlayer.pause();
                            }
                        }else {////快进5s
                            if (mediaPlayer.getCurrentPosition() + 10 * thousand < videoTotalTimeInt) {
                                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5 * thousand );
                            }else {
                                mediaPlayer.seekTo(videoTotalTimeInt);
                                mediaPlayer.pause();
                            }
                        }
                        break;
                    case 1://亮度
                        WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
                        Log.d("orangeS", "screenBrightness:"+layoutParams.screenBrightness);
                        layoutParams.screenBrightness=percent+layoutParams.screenBrightness;
                        if (layoutParams.screenBrightness>1.0f){
                            layoutParams.screenBrightness=1.0f;
                        }else if (layoutParams.screenBrightness<0.01f){
                            layoutParams.screenBrightness=0.01f;
                        }
                        getWindow().setAttributes(layoutParams);
                        mMediaPlayControlView.setCenterImage(BitmapFactory.decodeResource(getResources(), R.drawable.lights));
                        mMediaPlayControlView.setShowCount((int) (percent*mMediaPlayControlView.getMaxCount()));
                        break;
                    case 2://音量
                        mCurVolume= Math.round(percent*mMaxVolume)+
                                volumManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        if (mCurVolume>mMaxVolume){
                            mCurVolume=mMaxVolume;
                        }
                        volumManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurVolume, 0);
                        int showProgress=Math.round((float) mCurVolume / (float) mMaxVolume * 100f);
                        if (showProgress<0){
                            showProgress=0;
                        }
                        volumSeekBar.setProgress(showProgress);
                        tvVolume.setText("音量" + showProgress + "%");
                        mMediaPlayControlView.setCenterImage(BitmapFactory.decodeResource(getResources(), R.drawable.volume));
                        mMediaPlayControlView.setShowCount((int) (percent * mMediaPlayControlView.getMaxCount()));
                        break;
                }
            }
        });
    }

    private void initView() {
        btStop= (Button) findViewById(R.id.stop);
        btPlay= (Button) findViewById(R.id.play);
        btPause= (Button) findViewById(R.id.pause);
        surfaceView= (VideoControlSurfaceView) findViewById(R.id.svSurfaceView);
        playSeekBar = (SeekBar) findViewById(R.id.seekBar);
        volumSeekBar= (SeekBar) findViewById(R.id.volumSeekBar);
        tvVolume= (TextView) findViewById(R.id.tvVolume);
        tvPlayPos= (TextView) findViewById(R.id.tvPlayPos);
        ivStart= (ImageView) findViewById(R.id.ivStart);
        ivVideoThumbnail= (ImageView) findViewById(R.id.ivVideoThumbnail);
        mMediaPlayControlView= (MediaPlayControlView) findViewById(R.id.mediaPlayControlView);
        playSeekBar.setMax(100);
        volumSeekBar.setMax(100);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDisplay(surfaceHolder);
//        mediaPlayer.setVolume(0,0);
        String path= Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/77.mp4";
        try {

            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Toast.makeText(this,"文件不存在",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playSeekBar.setProgress(100);
                tvPlayPos.setText(videoTotalTime);
                Toast.makeText(VideoPlayActivity.this, "播放结束", Toast.LENGTH_SHORT).show();
            }
        });
        volumSeekBar.setProgress(50);
        videoTotalTimeInt=mediaPlayer.getDuration();
        videoTotalTime = TimeUtils.formatTime((long) mediaPlayer.getDuration());
        tvPlayPos.setText("播放进度:" + 0 + "/" + videoTotalTime );
        tvVolume.setText("音量" +50+ "%");
        ivVideoThumbnail.setImageBitmap(UiUtils.getVideoThumbnail(path));
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onDestroy() {
        Log.d("orangePath","onDestroy");
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        handler.removeMessages(0);
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        Log.d("orangePath","onResume");
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("orangePath","onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    /**
     * 在manifest文件中如果添加了config change参数的话不会重新调用onCreate,onSaveInstanceState,onRestainInstance
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("orangePath","onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }
}
