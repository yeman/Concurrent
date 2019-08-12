package com.yjt.concurrent.multithread.day01;

import org.junit.jupiter.api.Test;

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
        Thread.sleep(TimeUnit.SECONDS.toSeconds(1));
        threadB.start();

    }

    @Test
    public void mutilThead02() {
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

    }
    //多线程不加锁性能和同步性
    @Test
    public void syncalIncTest() {
        final SyncCal cal = new SyncCal();
        for(int i=0;i<6;i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    cal.inc();
                }
            }, (i+1)+"");
            thread.start();
        }
    }
    //多线程加锁性能和同步性
    @Test
    public void syncalincsyncTest() {
        final SyncCal cal = new SyncCal();
        for(int i=0;i<6;i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    cal.incsync();
                }
            }, (i+1)+"");
            thread.start();
        }
    }
}
