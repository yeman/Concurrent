package com.yjt.zk.api;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class SessionApi implements Watcher {


    private String zkServers;

    private int sessionTimeout;

    private int connectionTimeout;

    private ZkSerializer zkSerializer;

    private ZkClient client;

    private ZooKeeper zooKeeper;

    @Override
    public void process(WatchedEvent event) {
        log.info("process path {}, state {}", event.getPath(), event.getState());
    }

    @Before
    public void init() throws IOException, InterruptedException {
        zkServers = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        sessionTimeout = 20000;
        connectionTimeout = 30000;
        zkSerializer = new SerializableSerializer();
        client = new ZkClient(zkServers, sessionTimeout, connectionTimeout, zkSerializer);
        zooKeeper = new ZooKeeper(zkServers, sessionTimeout, this);
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

    /**
     * @description word anyone 方式
     * @author YM
     * @date 2020/7/8 8:38
     * @param
     * @return void
     */
    @Test
    public void testAcl() throws KeeperException, InterruptedException {
        if (!client.exists("/test02")) {
            client.createPersistent("/test02");
        }
        zooKeeper.create("/test02/server1", "192.168.0.1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        TimeUnit.SECONDS.sleep(5);
        List<ACL> aclList = zooKeeper.getACL("/test02/server1", null);
        log.info("acl list {}", aclList);
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * @description ip方式
     * @author YM
     * @date 2020/7/8 8:39
     * @param
     * @return void
     */
    @Test
    public void testAclIp() throws KeeperException, InterruptedException {
        if (!client.exists("/test02")) {
            client.createPersistent("/test02");
        }
        ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("ip", "10.78.210.123"));
        ACL acl2 = new ACL(ZooDefs.Perms.ALL, new Id("ip", "127.0.0.1"));
        String res = zooKeeper.create("/test02/server3", "192.168.0.1".getBytes(), Arrays.asList(acl, acl2), CreateMode.PERSISTENT);
        if(StringUtils.isNotBlank(res)){
            log.info("节点{}创建成功!",res);
        }
        TimeUnit.SECONDS.sleep(2);
        List<ACL> aclList = zooKeeper.getACL("/test02/server3", null);
        log.info("acl list {}", aclList);
        // NoAuth for /test02/server3
        //zooKeeper.addAuthInfo("ip","10.78.210.123".getBytes());
        byte[] data = zooKeeper.getData("/test02/server3", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("node path {},state {},event type{}", event.getPath(), event.getState(), event.getType());
            }
        }, null);
        log.info("res data -> {}", new String(data));
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * @description 明文Auth
     * @author YM
     * @date 2020/7/8 8:39
     * @param
     * @return void
     */
    @Test
    public void testAclAuth() throws KeeperException, InterruptedException {
        zooKeeper.addAuthInfo("digest","admin:root".getBytes());
        zooKeeper.create("/test02/server5", "明文".getBytes(),  ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        TimeUnit.SECONDS.sleep(2);
        // NoAuth
        byte[] data = zooKeeper.getData("/test02/server5", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("node path {},state {},event type{}", event.getPath(), event.getState(), event.getType());
            }
        }, null);
        log.info(">> data {}",new String(data));
        TimeUnit.SECONDS.sleep(2);
    }


    @Test
    public void testAclAuthVisit() throws KeeperException, InterruptedException {
        zooKeeper.addAuthInfo("digest", "admin:root".getBytes());
        byte[] data = zooKeeper.getData("/test02/server5", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("node path {},state {},event type{}", event.getPath(), event.getState(), event.getType());
            }
        }, null);
        log.info("res data -> {}", new String(data));
        TimeUnit.SECONDS.sleep(5);
    }

     public String digest(String idPassword) throws NoSuchAlgorithmException {
        return DigestAuthenticationProvider.generateDigest(idPassword);
     }

     @Test
    public void testAclDigest() throws KeeperException, InterruptedException, NoSuchAlgorithmException {
         if (!client.exists("/test03")) {
             client.createPersistent("/test03");
         }
        List<ACL> aclList = new ArrayList<ACL>();
        Id admin= new Id("digest", digest("admin:root"));
        Id user = new Id("digest", digest("user:user"));
        Id owner = new Id("digest", digest("zoo:user"));
        //管理员
        aclList.add(new ACL(ZooDefs.Perms.ALL,admin));
        //只读
        aclList.add(new ACL(ZooDefs.Perms.READ,user));
        //读写删
        aclList.add(new ACL(ZooDefs.Perms.READ| ZooDefs.Perms.WRITE| ZooDefs.Perms.DELETE,owner));
        zooKeeper.create("/test03/server1", "测试数据".getBytes(), aclList, CreateMode.PERSISTENT);
        TimeUnit.SECONDS.sleep(2);
        // TODO NoAuth for /test03/server1
        byte[] data = zooKeeper.getData("/test03/server1", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("node path {},state {},event type{}", event.getPath(), event.getState(), event.getType());
            }
        }, null);
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void testAclDigestVisit() throws KeeperException, InterruptedException {
        zooKeeper.addAuthInfo("digest","zoo:user".getBytes());
        byte[] data = zooKeeper.getData("/test03/server1", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("node path {},state {},event type{}", event.getPath(), event.getState(), event.getType());
            }
        }, null);
        TimeUnit.SECONDS.sleep(2);
        log.info(">>> data {}",new String(data));
    }


}
