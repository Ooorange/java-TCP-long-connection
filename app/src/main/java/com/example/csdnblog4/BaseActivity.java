package com.example.csdnblog4;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created by orange on 16/5/10.
 */

public abstract class BaseActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(savedInstanceState);
        ButterKnife.bind(this);
    }
    abstract void initContentView(Bundle savedInstanceState);
}
