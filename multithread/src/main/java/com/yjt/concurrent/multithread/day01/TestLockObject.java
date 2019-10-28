package com.yjt.concurrent.multithread.day01;

import com.yjt.concurrent.multithread.day01.P1;
import com.yjt.concurrent.multithread.day01.TheadA;
import com.yjt.concurrent.multithread.day01.TheadB;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TestLockObject {

    //单个对象多个线程 结果同步运行
    @Test
    public void singleObjectMutilThread() throws InterruptedException {
        P1 p = new P1();
        Thread a = new Thread(() -> {
            p.login("a");
        }, "threadA");
        Thread b = new Thread(() -> {
            p.login("b");
        }, "threadB");
        b.start();
        a.start();
        TimeUnit.SECONDS.sleep(5);
    }


    //线程A,线程B 分别同时访问实例对象p1和p2 的同步方法,结果异步
    @Test
    public void mutilObjectMutilThread() throws InterruptedException {
        P1 p1 = new P1();
        P1 p2 = new P1();
        Thread a = new Thread(() -> {
            p1.login("a");
        });
        Thread b = new Thread(() -> {
            p2.login("b");
        });
        a.start();
        b.start();

        TimeUnit.SECONDS.sleep(5);
    }

    //多个线程访问分别同时访问对象p1,p2 访问同步方法,结果异步
    @Test
    public void mutilObjectMutilThread2() throws InterruptedException {
        P1 p1 = new P1();
        P1 p2 = new P1();
        Thread[] threadsA = new Thread[6];
        Thread[] threadsB = new Thread[6];
        for (int i = 0; i < threadsA.length; i++) {
            threadsA[i] = new Thread(() -> {
                p1.login("a");
            }, "A" + i);
        }
        for (int i = 0; i < threadsB.length; i++) {
            threadsB[i] = new Thread(() -> {
                p2.login("b");
            }, "B" + i);
        }
        Arrays.stream(threadsA).forEach(Thread::start);
        Arrays.stream(threadsB).forEach(Thread::start);

        TimeUnit.SECONDS.sleep(10);
    }

    //多个线程访问分别同时访问对象p1,p2 访问非同步方法,结果异步
    @Test
    public void mutilObjectMutilThread3() throws InterruptedException {
        P1 p1 = new P1();
        P1 p2 = new P1();
        Thread[] threadsA = new Thread[6];
        Thread[] threadsB = new Thread[6];
        for (int i = 0; i < threadsA.length; i++) {
            threadsA[i] = new Thread(() -> {
                p1.login2("a");
            }, "A" + i);
        }
        for (int i = 0; i < threadsB.length; i++) {
            threadsB[i] = new Thread(() -> {
                p2.login2("b");
            }, "B" + i);
        }
        Arrays.stream(threadsB).forEach(Thread::start);
        TimeUnit.SECONDS.sleep(1);
        Arrays.stream(threadsA).forEach(Thread::start);


        TimeUnit.SECONDS.sleep(10);
    }

}
