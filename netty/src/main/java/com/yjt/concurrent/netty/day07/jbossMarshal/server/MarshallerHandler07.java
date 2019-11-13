package com.yjt.concurrent.netty.day07.jbossMarshal.server;

import com.yjt.concurrent.netty.day07.jbossMarshal.bean.SubscribeReqBean;
import com.yjt.concurrent.netty.day07.jbossMarshal.bean.SubscribeRespBean;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * TODO
 * ClassName: MarshallerHandler07
 * Date: 2019-11-12 22:25
 * author Administrator
 * version V1.0
 */
public class MarshallerHandler07 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqBean req = (SubscribeReqBean) msg;
        System.out.println("服务器接收请求:"+ req);
        if(req.getUserName().equalsIgnoreCase("coco")){
            ctx.writeAndFlush(bulidRsp(req.getSubReqId()));
        }
    }

    private SubscribeRespBean bulidRsp(int subReqId) {
        SubscribeRespBean resp = new SubscribeRespBean();
        resp.setSubSeqID(subReqId);
        resp.setRespCode(200);
        resp.setDesc("测试描述");
        return resp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
       ctx.close();
    }
}
