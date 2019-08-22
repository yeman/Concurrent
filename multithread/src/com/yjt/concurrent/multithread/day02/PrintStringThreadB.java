package com.yjt.concurrent.multithread.day02;

public class PrintStringThreadB extends Thread {
    PrintStringService printStringService;

    public PrintStringThreadB(PrintStringService printStringService) {
        this.printStringService = printStringService;
    }

    @Override
    public void run() {
        super.run();
        printStringService.printString("AA");
    }
}
