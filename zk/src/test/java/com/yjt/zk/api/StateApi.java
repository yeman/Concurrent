package com.yjt.zk.api;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @className StateApi
 * @description TODO
 * @author YM
 * @date 2020-07-13 14:37
 * @version V1.0
 * @since 1.0
 **/
@Slf4j
public class StateApi implements Watcher {

    private String zkServers;

    private int sessionTimeout;

    private ZooKeeper zooKeeper;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private void init() {
        zkServers = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        sessionTimeout = 20000;
    }

    private void disConnection() {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void createConnection() {
        this.disConnection();
        try {
            zooKeeper = new ZooKeeper(zkServers, sessionTimeout, this);
            log.info("开始创建连接");
            countDownLatch.wait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean createNode(String path, byte[] data) {
        try {
            zooKeeper.exists(path, true);
            zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Stat writeData(@NonNull String path, @NonNull String data) {
        try {
            return zooKeeper.setData(path, data.getBytes(), Version.REVISION);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String readData(@NonNull String path, boolean needWatch) {
        try {
            return new String(zooKeeper.getData(path, needWatch, null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteNode(String path) {
        try {
            zooKeeper.delete(path, Version.REVISION);
            log.info("删除节点{},成功!", path);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除节点{},失败!", path);
        }
    }

    public Stat exists(String path, boolean watch) {
        try {
            return zooKeeper.exists(path, watch);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void process(WatchedEvent event) {
        log.info("---------- enter process -------------");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(event==null){
            return;
        }
        Event.KeeperState keeperState = event.getState();
        event.getType()
    }
}
