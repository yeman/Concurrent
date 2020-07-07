 JiBX 

JiBX 是一个绑定 XML 数据到 Java 对象的框架。JiBX 用一个绑定定义文挡（binding definition document）来定义 XML 数据与 Java 对象转换的规则（binding.xml），这个文挡就是联系 XML 数据与 Java 对象之间的桥梁。

下载地址 http://jibx.sourceforge.net/index.html 

生成

```
java -cp F:\jibx_1_3_1\jibx\lib\jibx-tools.jar org.jibx.binding.generator.BindGen -t E:\idea_WorkSpace\Concurrent\netty\src\main\jibx -v  com.yjt.concurrent.netty.day09.bean.Person com.yjt.concurrent.netty.day09.bean.Address
```

- -cp 设置classpath路径，可以加入需要依赖的包。
- -b 设置生成的binding.xml文件存放路径及文件名
- -v 显示控制台信息
- 最后一个参数为需要转换的类



2 对字节码增强

2.1 使用命令对字节码增强

```
java -cp F:\jibx_1_3_1\jibx\lib\jibx-bind.jar org.jibx.binding.Compile -v E:\idea_WorkSpace\Concurrent\netty\src\main\jibx\binding.xml


```

2.1对字节码增强的第二种方法

 执行maven install即可增强class文件 



**3  httpxml客户端响应问题排查**