package com.yjt.concurrent.multithread.atomic;

import cn.hutool.core.util.ArrayUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @className AtomicTest
 * @description TODO
 * @author YM
 * @date 2020-09-23 15:14
 * @version V1.0
 * @since 1.0
 **/
public class AtomicTest {


    /**
     * @description 属于原子操作,但存在ABA问题
     * @author YM
     * @date 2020/9/23 16:03
     * @param
     * @return void
     */
    @Test
    public void testAtomic() throws InterruptedException {
        int workCount = 5000;
        final AtomicCounter counter = new AtomicCounter();
        CountDownLatch countDownLatch = new CountDownLatch(workCount*2);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < workCount; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    counter.inc();
                    countDownLatch.countDown();
                }
            };
            executor.submit(runnable);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < workCount; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    counter.dec();
                    countDownLatch.countDown();
                }
            };
            executorService.submit(runnable);
        }
        // 关闭启动线程，执行未完成的任务
        executor.shutdown();
        executorService.shutdown();
        countDownLatch.await();
        System.out.println("counter 计数器结果 "+counter.get());
    }

    /**
     * @description ABA问题测试
     * @author YM
     * @date 2020/9/24 15:23
     * @param
     * @return void
     */
    @Test
    public void testABA() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(100);
        Thread a = new Thread(()->{
            atomicInteger.compareAndSet(100,200);
            atomicInteger.compareAndSet(200,100);
            System.out.println(Thread.currentThread().getName()+" 100 -> 200 -> 100");
        },"线程A");

        Thread b = new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName()+" 100 -> 500");
                atomicInteger.compareAndSet(100,500);
                System.out.println(Thread.currentThread().getName()+" 变更后的值:"+atomicInteger.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"线程B");
        a.start();
        b.start();
        a.join();
        b.join();
    }

    /**
     * @description ABA问题解决
     * @author YM
     * @date 2020/9/24 15:56
     * @param
     * @return void
     */
    @Test
    public void testABAResolve() throws InterruptedException {

        AtomicStampedReference<String> reference =  new AtomicStampedReference<String>("100",1);
        Thread a = new Thread(()->{
            System.out.println(Thread.currentThread().getName()+" 100 -> 200 -> 100");
            System.out.println(Thread.currentThread().getName()+" 初始版本号:"+reference.getStamp()+"值为:"+reference.getReference());
            reference.compareAndSet("100","200",reference.getStamp(),reference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+" 第二次版本号:"+reference.getStamp()+"值为:"+reference.getReference());
            reference.compareAndSet("200","100",reference.getStamp(),reference.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+" 第三次版本号:"+reference.getStamp()+"值为:"+reference.getReference());
        },"线程A");

        Thread b = new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread().getName()+" 当前版本号"+reference.getStamp()+"值为:"+reference.getReference());
                boolean success = reference.compareAndSet("100","500",1,reference.getStamp()+1);
                System.out.println(Thread.currentThread().getName()+" 是否修改成功"+success + "当前版本号"+reference.getStamp());
                //JDK1.9之前 weakCompareAndSet 与 compareAndSet 无差异
                success = reference.weakCompareAndSet("100","500",3,reference.getStamp()+1);
                System.out.println(Thread.currentThread().getName()+" 是否修改成功"+success + "当前版本号"+reference.getStamp());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"线程B");
        a.start();
        b.start();
        a.join();
        b.join();

    }
}


