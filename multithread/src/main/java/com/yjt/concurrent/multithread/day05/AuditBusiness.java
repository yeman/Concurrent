package com.yjt.concurrent.multithread.day05;

import cn.hutool.core.util.RandomUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @className AuditBusiness
 * @description TODO
 * @author YM
 * @date 2020-09-04 09:30
 * @version V1.0
 * @since 1.0
 **/
@Slf4j
public class AuditBusiness {
    private ReentrantLock reentrantLock = new ReentrantLock();

    public void validateReport() throws InterruptedException {
       log.info("{}校验报告格式",Thread.currentThread().getName());
        int times = RandomUtil.randomInt(4,10);
        TimeUnit.SECONDS.sleep(times);
        log.info("{}校验报告格式完毕",Thread.currentThread().getName());
    }

    public void dbOperation() throws InterruptedException {
        log.info("{}写库操作",Thread.currentThread().getName());
        int times = RandomUtil.randomInt(1,3);
        TimeUnit.SECONDS.sleep(times);
        log.info("{}写库操作--",Thread.currentThread().getName());
    }

    public void doAudit() throws InterruptedException {
        reentrantLock.lock();
        validateReport();
        dbOperation();
        log.info("{}审批执行完毕",Thread.currentThread().getName());
        reentrantLock.unlock();
    }


    @Test
    public void test01(){
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for(int i=0;i<4;i++)
        {
            executorService.submit(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    doAudit();
                }
            });
        }
        while(!executorService.isTerminated()){

        }


    }

    public void test02(){
        try {
            reentrantLock.lock();
            int i = 1/0;
        } catch (Exception e) {
           throw new RuntimeException("11");
        } finally {
            reentrantLock.unlock();
        }
    }

    @Test
    public void doCall(){
        test02();
    }
}
