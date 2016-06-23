package com.orange.blog;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;

/**
 * Created by orange on 16/6/7.
 */
public class MuiltyThreadAcitivity extends BaseActivity {
    @BindView(R.id.tv_thread_run)
    TextView tv_thread_run;
    @Override
    int initContentView() {
        return R.layout.muilty_thread_act;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("多线程");
//        MuiltyDemo muiltyDemo=new MuiltyDemo();
//        MuiltyDemo muiltyDemo2=new MuiltyDemo();
//        MuiltyDemo muiltyDemo3=new MuiltyDemo();
//        MuiltyDemo muiltyDemo4=new MuiltyDemo();
//
//        muiltyDemo.start();
//        muiltyDemo2.start();
//        muiltyDemo3.start();
//        muiltyDemo4.start();

        MuiltyDemo muiltyDemo=new MuiltyDemo();
        Thread thread1=new Thread(muiltyDemo);
        Thread thread2=new Thread(muiltyDemo);
        Thread thread4=new Thread(muiltyDemo);
        Thread thread3=new Thread(muiltyDemo);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }


    /**
     * 只有start()才能启动创建线程,run()方法只是保存需要在线程中执行的代码,
     * thread.run()是在父线程中执行,相当于同步,也就是说还是处于一个单线程环境中
     * start()方法调用后不一定会马上执行,需要等待CPU资源切换到该线程,
     * 当你wait()或是sleep()结束后,不一定马上运行,也需要等待资源切换,线程处于阻塞(等待)状态
     *
     * 实现:多个窗口同时卖票;问题:重复卖票;多出卖票
     * 多个线程贡献一个资源:资源可用static标记;当多个线程在操作同一个共享数据时,一个线程对多条语句只执行了一部分
     * ,还没执行完,另一个线程参与进来执行,会导致共享数据的错误,解决办法对操作共享数据的语句加同步
     * 对同一个资源进行同步需要保存这几个线程所持有的锁是同一个锁
     * 静态同步函数使用的锁不是this,而是***.class
     * 死锁出现的原因:同步锁互相嵌套,this要obj,obg要this
     * 实现runnable接口或是继承Thread,实现runnable好处是不会有java单继承的局限性,区别就是存放run方法的位置不一样
     */
    class MuiltyDemo implements Runnable //extends Thread
    {
        private int tickets=100;
        @Override
        public void run() {
            while(true){
                synchronized (this) {
                    if (tickets > 0) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("orangeTic", Thread.currentThread().getName() + ":" + tickets--);
                    }
                }
            }

        }
    }

    /**
     * 当有多个线程的对统一资源进行访问的时候,需要枷锁
     */
    class MuiltyDemo2 implements Runnable //extends Thread
    {
        private int tickets=100;
        @Override
        public void run() {
            while(true){
                synchronized (this) {
                    if (tickets > 0) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("orangeTic", Thread.currentThread().getName() + ":" + tickets--);
                    }
                }
            }

        }
    }
}
