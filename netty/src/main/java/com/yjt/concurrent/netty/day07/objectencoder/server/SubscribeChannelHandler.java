package com.yjt.concurrent.netty.day07.objectencoder.server;

import cn.hutool.http.HttpStatus;
import com.yjt.concurrent.netty.day07.objectencoder.bean.SubscribeReqBean;
import com.yjt.concurrent.netty.day07.objectencoder.bean.SubscribeRespBean;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * TODO
 * ClassName: SubscribeChannelHandler
 * Date: 2019-11-10 10:53
 * author Administrator
 * version V1.0
 */
public class SubscribeChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqBean subscribeReq = (SubscribeReqBean) msg;
       if(subscribeReq.getUserName().startsWith("coco")){
           System.out.println("服务端接收客户端:"+subscribeReq);
           ctx.writeAndFlush(buildRsp(subscribeReq.getSubReqId()));
       }
    }

    private SubscribeRespBean buildRsp(int subReqId) {
        SubscribeRespBean resp = new SubscribeRespBean();
        resp.setSubSeqID(subReqId);
        resp.setRespCode(HttpStatus.HTTP_OK);
        resp.setDesc("订单详情内容");
        return resp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
