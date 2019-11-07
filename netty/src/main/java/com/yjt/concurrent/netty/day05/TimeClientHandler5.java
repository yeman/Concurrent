package com.yjt.concurrent.netty.day05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;

/**
 * TODO
 * ClassName: TimeClientHandler5
 * Date: 2019-11-07 23:30
 * author Administrator
 * version V1.0
 */
public class TimeClientHandler5 extends ChannelInboundHandlerAdapter {
    private int counter;
    private byte[] req;

    public  TimeClientHandler5(){
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
        ByteBuf req = (ByteBuf) msg;
        byte[] bytes = new byte[req.readableBytes()];
        req.readBytes(bytes);
        String body = new String(bytes,"UTF-8");
        System.out.println("client recevie:"+ body + " couter:"+(++counter));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
