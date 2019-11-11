#### 1下载ideal protobuf插件, 编写proto文件

```
package bean;
option java_package ="com.yjt.concurrent.netty.day07.googleprotobuf";
option java_outer_classname = "SubscribeReqBean";

message SubscribeReq{
    required int32 subSeqId = 1;
    required string userName = 2;
    required string productName = 3;
    required string address = 4;
}
```

2 运行命令

```
protoc.exe --java_out=temp temp/SubscribeRsp.proto
```
netty提供的半包处理器
    1 LengthFieldBasedFrameDecoder
    2 继承 ByteToMessageDecoder,自行处理半包消息
