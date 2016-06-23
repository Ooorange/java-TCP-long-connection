package com.orange.blog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.orange.blog.Adapter.AllFeatureAdapter;
import com.orange.blog.Adapter.DividerItemDecoration;

import butterknife.BindView;

/**
 * you need set:LeakCanary.install(this); in application,then you can good to go
 * Created by orange on 16/5/10.
 */

public class MainActivity extends BaseActivity {
    @BindView(R.id.lv_all_feature)
    RecyclerView lv_all_feature;

    AllFeatureAdapter allFeatureAdapter;
    String[] allFeatureName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allFeatureName =getResources().getStringArray(R.array.project_features);
//        allFeatureName =arrs;
        allFeatureAdapter=new AllFeatureAdapter(this, allFeatureName);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        lv_all_feature.setLayoutManager(layoutManager);
        lv_all_feature.setItemAnimator(new DefaultItemAnimator());
        lv_all_feature.addItemDecoration(new DividerItemDecoration(Color.parseColor("#000000"), 2));
        lv_all_feature.setAdapter(allFeatureAdapter);
        initListener();
//        HostInetTest hostInetTest=new HostInetTest();/**InetAddress and URL APi test */
        setRlLeftContainerVisiablity(false);
        setTitle("项目预览");
    }

    @Override
    int initContentView() {
        return R.layout.all_feature_activity;
    }
    private void initListener(){
        allFeatureAdapter.setOnItemtClickListener(new AllFeatureAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=null;
                switch (position){
                    case 0:
                        intent=new Intent(MainActivity.this,MemoryLeakActivity.class);
                        break;
                    case 1:
                        intent=new Intent(MainActivity.this,LeafLoadingActivity.class);
                        break;
                    case 2:
                        intent=new Intent(MainActivity.this,SortActivity.class);
                        break;
                    case 3:
                        intent=new Intent(MainActivity.this,AnimateActivity.class);
                        break;
                    case 4:
                        intent=new Intent(MainActivity.this,SocketTestActivity.class);
                        break;
                    case 5:
                        intent=new Intent(MainActivity.this,MuiltyThreadAcitivity.class);
                        break;
                }
                startActivity(intent);
            }
        });
    }
}
