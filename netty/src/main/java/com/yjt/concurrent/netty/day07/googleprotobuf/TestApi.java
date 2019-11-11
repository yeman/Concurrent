package com.yjt.concurrent.netty.day07.googleprotobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yjt.concurrent.netty.day07.googleprotobuf.bean.SubscribeReq;
import org.junit.Test;

/**
 * TODO
 * ClassName: TestApi
 * Date: 2019-11-10 20:14
 * author Administrator
 * version V1.0
 */
public class TestApi {

    @Test
    public void test01() throws InvalidProtocolBufferException {
        SubscribeReq.SubscribeReqBean.Builder builder = SubscribeReq.SubscribeReqBean.newBuilder();
        builder.setSubSeqId(1).setUserName("测试").setProductName("产品").setAddress("地址");
        SubscribeReq.SubscribeReqBean subscribeReqBean = builder.build();
        System.out.println("序列化:"+ subscribeReqBean.toByteArray());
        byte[] bytes = subscribeReqBean.toByteArray();
        for (int i = 0; i < bytes.length; i++) {
            System.out.println(bytes[i]);
        }
        //字节反序列化
        SubscribeReq.SubscribeReqBean bean = SubscribeReq.SubscribeReqBean.parseFrom(bytes);
        System.out.println("反序列化:"+ bean.getSubSeqId()+" "+bean.getUserNameBytes().toStringUtf8()+" "+bean.getProductNameBytes().toStringUtf8()+" "+bean.getAddressBytes().toStringUtf8());
    }
}
