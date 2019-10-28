package com.yjt.concurrent.multithread.day02;

import org.junit.Test;

public class ReEnterLockTest {

    //可重入锁:自己可以再次获取自己的内部锁
    @Test
    public void testEnterLock(){
        Service service = new Service();
        ServiceThread serviceThread = new ServiceThread(service);
        serviceThread.start();
    }
}
