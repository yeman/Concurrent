package com.yjt.concurrent.netty.day09;

import com.yjt.concurrent.netty.day09.bean.Person;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * TODO
 * ClassName: HttpServer
 * Date: 2019-11-21 21:36
 * author Administrator
 * version V1.0
 */
public class HttpServer {

    public void connect(final int port) {
        NioEventLoopGroup main = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(main, work)
                    .channel(NioServerSocketChannel.class)
                    //.option(ChannelOption.SO_BACKLOG, 1024)
                    //.option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("server->httpRequstDecoder", new HttpRequestDecoder())
                                    .addLast("server->httpObjectAggregator", new HttpObjectAggregator(65536))
                                    .addLast("server->httpXmlRequestDecoder", new HttpXmlRequestDecoder(Person.class))
                                    .addLast("server->httpReponseEncoder", new HttpResponseEncoder())
                                    .addLast("server->httpXmlReponseEncoder", new HttpXmlResponseEncoder())
                                    .addLast("server->httpXmlServerHandler", new HttpXmlServerHandler());

                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            work.shutdownGracefully();
            main.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        final int port = 8080;
        HttpServer server = new HttpServer();
        server.connect(port);
    }
}
