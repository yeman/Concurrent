package com.yjt.concurrent.multithread.day02;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TestPrintStream {

    //字符串常量池缓存,所以用字符串常量锁,同一个字符串常量会导致是同一把锁,一般改成对象锁
    @Test
    public void testFinalString() throws InterruptedException {
        PrintStringService service = new PrintStringService();
        PrintStringThreadA threadA = new PrintStringThreadA(service);
        threadA.setName("threadA");
        PrintStringThreadB threadB = new PrintStringThreadB(service);
        threadB.setName("threadB");
        threadA.start();
        threadB.start();
        TimeUnit.SECONDS.sleep(5);
    }
}
