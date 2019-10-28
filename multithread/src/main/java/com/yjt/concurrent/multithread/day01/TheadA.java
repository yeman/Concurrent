package com.yjt.concurrent.multithread.day01;

public class TheadA extends Thread {
    private P1 p1;

    public TheadA(P1 p1) {
        this.p1 = p1;
    }

    @Override
    public void run() {
        super.run();
        p1.login("a");
    }
}
