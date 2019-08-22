package com.yjt.concurrent.multithread.day02;

import java.time.Duration;
import java.time.Instant;

public class ThreadLongTask2 extends Thread {
    private LongTimeTaskService service;

    ThreadLongTask2(LongTimeTaskService service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        TimeUtils.beginTime2 = Instant.now();
        service.doCal();
        TimeUtils.endTime2 = Instant.now();
    }
}
