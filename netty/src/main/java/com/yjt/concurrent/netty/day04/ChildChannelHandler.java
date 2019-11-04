package com.yjt.concurrent.netty.day04;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * TODO
 * ClassName: ChildChannelHandler
 * Date: 2019-11-04 22:50
 * author Administrator
 * version V1.0
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addLast(new TimeServerHandler());
    }
}
