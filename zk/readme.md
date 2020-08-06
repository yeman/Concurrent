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



#### 5 序列化器Jute

同类型Hadoop采用apacheAvro



#### 6 zab协议原理

Zab协议要求每个Leader经历三个阶段: **发现**,**同步**,**广播**

**发现**：要求zookeeper集群必须选举出一个 Leader 进程，同时 Leader 会维护一个 Follower 可用客户端列表。将来客户端可以和这些 Follower节点进行通信。

**同步**：Leader 要负责将本身的数据与 Follower 完成同步，做到多副本存储。这样也是体现了CAP中的高可用和分区容错。Follower将队列中未处理完的请求消费完成后，写入本地事务日志中。

**广播**：Leader 可以接受客户端新的事务Proposal请求，将新的Proposal请求广播给所有的 Follower。



Zab协议核心
Zab协议的核心：定义了事务请求的处理方式

1）所有的事务请求必须由一个全局唯一的服务器来协调处理，这样的服务器被叫做 Leader服务器。其他剩余的服务器则是 Follower服务器。

2）Leader服务器 负责将一个客户端事务请求，转换成一个 事务Proposal，并将该 Proposal 分发给集群中所有的 Follower 服务器，也就是向所有 Follower 节点发送数据广播请求（或数据复制）

3）分发之后Leader服务器需要等待所有Follower服务器的反馈（Ack请求），在Zab协议中，只要超过半数的Follower服务器进行了正确的反馈后（也就是收到半数以上的Follower的Ack请求），那么 Leader 就会再次向所有的 Follower服务器发送 Commit 消息，要求其将上一个 事务proposal 进行提交

