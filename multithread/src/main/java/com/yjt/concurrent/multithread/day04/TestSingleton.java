package com.yjt.concurrent.multithread.day04;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TestSingleton {

    //多线程线程安全测试 懒汉式
    @Test
    public void test01() throws InterruptedException {
        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                System.out.println("hashCode:"+SingletonA.getInstance().hashCode());
            });
        }
        Arrays.stream(threads).forEach(Thread::start);
        TimeUnit.SECONDS.sleep(10);
    }

    //多线程线程安全测试 饿汉式
    @Test
    public void test02() throws InterruptedException {
        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                System.out.println("hashCode:"+SingletonB.getInstance().hashCode());
            });
        }
        Arrays.stream(threads).forEach(Thread::start);
        TimeUnit.SECONDS.sleep(10);
    }
}
