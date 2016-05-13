package com.example.csdnblog4;

import android.os.Bundle;

/**
 * Created by orange on 16/5/10.
 */
public class MemoryLeak extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_leak);
    }
}
