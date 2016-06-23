package com.orange.blog.iPresenter;

import android.app.Activity;

import com.orange.blog.Iview.ISortView;

/**
 * Created by orange on 16/5/26.
 */
public class SortPresenterImp implements SortPresenter {
    private Activity context;
    private ISortView sortView;
    private int algorithm=1;
    public SortPresenterImp(Activity context,ISortView sortView){
        this.context=context;
        this.sortView=sortView;
    }


    @Override
    public void getData() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sortView.showDialog();
                int data[] = new int[1000];
                StringBuilder builder = new StringBuilder("");
                for (int i = 0; i < 1000; i++) {
                    data[i] = (int) (Math.random() * 100);
                    builder.append(data[i] + ",");
                }
                setAlgorithmImp(algorithm,data);
                sortView.showRandomData(builder.toString(), data);//显示输入的数据
                sortView.dismissDialog();
            }
        });
    }

    @Override
    public void setAlgorithm(int algorithm) {
        this.algorithm=algorithm;
    }

    /**
     * 桶子排序,适合用于知道最大数或是最小数据的排序
     * @param data
     * @return result after caskSort
     */
    public int[] caskSort(int data[]){
        int result[]=new int[1001];//桶子
        //将桶子初始化为0
        for (int i=0,size=result.length;i<size;i++){
            result[i]=0;
        }
        //将桶子与数值对应
        for (int j=0,size=data.length;j<size;j++){
            result[data[j]]++;//对编号为data[j]的桶子计数
        }
        StringBuilder builder=new StringBuilder("");
        //倒叙输出,桶子编号越大里面的值越大
        for (int k=1000;k>=1;k--){
            for (int t=0;t<result[k];t++){//从1开始计数,出现几次打印几次
                builder.append(k+",");
            }
        }
        sortView.showResutlAfterSort(builder.toString());
        return result;
    }

    public void setAlgorithmImp(int id,int[] data){
        switch (id){
            case 1:
                caskSort(data);
                break;
            case 2:
                break;
            default:
                caskSort(data);
        }
    }

}
