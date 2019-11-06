package com.yjt.concurrent.netty.day04;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * TODO
 * ClassName: ClientHandler
 * Date: 2019-11-06 21:54
 * author Administrator
 * version V1.0
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private final ByteBuf req;

    public TimeClientHandler() {
        byte[] requestBytes = "QUERY TIME".getBytes();
        req = Unpooled.buffer(requestBytes.length);
        req.writeBytes(requestBytes);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(req);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] response = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(response);
        String reponseStr = new String(response,"UTF-8");
        System.out.println("response ->"+ reponseStr);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
