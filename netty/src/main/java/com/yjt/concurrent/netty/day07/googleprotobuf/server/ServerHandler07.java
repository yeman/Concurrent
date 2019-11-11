package com.yjt.concurrent.netty.day07.googleprotobuf.server;

import com.yjt.concurrent.netty.day07.googleprotobuf.bean.SubscribeReq;
import com.yjt.concurrent.netty.day07.googleprotobuf.bean.SubscribeRsp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * TODO
 * ClassName: ServerHandler07
 * Date: 2019-11-11 23:52
 * author Administrator
 * version V1.0
 */
public class ServerHandler07 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      SubscribeReq.SubscribeReqBean req = (SubscribeReq.SubscribeReqBean) msg;
        System.out.println("服务端接收请求:"+"["+"subSeqId:"+req.getSubSeqId()+" userName:"+req.getUserNameBytes().toStringUtf8()+" productName:"+req.getProductNameBytes().toStringUtf8()+" address:"+req.getAddressBytes().toStringUtf8()+"]");
      if(req.getUserNameBytes().toStringUtf8().startsWith("coco")){
          SubscribeRsp.SubscribeRspBean rsp = bulidRsp(req.getSubSeqId());
          ctx.writeAndFlush(rsp);
      }
    }

    private SubscribeRsp.SubscribeRspBean bulidRsp(int subSeqId) {
        return SubscribeRsp.SubscribeRspBean.newBuilder().setSubSeqId(subSeqId).setRespCode(200).setDesc("测试响应结果").build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
