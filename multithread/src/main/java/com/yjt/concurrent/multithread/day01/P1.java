package com.yjt.concurrent.multithread.day01;

import java.util.concurrent.TimeUnit;

public class P1 {

    private int num;

    synchronized public void login(String userName) {

        try {
            if ("a".equalsIgnoreCase(userName)) {
                num = 100;
                System.out.println("a set over");
                TimeUnit.SECONDS.sleep(2);
            }else{
                num = 200;
                System.out.println("b set over");
            }
            System.out.println(Thread.currentThread().getName()+String.format(" userName=%s,num=%d",userName,num));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
     public void login2(String userName) {

        try {
            if ("a".equalsIgnoreCase(userName)) {
                num = 100;
                System.out.println("a set over");
                TimeUnit.SECONDS.sleep(1);
            }else{
                num = 200;
                System.out.println("b set over");
            }
            System.out.println(Thread.currentThread().getName()+String.format(" userName=%s,num=%d",userName,num));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
