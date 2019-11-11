package com.yjt.concurrent.netty.day07.objectencoder.client;

import com.yjt.concurrent.netty.day07.objectencoder.bean.SubscribeReqBean;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * TODO
 * ClassName: SubscribeClientHandler
 * Date: 2019-11-10 13:48
 * author Administrator
 * version V1.0
 */
public class SubscribeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            SubscribeReqBean req = buildRequest(i);
            ctx.writeAndFlush(req);
        }
    }

    private SubscribeReqBean buildRequest(int i) {
        SubscribeReqBean req = new SubscribeReqBean();
        req.setSubReqId((i+1));
        req.setUserName("coco");
        req.setPhoneNumber("111");
        req.setProductName("产品1");

        return req;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqBean resp = (SubscribeReqBean) msg;
        System.out.println("客户端接收:"+ resp.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
