package com.yjt.concurrent.multithread.day02;

import java.time.Instant;

public class ThreadLongBlockTask extends Thread {
    private LongTimeTaskService service;

    ThreadLongBlockTask(LongTimeTaskService service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        TimeUtils.beginTime1 = Instant.now();
        service.doCalLockBlock();
        TimeUtils.endTime1 = Instant.now();
    }
}
