package com.yjt.zk.api;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
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

    @After
    public void disConnection() {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Before
    public void createConnection() {
        this.disConnection();
        try {
            init();
            zooKeeper = new ZooKeeper(zkServers, sessionTimeout, this);
            log.info("开始创建连接");
            countDownLatch.await();
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

    private List<String> getChildren(String path, boolean watch) {
        try {
            return zooKeeper.getChildren(path,watch);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        Event.EventType eventType = event.getType();
        String path = event.getPath();
        if(Event.KeeperState.SyncConnected == keeperState){
            if(Event.EventType.None == eventType){
                log.info("成功连接 path={}",path);
                countDownLatch.countDown();
            }else if(Event.EventType.NodeCreated == eventType){
                log.info("创建节点 path={},状态{}",path,this.exists(path,true));
            }else if(Event.EventType.NodeDeleted == eventType){
                log.info("节点删除 path={},状态{}",path, this.exists(path,true));
            }else if(Event.EventType.NodeDataChanged == eventType){
                log.info("节点数据更新 path={},成功{}",path,readData(path,true));
            }else if(Event.EventType.NodeChildrenChanged == eventType){
                log.info("子节点改变 path={},成功{}",path,this.getChildren(path,true));
            }

        }else if(Event.KeeperState.Disconnected == keeperState){
            log.info("断开连接 path={}",path);
        }else if(Event.KeeperState.AuthFailed == keeperState){
            log.info("认证失败 path={}",path);
        }else if(Event.KeeperState.Expired == keeperState){
            log.info("会话失效 path={}",path);
        }

    }

    @Test
    public void testState() throws InterruptedException {
        createNode("/state01",null);
        TimeUnit.SECONDS.sleep(2);
        createNode("/state01/node1",null);
        TimeUnit.SECONDS.sleep(2);
        writeData("/state01/node1","123456");
        TimeUnit.SECONDS.sleep(2);
        List<String> childs = getChildren("/state01",true);
        log.info("children {}",childs);
        TimeUnit.SECONDS.sleep(2);
        boolean flag = createNode("/state01/node2","678910".getBytes());
        log.info("createNode {}",flag);
        TimeUnit.SECONDS.sleep(2);
        deleteNode("/state01/node1");
        TimeUnit.SECONDS.sleep(2);

    }


}