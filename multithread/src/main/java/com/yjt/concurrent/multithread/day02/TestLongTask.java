package com.yjt.concurrent.multithread.day02;

import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TestLongTask {

    //synchronized 多个线程并发访问会造成性能问题 10001
    @Test
    public void testLongTask() throws InterruptedException {
        LongTimeTaskService service = new LongTimeTaskService();
        ThreadLongTask taskA = new ThreadLongTask(service);
        taskA.setName("taskA");
        ThreadLongTask2 taskB = new ThreadLongTask2(service);
        taskB.setName("taskB");
        taskA.start();
        taskB.start();
        TimeUnit.SECONDS.sleep(15);
        if (TimeUtils.beginTime2.isBefore(TimeUtils.beginTime1)){
            TimeUtils.beginTime1 = TimeUtils.beginTime2;
        }
        if (TimeUtils.endTime2.isAfter(TimeUtils.endTime1)){
            TimeUtils.endTime1 = TimeUtils.endTime2;
        }
        System.out.println("耗时:"+Duration.between(TimeUtils.beginTime1,TimeUtils.endTime1).toMillis());

    }


    //synchronized 代码块 和 synchronized 性能比较 synchronized性能更好 9991
    @Test
    public void testLongTask2() throws InterruptedException {
        LongTimeTaskService service = new LongTimeTaskService();
        ThreadLongBlockTask taskA = new ThreadLongBlockTask(service);
        taskA.setName("taskA");
        ThreadLongBlockTask2 taskB = new ThreadLongBlockTask2(service);
        taskB.setName("taskB");
        taskA.start();
        taskB.start();
        TimeUnit.SECONDS.sleep(15);
        System.out.println("耗时:"+Duration.between(TimeUtils.beginTime1,TimeUtils.endTime1).toMillis());
    }
}
