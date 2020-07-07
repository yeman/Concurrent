package com.yjt.zk.api;

import cn.hutool.json.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.acl.Acl;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @className SessionApi
 * @description TODO
 * @author YM
 * @date 2020-07-05 22:26
 * @version V1.0
 * @since 1.0
 **/
@Log4j2
public class SessionApi {


    private String zkServers;

    private int sessionTimeout;

    private int connectionTimeout;

    private ZkSerializer zkSerializer;

    private ZkClient client;

    private ZooKeeper zooKeeper;
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    @Before
    public void init() throws IOException {
        zkServers = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        sessionTimeout = 10000;
        connectionTimeout = 10000;
        zkSerializer = new SerializableSerializer();
        client =new ZkClient(zkServers,sessionTimeout,connectionTimeout,zkSerializer);
        zooKeeper = new ZooKeeper(zkServers, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("process path {}, state {}",event.getPath(),event.getState());
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                }
            }
        });
    }

    @After
    public void dispose() {
        client.close();
        log.info("zk closed ");
    }

    @Test
    public void createNode() {
        client.create("/test01", "123456Az", CreateMode.PERSISTENT);
        log.debug("session get data {}", (String) client.readData("/test01"));
    }

    @Test
    public void createChildrenNode() {
        client.createPersistent("/test01/myid01", true);
        client.createPersistent("/test01/myid02", true);
        List<String> children = client.getChildren("/test01");
        log.debug("children data {}", children);
    }

    @Test
    public void exitsNode() {
        boolean exists = client.exists("/test01/myid01");
        if (exists) {
            log.info("exist node {}", exists);
            client.deleteRecursive("/test01");
            log.info("recursive delete successed");
        }
        //递归删除,会一直删除到指定路径的根
        List<String> children = client.getChildren("/test01");
        log.debug("children data {}", children);
    }

    @Test
    public void writeData() throws InterruptedException {
        client.create("/test01", "123456Az", CreateMode.EPHEMERAL);
        log.info("session get data {}", (String) client.readData("/test01"));
        String jsonStr = "{\n" +
                "\t\"userName\": \"admin\",\n" +
                "\t\"userpass\": \"123456Az\",\n" +
                "}";

        client.writeData("/test01", JSONObject.parseObject(jsonStr), 0);
        log.info("session update after json data {}", (JSONObject) client.readData("/test01"));
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void writeData2() throws InterruptedException {
        client.create("/test01", "123456Az", CreateMode.EPHEMERAL);
        log.info("session get data {}", (String) client.readData("/test01"));

        User user = User.builder().build().setUserName("jack").setUserPass("123456Az");
        client.writeData("/test01", user, 0);
        log.info("session update data {}", (User) client.readData("/test01"));
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void testNodeListener() throws InterruptedException {
        createChildrenNode();
        client.subscribeChildChanges("/test01", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                log.info("parent path {}", parentPath);
                log.info("current childs {}", currentChilds);
            }
        });
        createNode("/test01/myid03", "testdata", CreateMode.EPHEMERAL);
        TimeUnit.SECONDS.sleep(50);
    }

    public void createNode(String path, Object data, CreateMode createMode) {
        client.create(path, data, createMode);
    }

    @Test
    public void testNodeDataListener() throws InterruptedException {
        createNode("/test01/myid03", "123456", CreateMode.EPHEMERAL);
        client.subscribeDataChanges("/test01/myid03", new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                log.info("node path {} ,data changed to {}", dataPath, data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                log.info("node path {} ,data deleted", dataPath);
            }
        });
        client.writeData("/test01/myid03", "6666");
        TimeUnit.SECONDS.sleep(5);
        client.delete("/test01/myid03");
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void testAcl() throws KeeperException, InterruptedException {
        if(!client.exists("/test02")){
            client.createPersistent("/test02");
        }

        zooKeeper.addAuthInfo("digest","admin:root".getBytes());
        zooKeeper.create("/test02/server1","192.168.0.1".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        TimeUnit.SECONDS.sleep(5);
        List<ACL> aclList = zooKeeper.getACL("/test02/server1", null);
        log.info("acl list {}",aclList);

        byte[] data = zooKeeper.getData("/test02/server1", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("node path {},state {},event type{}",event.getPath(),event.getState(),event.getType());
            }
        },null);
        log.info("res data -> {}",new String(data));
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void testPermission() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/test02/server1", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("node path {},state {},event type{}",event.getPath(),event.getState(),event.getType());
            }
        },null);
        log.info("res data -> {}",new String(data));
    }
}
