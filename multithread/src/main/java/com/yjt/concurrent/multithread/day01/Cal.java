package com.yjt.concurrent.multithread.day01;

import java.time.Instant;

/**
 * @ClassName Cal
 * @Description 多线程全局变量和方法局部变量问题
 * @Author: YM
 * @Version V1.0
 * @Since V1.0
 * @Date: 2019-08-12 16:21
 **/
public class Cal {
    private long total = 0;

    public void inc(){
        for (int i=0;i<500;i++){
            total+=1;
            System.out.println("线程"+Thread.currentThread().getName()+" i="+i);
        }
        System.out.println("共计"+Thread.currentThread().getName()+" total="+total);
    }

    public void incp(){
        long ptotal = 0;
        for (int i=0;i<500;i++){
            ptotal+=1;
            System.out.println("线程"+Thread.currentThread().getName()+" i="+i);
        }
        System.out.println("共计"+Thread.currentThread().getName()+" ptotal="+ptotal);
    }
}
