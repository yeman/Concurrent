package com.yjt.concurrent.multithread.threadPool;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @className ThreadPoolExecutor
 * @description TODO
 * @author YM
 * @date 2020-09-18 17:16
 * @version V1.0
 * @since 1.0
 **/
public class ThreadPoolExecutor {


    /**
     * @description 可以有无限大的线程数进来（线程地址不一样），但需要注意机器的性能，需要线程太多，会导致服务器出现问题
     * @author YM
     * @date 2020/9/18 17:42
     * @param
     * @return void
     */
    @Test
    public void testCacheThreadPool() throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executor.submit(new ThreadPoolRunable(i));
        }
        TimeUnit.SECONDS.sleep(10);
    }


    /**
     * @description 每次只有两个线程在处理，当第一个线程执行完毕后，新的线程进来开始处理（线程地址不一样)
     * @author YM
     * @date 2020/9/18 17:42
     * @param
     * @return void
     */
    @Test
    public void testFixedThreadPool() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 5; i++) {
            executor.submit(new ThreadPoolRunable(i));
        }
        TimeUnit.SECONDS.sleep(10);
    }


    @Test
    public void testScheduledThreadPool() throws InterruptedException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        for (int i = 0; i < 5; i++) {
            executor.schedule(new ThreadPoolRunable(i),3,TimeUnit.SECONDS);
        }
        TimeUnit.SECONDS.sleep(10);
    }


}
