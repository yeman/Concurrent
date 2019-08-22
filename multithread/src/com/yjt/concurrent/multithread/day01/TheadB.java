package com.yjt.concurrent.multithread.day01;

public class TheadB extends Thread {
    private P1 p1;

    public TheadB(P1 p1) {
        this.p1 = p1;
    }

    @Override
    public void run() {
        super.run();
        p1.login("b");
    }
}
