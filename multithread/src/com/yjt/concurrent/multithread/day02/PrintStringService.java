package com.yjt.concurrent.multithread.day02;

import java.util.concurrent.TimeUnit;

public class PrintStringService {

    public static void printString(String printStream) {
        try {
            synchronized (printStream) {
                while (true) {
                    System.out.println("线程:" + Thread.currentThread().getName());
                    TimeUnit.SECONDS.sleep(1);

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
