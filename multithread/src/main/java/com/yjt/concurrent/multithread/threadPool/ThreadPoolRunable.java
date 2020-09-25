package com.yjt.concurrent.multithread.threadPool;

import java.util.concurrent.TimeUnit;

/**
 * @className ThreadPool
 * @description TODO
 * @author YM
 * @date 2020-09-18 16:26
 * @version V1.0
 * @since 1.0
 **/
public class ThreadPoolRunable implements Runnable {

    private Integer index;

    public ThreadPoolRunable(Integer index) {
        this.index = index;
    }

    @Override
    public void run() {
        System.out.println("开始线程处理!");
        try {
            TimeUnit.SECONDS.sleep(index);
            System.out.println("线程 id="+this.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
