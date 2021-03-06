package com.yjt.concurrent.netty.day09;

import com.yjt.concurrent.netty.day09.bean.Person;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * TODO
 * ClassName: HttpClient
 * Date: 2019-11-21 20:28
 * author Administrator
 * version V1.0
 */
public class HttpClient {

    public void connect(String host, int port) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast( new HttpResponseDecoder())
                                    .addLast( new HttpObjectAggregator(65536))
                                    .addLast( new HttpXmlResponseDecoder(Person.class))
                                    .addLast( new HttpRequestEncoder())
                                    .addLast( new HttpXmlRequestEncoder())
                                    .addLast(new HttpXmlClientHandler());

                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(port)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        final int port = 8080;
        final String host = "127.0.0.1";
        HttpClient client = new HttpClient();
        client.connect(host, port);
    }
}
