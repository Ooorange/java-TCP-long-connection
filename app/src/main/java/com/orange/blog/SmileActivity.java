package com.orange.blog;

import android.os.Bundle;

import com.orange.blog.View.SmileView;

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
    int initContentView() {
        return R.layout.smile_activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        smileView.initAnimator();
    }
}
