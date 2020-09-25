package com.yjt.concurrent.multithread.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @className AtomicCounter
 * @description TODO
 * @author YM
 * @date 2020-09-23 16:34
 * @version V1.0
 * @since 1.0
 **/
public class AtomicCounter {

    private AtomicInteger counter = new AtomicInteger(0);

    public void inc(){
        counter.incrementAndGet();
    }

    public void dec(){
        counter.decrementAndGet();
    }

    public int get(){
        return counter.get();
    }

}
