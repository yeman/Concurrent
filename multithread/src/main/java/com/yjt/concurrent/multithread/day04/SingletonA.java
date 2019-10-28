package com.yjt.concurrent.multithread.day04;

import java.util.concurrent.TimeUnit;

//懒汉模式
public class SingletonA {
    private static SingletonA singletonA;

    private SingletonA(){

    }
    public static SingletonA getInstance(){
        //try {
            if(singletonA!=null){

            }else{
                //模拟延时操作
                //TimeUnit.SECONDS.sleep(1);
                singletonA = new SingletonA();
            }
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        return singletonA;
    }
}
