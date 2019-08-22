package com.yjt.concurrent.multithread.day03;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TestDeadLock {

    public static void main(String[] args) {

        try {
            DeadLockThread thread = new DeadLockThread();
            thread.setUserName("a");
            Thread threadA = new Thread(thread,"ThreadA");
            threadA.start();
            Thread.sleep(100);
            System.out.println("--------------");
            thread.setUserName("b");
            Thread threadB = new Thread(thread,"ThreadB");
            threadB.start();
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
