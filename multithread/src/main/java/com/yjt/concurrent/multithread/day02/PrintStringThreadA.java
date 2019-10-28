package com.yjt.concurrent.multithread.day02;

public class PrintStringThreadA extends Thread {
    PrintStringService printStringService;

    public PrintStringThreadA(PrintStringService printStringService) {
        this.printStringService = printStringService;
    }

    @Override
    public void run() {
        super.run();
        printStringService.printString("AA");
    }
}
