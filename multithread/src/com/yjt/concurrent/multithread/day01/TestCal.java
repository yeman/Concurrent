package com.yjt.concurrent.multithread.day01;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TestCal
 * @Description TODO
 * @Author: YM
 * @Version V1.0
 * @Since V1.0
 * @Date: 2019-08-12 16:25
 **/
public class TestCal {

    @Test
    public void mutilThead01() throws InterruptedException {
        final Cal cal = new Cal();
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                cal.inc();
            }
        }, "A");

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                cal.inc();
            }
        }, "B");
        threadA.start();
        threadB.start();
        TimeUnit.SECONDS.sleep(1);
    }
    //多线程方法内部变量是线程安全的
    @Test
    public void mutilThead02() throws InterruptedException {
        final Cal cal = new Cal();
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                cal.incp();
            }
        }, "A");

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                cal.incp();
            }
        }, "B");
        threadA.start();
        threadB.start();
        TimeUnit.SECONDS.sleep(1);
    }

    //多线程不加锁性能和同步性问题
    @Test
    public void syncalIncTest() throws InterruptedException {
        final SyncCal cal = new SyncCal();
        Thread[] threads = new Thread[3];
        for (int i=0;i<threads.length;i++) {
          threads[i] = new Thread(()->{
              cal.inc();
          },(i+1)+"");
        }
        Arrays.stream(threads).forEach(Thread::start);
        TimeUnit.SECONDS.sleep(1);
    }

    //多线程加锁性能和同步性
    @Test
    public void syncalincsyncTest() throws InterruptedException {
        SyncCal cal = new SyncCal();
        Thread[] threads = new Thread[3];
        for (int i=0;i<threads.length;i++) {
            threads[i] = new Thread(()->{
                cal.incsync();
            },(i+1)+"");
        }
        Arrays.stream(threads).forEach(Thread::start);
        TimeUnit.SECONDS.sleep(1);
    }
}
