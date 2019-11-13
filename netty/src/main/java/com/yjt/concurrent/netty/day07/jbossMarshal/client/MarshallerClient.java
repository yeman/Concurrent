package com.yjt.concurrent.netty.day07.jbossMarshal.client;

import com.yjt.concurrent.netty.day07.jbossMarshal.MarshallingCodeFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * TODO
 * ClassName: MarshallerClient
 * Date: 2019-11-12 22:33
 * author Administrator
 * version V1.0
 */
public class MarshallerClient {

    public void connect(String host, int port) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder())
                                    .addLast(MarshallingCodeFactory.buildMarshallingEncoder())
                                    .addLast(new MarshallerClientHandler07());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8080;
        MarshallerClient client = new MarshallerClient();
        client.connect(host, port);
    }
}
