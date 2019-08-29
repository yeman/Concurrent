package com.yjt.concurrent.multithread.day03;

public class PrintStringService {
    private boolean isContinuePrint = true;

    public boolean isContinuePrint() {
        return isContinuePrint;
    }

    public void setContinuePrint(boolean continuePrint) {
        isContinuePrint = continuePrint;
    }

    public void printString(){
        try {
            while (isContinuePrint==true){
                System.out.println(" run printString method threadNanme="+Thread.currentThread().getName());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
