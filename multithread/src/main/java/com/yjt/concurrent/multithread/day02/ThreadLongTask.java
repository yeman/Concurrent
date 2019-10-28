package com.yjt.concurrent.multithread.day02;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class ThreadLongTask extends Thread {
    private LongTimeTaskService service;

    ThreadLongTask(LongTimeTaskService service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        TimeUtils.beginTime1 = Instant.now();
        service.doCal();
        TimeUtils.endTime1 = Instant.now();
    }
}
