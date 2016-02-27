package com.example.csdnblog4;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

/**
 * Created by orange on 16/2/18.
 */
public class VideoPlay extends FragmentActivity {
    TextView tvFeature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_one);
        initView();
    }

    private void initView() {
        tvFeature= (TextView) findViewById(R.id.tvNewFeatureTxt);
    }

}
