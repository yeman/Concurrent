package com.yjt.concurrent.multithread.day01;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TestSyncCalPerformace {

    //多线程不加锁性能和同步性问题
    @Test
    public void syncalIncTest() throws InterruptedException {
        final SyncCal cal = new SyncCal();
        Thread[] threads = new Thread[5];
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
        Thread[] threads = new Thread[5];
        for (int i=0;i<threads.length;i++) {
            threads[i] = new Thread(()->{
                cal.incsync();
            },(i+1)+"");
        }
        Arrays.stream(threads).forEach(Thread::start);
        TimeUnit.SECONDS.sleep(1);
    }
}
