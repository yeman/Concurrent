package com.yjt.concurrent.multithread.day01;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TestLockObject02 {

    //多线程访问同一个锁对象同步方法 结果同步
    @Test
    public void singleObjectMutilThread() throws InterruptedException {
        P1 p = new P1();
        TheadA a = new TheadA(p);
        TheadB b = new TheadB(p);
        a.start();
        b.start();
        TimeUnit.SECONDS.sleep(5);
    }

    //线程A,B分别访问对象pa,pb 同步方法 结果异步
    @Test
    public void mutilObjectMutilThread() throws InterruptedException {
        P1 pa = new P1();
        P1 pb = new P1();
        TheadA a = new TheadA(pa);
        TheadB b = new TheadB(pb);
        a.start();
        b.start();
        TimeUnit.SECONDS.sleep(5);
    }
}
