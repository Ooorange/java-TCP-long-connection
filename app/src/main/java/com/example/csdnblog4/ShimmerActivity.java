package com.example.csdnblog4;

import android.os.Bundle;

import com.example.csdnblog4.View.Shimmer.Shimmer;
import com.example.csdnblog4.View.Shimmer.ShimmerTextView;

import butterknife.BindView;

/**
 * Created by orange on 16/6/6.
 */
public class ShimmerActivity extends BaseActivity {
    @BindView(R.id.tv_shimmer)
    ShimmerTextView shimmerTextView;
    Shimmer shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shimmer=new Shimmer();
        shimmer.start(shimmerTextView);
    }
    @Override
    int initContentView() {
        return R.layout.activity_main;
    }
}
