package com.yjt.concurrent.multithread.day01;

import java.time.Duration;
import java.time.Instant;

/**
 * @ClassName Cal
 * @Description
 * @Author: YM
 * @Version V1.0
 * @Since V1.0
 * @Date: 2019-08-12 16:21
 **/
public class SyncCal {
    private long total = 0;

    public void inc() {
        Instant start = Instant.now();
        for (int i = 0; i < 500; i++) {
            total += 1;
            System.out.println("线程" + Thread.currentThread().getName() + " i=" + i);
        }
        System.out.println("线程"+Thread.currentThread().getName()+"共计"  + " total=" + total);
        System.out.println("线程"+Thread.currentThread().getName() + " inc耗时" + Duration.between(start, Instant.now()).toMillis());

    }

    synchronized public void incsync() {
        Instant start = Instant.now();
        for (int i = 0; i < 500; i++) {
            total += 1;
            System.out.println("线程" + Thread.currentThread().getName() + " i=" + i);
        }
        System.out.println("线程" + Thread.currentThread().getName() + " 共计" + " total=" + total);
        System.out.println("线程" + Thread.currentThread().getName() + " incsync耗时" + Duration.between(start, Instant.now()).toMillis());
    }
}


