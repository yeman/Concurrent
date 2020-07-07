### zookeeper

#### api



#### acl

使用zkCli时，ACL的格式由<schema>:<id>:<acl>三段组成。

- schema：可以取下列值：world, auth, digest, host/ip
         **world：**默认方式，相当于全部都能访问
     　**auth**：代表已经认证通过的用户(cli中可以通过addauth digest user:pwd 来添加当前上下文中的授权用户)
  　　**digest**：即用户名:密码这种方式认证，这也是业务系统中最常用的。用 *username:password* 字符串来产生一个MD5串，然后该串被用来作为ACL ID。认证是通过明文发送*username:password* 来进行的，当用在ACL时，表达式为*username:base64* ，base64是password的SHA1摘要的编码。
  　　**ip**：使用客户端的主机IP作为ACL ID 。这个ACL表达式的格式为*addr/bits* ，此时addr中的有效位与客户端addr中的有效位进行比对。
- id： 标识身份，值依赖于schema做解析。
- acl：就是权限：cdwra分别表示create, delete,write,read, admin

#### state



