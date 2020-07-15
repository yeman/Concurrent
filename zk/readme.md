### zookeeper

#### 1 api描述

| 命令             | 说明                     | 样例                                                         |
| ---------------- | ------------------------ | ------------------------------------------------------------ |
| ZkClient         | 构造器                   | new ZkClient(zkServers, sessionTimeout, connectionTimeout, zkSerializer) |
| create           | 创建节点zkClient         | client.create("/test01", "123456Az", CreateMode.EPHEMERAL);  |
| create           | ZooKeeper创建节点包含ACL | zooKeeper.create("/test02/server1", "192.168.0.1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL); |
| createPersistent | 创建持久化节点           | client.createPersistent("/test01/myid01", true);             |
| getData          | ZooKeeper                | zooKeeper.getData("/test02/server5", new Watcher() {}        |
| getChildren      | zkClient                 | client.getChildren("/test01")                                |
| deleteRecursive  | zkClient                 | client.deleteRecursive("/test01");                           |
| createPersistent | zkClient                 | client.createPersistent("/test03");                          |
| addAuthInfo      | ZooKeeper                | zooKeeper.addAuthInfo("digest", "admin:root".getBytes());    |



#### 2 acl访问控制列表

使用zkCli时，ACL的格式由<schema>:<id>:<acl>三段组成。

- schema：可以取下列值：world, auth, digest, host/ip
         **world：**默认方式，相当于全部都能访问
     　**auth**：代表已经认证通过的用户(cli中可以通过addauth digest user:pwd 来添加当前上下文中的授权用户)
      **digest**：即用户名:密码这种方式认证，这也是业务系统中最常用的。用 *username:password* 字符串来产生一个MD5串，然后该串被用来作为ACL ID。认证是通过明文发送*username:password* 来进行的，当用在ACL时，表达式为*username:base64* ，base64是password的SHA1摘要的编码。
      **ip**：使用客户端的主机IP作为ACL ID 。这个ACL表达式的格式为*addr/bits* ，此时addr中的有效位与客户端addr中的有效位进行比对。
  
- id： 标识身份，值依赖于schema做解析。

      **IP** IP地址或ip段 192.168.0.100 或者 192.168.0.1/254

      **digest** 自定义通常是 `username:Base64(SHA-1(username:password))` 例: `foo:kWn6aNSbjcKWPqjiV7cg0N24aU=`

      **Word**  只有anyone

     **Super** 与digest一致

- acl：就是权限：cdwra分别表示create, delete,write,read, admin

#### 3 watcher之state

##### 3.1 客户端状态类型:

| 状态              | Deprecated | 说明     |
| ----------------- | ---------- | -------- |
| Unknown           | Y          |          |
| Disconnected      | N          | 连接失败 |
| NoSyncConnected   | Y          |          |
| SyncConnected     | N          | 连接成功 |
| AuthFailed        | N          | 认证失败 |
| ConnectedReadOnly | N          |          |
| SaslAuthenticated | N          |          |
| Expired           | N          | 会话过期 |
| Closed            | N          | 连接关闭 |

##### 3.2 事件类型

| 事件类型(znode节点)  |                |
| -------------------- | -------------- |
| NodeCreated          | 节点创建       |
| NodeDataChanged      | 节点数据变更   |
| NodeChildrentChanged | 子节点数据变更 |
| NodeDeleted          | 节点删除       |
|                      |                |



#### 4 节点state

每个节点除了存储数据内容之外，还存储了数据节点本身的一些状态信息，通过get命令可以获得状态信息的详细内容

| 状态属性       | 说明                                                         |
| :------------- | :----------------------------------------------------------- |
| czxid          | 即Created ZXID，表示该数据节点被创建时的事务ID               |
| mzxid          | 即Modified ZXID，表示该节点最后一次被更新时的事务ID          |
| ctime          | 即Created Time，表示节点被创建的时间                         |
| mtime          | 即Modified Time，表示该节点最后一次被更新的时间              |
| version        | 数据节点的版本号                                             |
| cversion       | 子结点的版本号                                               |
| aversion       | 节点的ACL版本号                                              |
| ephemeralOwner | 创建该临时节点的会话的sessionID。如果该节点是持久节点，那么这个属性值为0 |
| dataLength     | 数据内容的长度                                               |
| numChildren    | 当前节点的子节点个数                                         |
| pzxid          | 表示该节点的子节点里欸包最后一次被修改时的事务ID。注意，只有子节点列表变更了才会变更pzxid，子节点内容变更不会影响pzxid |





