package com.orange.blog;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;

/**
 * 妈的,发现在v7包下自定义actionBar的时候左右两边会出现空白.
 * 解决方案:使baseActivity继承AppCompatActivity,并且使用getSupportActionBar()获取
 * actionBar从而自定义,并且在style文件中<item name="contentInsetStart">0dp</item>
                                    <item name="contentInsetEnd">0dp</item>

 * Created by orange on 16/5/10.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private RelativeLayout rl_left_container;
    private ImageView iv_back, iv_right_button;
    private TextView tv_back_text, tv_title, tv_right_menu_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        initActionBar();
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    abstract int initContentView();

    private void initActionBar() {
        ActionBar actionBar=getSupportActionBar();

        if (actionBar== null) {
            return;
        }

        // 返回箭头（默认不显示）
        actionBar.setDisplayHomeAsUpEnabled(false);
        // 左侧图标点击事件使能
        actionBar.setHomeButtonEnabled(true);
        // 使左上角图标(系统)是否显示
        actionBar.setDisplayShowHomeEnabled(false);
        // 显示标题
        actionBar.setDisplayShowTitleEnabled(false);
        // 显示自定义视图
        actionBar.setDisplayShowCustomEnabled(true);

        View view = LayoutInflater.from(this).inflate(R.layout.actionbar_header, null);
        actionBar.setCustomView(view,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        rl_left_container = (RelativeLayout) view.findViewById(R.id.rl_left_container);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_right_button = (ImageView) view.findViewById(R.id.iv_right_button);
        tv_back_text = (TextView) view.findViewById(R.id.tv_back_text);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_right_menu_text = (TextView) view.findViewById(R.id.tv_right_menu_text);
        setClickListener();
    }

    private void setClickListener() {
        rl_left_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPreviousActivity();
            }
        });
    }

    protected void backPreviousActivity() {
        BaseActivity.this.finish();
    }

    protected void setTitle(String title) {
        tv_title.setText(title);
    }

    protected void setActionBarMenuText(String menuText) {
        tv_right_menu_text.setText(menuText);
        tv_right_menu_text.setVisibility(View.VISIBLE);
        iv_right_button.setVisibility(View.GONE);
        tv_right_menu_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actoinBarTextClick();
            }
        });
    }

    protected void actoinBarTextClick() {
    }

    protected void setActionBarMenuIcon(int viewId) {
        tv_right_menu_text.setVisibility(View.GONE);
        iv_right_button.setVisibility(View.VISIBLE);
        iv_right_button.setImageResource(viewId);
        iv_right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actoinBarIconClick();
            }
        });
    }

    protected void actoinBarIconClick() {
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.push_right_out);
    }

    protected void setRlLeftContainerVisiablity(boolean visiablity){
        rl_left_container.setVisibility(visiablity?View.VISIBLE:View.GONE);
    }
}
