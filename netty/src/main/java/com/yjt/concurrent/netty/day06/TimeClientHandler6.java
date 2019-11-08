package com.yjt.concurrent.netty.day06;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * TODO
 * ClassName: TimeClientHandler6
 * Date: 2019-11-07 23:30
 * author Administrator
 * version V1.0
 */
public class TimeClientHandler6 extends ChannelInboundHandlerAdapter {
    private int counter;
    private byte[] req;

    public  TimeClientHandler6(){
        req = ("CURRENTTIME ORDER"+System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       ByteBuf msg = null;
       for(int i=0;i<100;i++){
           msg = Unpooled.buffer(req.length);
           msg.writeBytes(req);
           ctx.writeAndFlush(msg);
       }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("client recevie:"+ body + " couter:"+(++counter));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
