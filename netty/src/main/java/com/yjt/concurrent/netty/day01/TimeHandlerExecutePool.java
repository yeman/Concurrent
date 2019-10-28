package com.yjt.concurrent.netty.day01;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TimeHandlerExecutePool
 * @Description TODO
 * @Author: YM
 * @Version V1.0
 * @Since V1.0
 * @Date: 2019-09-12 14:38
 **/
public class TimeHandlerExecutePool {

    private ExecutorService executor;

    public TimeHandlerExecutePool(int maxPoolSize,int queueSize){
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,120L, TimeUnit.SECONDS,new ArrayBlockingQueue(queueSize,true));
    }
    public void execute(Runnable task){
        executor.execute(task);
    }

}
