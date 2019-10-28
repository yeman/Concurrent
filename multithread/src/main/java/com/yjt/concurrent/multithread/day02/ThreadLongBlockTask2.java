package com.yjt.concurrent.multithread.day02;

import java.time.Instant;

public class ThreadLongBlockTask2 extends Thread {
    private LongTimeTaskService service;

    ThreadLongBlockTask2(LongTimeTaskService service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        TimeUtils.beginTime2 = Instant.now();
        service.doCalLockBlock();
        TimeUtils.endTime2 = Instant.now();
    }
}
