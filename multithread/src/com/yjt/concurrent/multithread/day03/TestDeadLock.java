package com.yjt.concurrent.multithread.day03;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TestDeadLock {

    //通过 jps 和 jstack -l 线程id   发现有 Found 1 deadlock
    @Test
    public void test01() {

        try {
            DeadLockThread thread = new DeadLockThread();
            thread.setUserName("a");
            Thread threadA = new Thread(thread, "ThreadA");
            threadA.start();
            Thread.sleep(100);
            System.out.println("--------------");
            thread.setUserName("b");
            Thread threadB = new Thread(thread, "ThreadB");
            threadB.start();
            TimeUnit.SECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
