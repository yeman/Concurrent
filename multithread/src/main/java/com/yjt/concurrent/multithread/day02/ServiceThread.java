package com.yjt.concurrent.multithread.day02;

public class ServiceThread extends Thread{
    private Service service;

    public ServiceThread(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        super.run();
        service.service1();
    }
}
