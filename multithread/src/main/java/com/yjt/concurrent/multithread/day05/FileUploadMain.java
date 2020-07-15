package com.yjt.concurrent.multithread.day05;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @className FileUploadMain
 * @description TODO
 * @author YM
 * @date 2020-07-15 16:57
 * @version V1.0
 * @since 1.0
 **/
@Log4j2
public class FileUploadMain {

    private  CountDownLatch count = new CountDownLatch(10);

    @Test
    public void doUpload() throws InterruptedException {
        FileUploadThread[] threads = new FileUploadThread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new FileUploadThread(i+"");
        }
        Arrays.asList(threads).stream().forEach(FileUploadThread::run);
      /* while(count.getCount()>1){
           log.info("主线程{}等待",Thread.currentThread().getId());
            count.await();
        }*/
        log.info("所有线程执行完毕");
    }

     class FileUploadThread extends Thread {

         public FileUploadThread(String name) {
             super(name);
         }

         @Override
        public void run() {
            log.info("Thread {},开始文件上传", this.getName());
            Random r = new Random();
            int seconds = r.nextInt(1);
            try {
                log.info("Thread {},开始休眠{}s", this.getName(),seconds);
                TimeUnit.SECONDS.sleep(seconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Thread {},文件上完毕", this.getName());

        }
    }
}
