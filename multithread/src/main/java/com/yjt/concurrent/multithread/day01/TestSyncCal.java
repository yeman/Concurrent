package com.yjt.concurrent.multithread.day01;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TestSyncCal {

    //锁性能测试
    @Test
    public void  testPerformance() throws InterruptedException {
        SyncPerformance syncPerformance = new SyncPerformance();
        Thread[] threads = new Thread[6];
        for (int i = 0; i < 6; i++) {
            threads[i] = new Thread(()->{
                syncPerformance.cal();
            },("i"+i));
        }
        Arrays.stream(threads).forEach(Thread::start);

        Thread[] syncthreads = new Thread[6];
        for (int j = 0; j < 6; j++) {
            syncthreads[j] = new Thread(()->{
                syncPerformance.syncal();
            },"j"+j);
        }
        Arrays.stream(syncthreads).forEach(Thread::start);
        TimeUnit.SECONDS.sleep(10);
    }
}
