package com.example.csdnblog4;

import android.os.Bundle;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by orange on 16/5/10.
 */
public class MemoryLeakActivity extends BaseActivity {
    @BindView(R.id.circleView)
    CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    int initContentView() {
        return R.layout.activity_memory_leak;
    }
}
