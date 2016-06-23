package com.orange.blog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.orange.blog.Iview.ISortView;
import com.orange.blog.iPresenter.SortPresenter;
import com.orange.blog.iPresenter.SortPresenterImp;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by orange on 16/5/26.
 */
public class SortActivity extends BaseActivity implements ISortView {
    @BindView(R.id.rg_sort_algorithm)
    RadioGroup rg_sort_algorithm;
    @BindView(R.id.tv_output_data)
    TextView tv_output_data;
    @BindView(R.id.tv_data)
    TextView tv_data;
    Dialog dialog;
    SortPresenter basePresenter;
    public static final String[] items={"桶子排序","快速排序"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        basePresenter=new SortPresenterImp(this,this);
        addRadioGroup(items);
        rg_sort_algorithm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                basePresenter.setAlgorithm(checkedId);
            }
        });
    }

    @OnClick(R.id.bt_getData)
    public void getData(){
        basePresenter.getData();
    }

    @Override
    int initContentView() {
        return R.layout.sort_activity;
    }

    public void addRadioGroup(String[] itemName) {
        rg_sort_algorithm.setOrientation(LinearLayout.HORIZONTAL);
        for (int i=0,size=itemName.length;i<size;i++) {
            RadioButton radioButton=new RadioButton(this);
            radioButton.setText(itemName[i]);
            rg_sort_algorithm.addView(radioButton);
            if (i==0){
                rg_sort_algorithm.check(radioButton.getId());
            }
        }
    }

    @Override
    public void showRandomData(String data,int dataArr[]) {
        tv_data.setText("随机输入数据为:"+data);
    }

    @Override
    public void showResutlAfterSort(String data) {
        tv_output_data.setText(data);
    }

    @Override
    public void showDialog() {
        dialog=new AlertDialog.Builder(this)
                .setTitle("正在获取数据")
                .setMessage("正在获取数据")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void dismissDialog() {
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
