package com.example.csdnblog4;

import android.os.Bundle;

import com.example.csdnblog4.View.SmileView;

import butterknife.BindView;

/**
 *
 *
 * Created by orange on 16/5/27.
 */
public class SmileActivity extends BaseActivity {
    @BindView(R.id.sv_smile)
    SmileView smileView;
    @Override
    void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.smile_activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        smileView.initAnimator();
    }
}
