package com.yjt.concurrent.multithread.day05;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;

/**
 * @className FileUploadMain
 * @description TODO
 * @author YM
 * @date 2020-07-15 16:57
 * @version V1.0
 * @since 1.0
 **/
@Slf4j
public class FileUploadMain {

    @Test
    public void testUpload() throws InterruptedException, ExecutionException {
        int threads = 10;
        ExecutorService threadPool = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(threads);
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        for (int i = 0; i < threads; i++) {
            Future<ConcurrentHashMap> future = threadPool.submit(new DoUpload(i, countDownLatch));
            concurrentHashMap.putAll(future.get());
        }
        countDownLatch.await();
        log.info("map:{}", concurrentHashMap);
    }

    class DoUpload implements Callable<ConcurrentHashMap> {
        private int index;
        private CountDownLatch countDownLatch;

        public DoUpload(int index, CountDownLatch countDownLatch) {
            this.index = index;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public ConcurrentHashMap call() {
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            log.info("线程{}开始上传编号{}的文件", Thread.currentThread().getName(), index);
            try {
                TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("上传成功");
            concurrentHashMap.put(index, "a");
            countDownLatch.countDown();
            return concurrentHashMap;
        }
    }

    @Test
    public void testDir() throws IOException {
        Path newDirectory = FileSystems.getDefault().getPath("\\opt\\la-api\\kbao-la-api\\temp\\\\1267372633603207172");
        Path path = Files.createDirectories(newDirectory);
        log.info("{}创建成功", path);
    }
}
