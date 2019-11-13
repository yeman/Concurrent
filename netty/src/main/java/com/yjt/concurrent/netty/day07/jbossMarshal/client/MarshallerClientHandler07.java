package com.yjt.concurrent.netty.day07.jbossMarshal.client;

import com.yjt.concurrent.netty.day07.jbossMarshal.bean.SubscribeReqBean;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * TODO
 * ClassName: MarshallerClientHandler07
 * Date: 2019-11-12 22:39
 * author Administrator
 * version V1.0
 */
public class MarshallerClientHandler07 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int loop = 100;
        for (int i = 0; i < loop; i++) {
            SubscribeReqBean req = bulidReq((i + 1));
            System.out.println("客户端创建请求对象:"+ req);
            ctx.write(req);
        }
        ctx.flush();
    }

    private SubscribeReqBean bulidReq(int i) {
        SubscribeReqBean req = new SubscribeReqBean();
        req.setSubReqId(i);
        req.setUserName("coco");
        req.setProductName("产品" + i);
        req.setAddress("地址");
        req.setPhoneNumber("13080660677");
        return req;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端接收响应:" + msg.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