![img](https://upload-images.jianshu.io/upload_images/1053629-61bd17259d608fc8.png?imageMogr2/auto-orient/strip|imageView2/2/w/614/format/webp)



#### 7 保证消息有序

在整个消息广播中，Leader会将每一个事务请求转换成对应的 proposal 来进行广播，并且在广播 事务Proposal 之前，Leader服务器会首先为这个事务Proposal分配一个全局单递增的唯一ID，称之为事务ID（即zxid），由于Zab协议需要保证每一个消息的严格的顺序关系，因此必须将每一个proposal按照其zxid的先后顺序进行排序和处理





#### 8 消息广播

1）在zookeeper集群中，数据副本的传递策略就是采用消息广播模式。zookeeper中农数据副本的同步方式与二段提交相似，但是却又不同。二段提交要求协调者必须等到所有的参与者全部反馈ACK确认消息后，再发送commit消息。要求所有的参与者要么全部成功，要么全部失败。二段提交会产生严重的阻塞问题。

2）Zab协议中 Leader 等待 Follower 的ACK反馈消息是指“只要半数以上的Follower成功反馈即可，不需要收到全部Follower反馈”



![img](https:////upload-images.jianshu.io/upload_images/1053629-447433fdf7a1d7d6.png?imageMogr2/auto-orient/strip|imageView2/2/w/555/format/webp)

消息广播流程图



#### 9 消息广播具体步骤

1）客户端发起一个写操作请求。

2）Leader 服务器将客户端的请求转化为事务 Proposal 提案，同时为每个 Proposal 分配一个全局的ID，即zxid。

3）Leader 服务器为每个 Follower 服务器分配一个单独的队列，然后将需要广播的 Proposal 依次放到队列中取，并且根据 FIFO 策略进行消息发送。

4）Follower 接收到 Proposal 后，会首先将其以事务日志的方式写入本地磁盘中，写入成功后向 Leader 反馈一个 Ack 响应消息。

5）Leader 接收到超过半数以上 Follower 的 Ack 响应消息后，即认为消息发送成功，可以发送 commit 消息。

6）Leader 向所有 Follower 广播 commit 消息，同时自身也会完成事务提交。Follower 接收到 commit 消息后，会将上一条事务提交。

**zookeeper 采用 Zab 协议的核心，就是只要有一台服务器提交了 Proposal，就要确保所有的服务器最终都能正确提交 Proposal。这也是 CAP/BASE 实现最终一致性的一个体现。**

**Leader 服务器与每一个 Follower 服务器之间都维护了一个单独的 FIFO 消息队列进行收发消息，使用队列消息可以做到异步解耦。 Leader 和 Follower 之间只需要往队列中发消息即可。如果使用同步的方式会引起阻塞，性能要下降很多**



#### 10 Zab 协议如何保证数据一致性

假设两种异常情况：
 1、一个事务在 Leader 上提交了，并且过半的 Folower 都响应 Ack 了，但是 Leader 在 Commit 消息发出之前挂了。
 2、假设一个事务在 Leader 提出之后，Leader 挂了。

要确保如果发生上述两种情况，数据还能保持一致性，那么 Zab 协议选举算法必须满足以下要求：

**Zab 协议崩溃恢复要求满足以下两个要求**：
 1）**确保已经被 Leader 提交的 Proposal 必须最终被所有的 Follower 服务器提交**。
 2）**确保丢弃已经被 Leader 提出的但是没有被提交的 Proposal**。

根据上述要求
 Zab协议需要保证选举出来的Leader需要满足以下条件：
 1）**新选举出来的 Leader 不能包含未提交的 Proposal** 。
 即新选举的 Leader 必须都是已经提交了 Proposal 的 Follower 服务器节点。
 2）**新选举的 Leader 节点中含有最大的 zxid** 。
 这样做的好处是可以避免 Leader 服务器检查 Proposal 的提交和丢弃工作。
 

#### 11 Zab 如何数据同步

1）完成 Leader 选举后（新的 Leader 具有最高的zxid），在正式开始工作之前（接收事务请求，然后提出新的 Proposal），Leader 服务器会首先确认事务日志中的所有的 Proposal 是否已经被集群中过半的服务器 Commit。

2）Leader 服务器需要确保所有的 Follower 服务器能够接收到每一条事务的 Proposal ，并且能将所有已经提交的事务 Proposal 应用到内存数据中。等到 Follower 将所有尚未同步的事务 Proposal 都从 Leader 服务器上同步过啦并且应用到内存数据中以后，Leader 才会把该 Follower 加入到真正可用的 Follower 列表中。
 

#### 12 Zab 数据同步过程中，如何处理需要丢弃的 Proposal

在 Zab 的事务编号 zxid 设计中，zxid是一个64位的数字。

其中低32位可以看成一个简单的单增计数器，针对客户端每一个事务请求，Leader 在产生新的 Proposal 事务时，都会对该计数器加1。而高32位则代表了 Leader 周期的 epoch 编号。

> epoch 编号可以理解为当前集群所处的年代，或者周期。每次Leader变更之后都会在 epoch 的基础上加1，这样旧的 Leader 崩溃恢复之后，其他Follower 也不会听它的了，因为 Follower 只服从epoch最高的 Leader 命令。

每当选举产生一个新的 Leader ，就会从这个 Leader 服务器上取出本地事务日志充最大编号 Proposal 的 zxid，并从 zxid 中解析得到对应的 epoch 编号，然后再对其加1，之后该编号就作为新的 epoch 值，并将低32位数字归零，由0开始重新生成zxid。

**Zab 协议通过 epoch 编号来区分 Leader 变化周期**，能够有效避免不同的 Leader 错误的使用了相同的 zxid 编号提出了不一样的 Proposal 的异常情况。

基于以上策略
 **当一个包含了上一个 Leader 周期中尚未提交过的事务 Proposal 的服务器启动时，当这台机器加入集群中，以 Follower 角色连上 Leader 服务器后，Leader 服务器会根据自己服务器上最后提交的 Proposal 来和 Follower 服务器的 Proposal 进行比对，比对的结果肯定是 Leader 要求 Follower 进行一个回退操作，回退到一个确实已经被集群中过半机器 Commit 的最新 Proposal**。

------



#### 13 实现原理

**Zab 节点有三种状态**：

- Following：当前节点是跟随者，服从 Leader 节点的命令。
- Leading：当前节点是 Leader，负责协调事务。
- Election/Looking：节点处于选举状态，正在寻找 Leader。

代码实现中，多了一种状态：Observing 状态
 这是 Zookeeper 引入 Observer 之后加入的，Observer 不参与选举，是只读节点，跟 Zab 协议没有关系。

**节点的持久状态**：

- history：当前节点接收到事务 Proposal 的Log
- acceptedEpoch：Follower 已经接受的 Leader 更改 epoch 的 newEpoch 提议。
- currentEpoch：当前所处的 Leader 年代
- lastZxid：history 中最近接收到的Proposal 的 zxid（最大zxid）
   

#### Zab 的四个阶段

**1、选举阶段（Leader Election）**
 节点在一开始都处于选举节点，只要有一个节点得到超过半数节点的票数，它就可以当选准 Leader，只有到达第三个阶段（也就是同步阶段），这个准 Leader 才会成为真正的 Leader。

**Zookeeper 规定所有有效的投票都必须在同一个 轮次 中，每个服务器在开始新一轮投票时，都会对自己维护的 logicalClock 进行自增操作**。

每个服务器在广播自己的选票前，会将自己的投票箱（recvset）清空。该投票箱记录了所受到的选票。
 例如：Server_2 投票给 Server_3，Server_3 投票给 Server_1，则Server_1的投票箱为(2,3)、(3,1)、(1,1)。（每个服务器都会默认给自己投票）

前一个数字表示投票者，后一个数字表示被选举者。票箱中只会记录每一个投票者的最后一次投票记录，如果投票者更新自己的选票，则其他服务器收到该新选票后会在自己的票箱中更新该服务器的选票。

**这一阶段的目的就是为了选出一个准 Leader ，然后进入下一个阶段。**
 协议并没有规定详细的选举算法，后面会提到实现中使用的 Fast Leader Election。

![img](https:////upload-images.jianshu.io/upload_images/1053629-cb0c776cef667fcb.png?imageMogr2/auto-orient/strip|imageView2/2/w/700/format/webp)

选举流程







**2、发现阶段（Descovery）**
 在这个阶段，Followers 和上一轮选举出的准 Leader 进行通信，同步 Followers 最近接收的事务 Proposal 。
 一个 Follower 只会连接一个 Leader，如果一个 Follower 节点认为另一个 Follower 节点，则会在尝试连接时被拒绝。被拒绝之后，该节点就会进入 Leader Election阶段。

**这个阶段的主要目的是发现当前大多数节点接收的最新 Proposal，并且准 Leader 生成新的 epoch ，让 Followers 接收，更新它们的 acceptedEpoch**。

![img](https:////upload-images.jianshu.io/upload_images/1053629-c75701e220688a8e.png?imageMogr2/auto-orient/strip|imageView2/2/w/700/format/webp)

发现流程







**3、同步阶段（Synchronization）**
 **同步阶段主要是利用 Leader 前一阶段获得的最新 Proposal 历史，同步集群中所有的副本**。
 只有当 quorum（超过半数的节点） 都同步完成，准 Leader 才会成为真正的 Leader。Follower 只会接收 zxid 比自己 lastZxid 大的 Proposal。

![img](https:////upload-images.jianshu.io/upload_images/1053629-594a86e8224affba.png?imageMogr2/auto-orient/strip|imageView2/2/w/700/format/webp)

同步流程







**4、广播阶段（Broadcast）**
 到了这个阶段，Zookeeper 集群才能正式对外提供事务服务，并且 Leader 可以进行消息广播。同时，如果有新的节点加入，还需要对新节点进行同步。
 需要注意的是，Zab 提交事务并不像 2PC 一样需要全部 Follower 都 Ack，只需要得到 quorum（超过半数的节点）的Ack 就可以。

![img](https:////upload-images.jianshu.io/upload_images/1053629-6c9e4297627e4570.png?imageMogr2/auto-orient/strip|imageView2/2/w/700/format/webp)

广播流程



------



## 14 协议实现

协议的 Java 版本实现跟上面的定义略有不同，选举阶段使用的是 Fast Leader Election（FLE），它包含了步骤1的发现指责。因为FLE会选举拥有最新提议的历史节点作为 Leader，这样就省去了发现最新提议的步骤。

实际的实现将发现和同步阶段合并为 Recovery Phase（恢复阶段），所以，Zab 的实现实际上有三个阶段。

#### Zab协议三个阶段：

1）**选举（Fast Leader Election）**
 2）**恢复（Recovery Phase）**
 3）**广播（Broadcast Phase）**

**Fast Leader Election（快速选举）**
 前面提到的 FLE 会选举拥有最新Proposal history （lastZxid最大）的节点作为 Leader，这样就省去了发现最新提议的步骤。**这是基于拥有最新提议的节点也拥有最新的提交记录**

- **成为 Leader 的条件：**
   1）选 epoch 最大的
   2）若 epoch 相等，选 zxid 最大的
   3）若 epoch 和 zxid 相等，选择 server_id 最大的（zoo.cfg中的myid）

节点在选举开始时，都默认投票给自己，当接收其他节点的选票时，会根据上面的 **Leader条件** 判断并且更改自己的选票，然后重新发送选票给其他节点。**当有一个节点的得票超过半数，该节点会设置自己的状态为 Leading ，其他节点会设置自己的状态为 Following**。



![img](https:////upload-images.jianshu.io/upload_images/1053629-75683fa04d349414.png?imageMogr2/auto-orient/strip|imageView2/2/w/700/format/webp)

选举过程







**Recovery Phase（恢复阶段）**
 这一阶段 Follower 发送他们的 lastZxid 给 Leader，Leader 根据 lastZxid 决定如何同步数据。这里的实现跟前面的 Phase 2 有所不同：Follower 收到 TRUNC 指令会终止 L.lastCommitedZxid 之后的 Proposal ，收到 DIFF 指令会接收新的 Proposal。

> history.lastCommitedZxid：最近被提交的 Proposal zxid
>  history.oldThreshold：被认为已经太旧的已经提交的 Proposal zxid
>
> ![img](https:////upload-images.jianshu.io/upload_images/1053629-613f99cec1c34e2e.png?imageMogr2/auto-orient/strip|imageView2/2/w/700/format/webp)



作者：_Zy
链接：https://www.jianshu.com/p/2bceacd60b8a
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。