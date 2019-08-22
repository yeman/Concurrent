package com.yjt.concurrent.multithread.day03;

public class DeadLockThread implements Runnable {
    private String userName;
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void run() {
        if (userName.equals("a")){
            synchronized (lock1){
                try {
                    System.out.println(Thread.currentThread().getName()+" userName="+userName);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (lock2){
                System.out.println(Thread.currentThread().getName() + " 按照 lock1 -> lock2 执行");
            }
        }
        if (userName.equals("b")){
            synchronized (lock2){
                try {
                    System.out.println(Thread.currentThread().getName()+" userName="+userName);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (lock1){
                System.out.println(Thread.currentThread().getName()+" 按照 lock2 -> lock1 执行");
            }
        }
    }
}
