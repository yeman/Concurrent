package com.yjt.concurrent.multithread.day03;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TestPrintString {

    //测试普通类设置通过设置属性改变运行状态
    @Test
    public void test01(){
        PrintStringService printStringService = new PrintStringService();
        printStringService.printString();
        System.out.println("停止,线程"+Thread.currentThread().getName());
        printStringService.setContinuePrint(false);

    }

    @Test
    public void test02() throws InterruptedException {
        PrintString printString = new PrintString();
        Thread thread =  new Thread(printString,"A");
        thread.start();
        System.out.println("thread setContinuePrint = false to stop loop");
        printString.setContinuePrint(false);
        TimeUnit.SECONDS.sleep(50);
    }

}
