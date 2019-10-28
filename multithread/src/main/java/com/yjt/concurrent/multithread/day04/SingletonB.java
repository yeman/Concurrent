package com.yjt.concurrent.multithread.day04;

//饿汉模式
public class SingletonB {
    private static SingletonB singletonA = new SingletonB();

    private SingletonB(){

    }
    public static SingletonB getInstance(){
        return singletonA;
    }
}
