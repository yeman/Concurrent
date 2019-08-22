package com.yjt.concurrent.multithread.day02;

import java.util.concurrent.TimeUnit;

public class LongTimeTaskService {
    private String data1;
    private String data2;
    synchronized public void doCal() {
        try {
            System.out.println("begin task "+Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getName() + "开始计算");
            TimeUnit.SECONDS.sleep(5);
            data1 = "长时间任务获取返回结果1"+Thread.currentThread().getName();
            data2 = "长时间任务获取返回结果2"+Thread.currentThread().getName();
            System.out.println(data1);
            System.out.println(data2);
            System.out.println("end task "+Thread.currentThread().getName());
            ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doCalLockBlock() {
        try {
            synchronized (this) {
                System.out.println("begin task "+Thread.currentThread().getName());
                System.out.println(Thread.currentThread().getName() + "开始计算");
                TimeUnit.SECONDS.sleep(5);
                data1 = "长时间任务获取返回结果1"+Thread.currentThread().getName();
                data2 = "长时间任务获取返回结果2"+Thread.currentThread().getName();
                System.out.println(data1);
                System.out.println(data2);
                System.out.println("end task "+Thread.currentThread().getName());
            }
            ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
