package com.yjt.concurrent.netty.day07.googleprotobuf.client;

import com.yjt.concurrent.netty.day07.googleprotobuf.bean.SubscribeReq;
import com.yjt.concurrent.netty.day07.googleprotobuf.bean.SubscribeRsp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * TODO
 * ClassName: Client07Handler
 * Date: 2019-11-12 0:07
 * author Administrator
 * version V1.0
 */
public class Client07Handler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int loop = 500;
        for (int i = 0; i < loop; i++) {
            SubscribeReq.SubscribeReqBean request = buildRquest((i+1));
            ctx.writeAndFlush(request);
        }
    }

    private SubscribeReq.SubscribeReqBean buildRquest(int seq) {
        return SubscribeReq.SubscribeReqBean.newBuilder().setSubSeqId(seq).setUserName("coco"+seq).setProductName("产品").setAddress("地址").build();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       SubscribeRsp.SubscribeRspBean resp = (SubscribeRsp.SubscribeRspBean) msg;
        System.out.println("客户端接收响应:"+"["+"subSeqId:"+resp.getSubSeqId()
                +" rspCode:"+resp.getRespCode()
                +" desc:"+resp.getDescBytes().toStringUtf8()+"]");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();;
        ctx.close();
    }
}
