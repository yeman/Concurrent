package com.yjt.concurrent.multithread.day01;

import java.time.Duration;
import java.time.Instant;

public class SyncPerformance {

    public void cal(){
        Instant now = Instant.now();
        for (long i=1;i<1000000;i++){
        }
        System.out.println("线程"+Thread.currentThread().getName()+"耗时"+ Duration.between(now,Instant.now()).toMillis());
    }

    synchronized public void syncal(){
        Instant now = Instant.now();
        for (long i=1;i<1000000;i++){

        }
        System.out.println("线程"+Thread.currentThread().getName()+"耗时"+ Duration.between(now,Instant.now()).toMillis());
    }
}
