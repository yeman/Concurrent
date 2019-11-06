package com.yjt.concurrent.netty.day04;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * TODO
 * ClassName: TimeServer
 * Date: 2019-11-04 21:51
 * author Administrator
 * version V1.0
 */
public class TimeServer {
    public void bind(int port) {
        EventLoopGroup parentLoopGroup = new NioEventLoopGroup();
        EventLoopGroup childLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(parentLoopGroup, childLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
            //绑定端口,同步等待成功
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            //等待服务器监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅退出,释放线程池资源
            parentLoopGroup.shutdownGracefully();
            childLoopGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        TimeServer server = new TimeServer();
        server.bind(port);
    }
}
