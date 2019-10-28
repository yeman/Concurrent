package com.yjt.concurrent.multithread.day02;

public class Service{
   synchronized public void  service1(){
        System.out.println("enter service1");
       service2();
    }
    synchronized public void  service2(){
        System.out.println("enter service2");
        service3();
    }
    synchronized public void  service3(){
        System.out.println("enter service3");
    }
}
