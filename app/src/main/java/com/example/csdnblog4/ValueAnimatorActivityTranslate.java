package com.example.csdnblog4;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by orange on 16/5/27.
 */
public class ValueAnimatorActivityTranslate extends BaseActivity {
    @BindView(R.id.iv_delete)
    View iv_delete;
    @BindView(R.id.bt_startY_ani)
    Button bt_startY_ani;

    @Override
    int initContentView() {
        return R.layout.animate_activity;
    }

    @OnClick(R.id.iv_delete)
    public void showToast(){
        Toast.makeText(this, "点击", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.bt_startY_ani)
    public void startYAni(){
        ValueAnimator valueAnimator=ObjectAnimator.ofFloat(iv_delete, "translationY", 0, 220);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(6000);
        valueAnimator.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ValueAnimator valueAnimator= ObjectAnimator.ofInt(iv_delete, "backgroundColor", Color.RED, Color.BLUE, Color.GRAY, Color.GREEN);
        valueAnimator.setDuration(6000);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.start();
    }
}
