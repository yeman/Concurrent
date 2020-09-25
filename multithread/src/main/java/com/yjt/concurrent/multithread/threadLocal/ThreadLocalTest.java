package com.yjt.concurrent.multithread.threadLocal;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @className ThreadLocalTest
 * @description TODO
 * @author YM
 * @date 2020-09-24 16:32
 * @version V1.0
 * @since 1.0
 **/
public class ThreadLocalTest {

    @Test
    public void testMultiThread() throws InterruptedException {
        Thread[] threads = new Thread[50];
        for (int i=0;i<threads.length;i++){
            final int finalI = i;
            threads[i] = new Thread(()-> {
                    LoginConstant.getLoginInfo().set("线程"+ finalI +"值为:"+finalI);
                    System.out.println(LoginConstant.getLoginInfo().get());
                },"线程"+i);
        }
        Arrays.stream(threads).forEach(Thread::start);
        TimeUnit.SECONDS.sleep(10);

    }
}
