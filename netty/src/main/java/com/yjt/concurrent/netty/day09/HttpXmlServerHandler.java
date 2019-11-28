package com.yjt.concurrent.netty.day09;

import com.yjt.concurrent.netty.day09.bean.Person;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * TODO
 * ClassName: HttpXmlServerHandler
 * Date: 2019-11-21 22:05
 * author Administrator
 * version V1.0
 */
public class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        if(ctx.channel().isActive()){
            sendError(ctx,HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpXmlRequest msg) throws Exception {
        HttpRequest request = msg.getHttpRequest();
        Person person = (Person) msg.getBody();
        System.out.println("HttpXmlServerHandler 解码成bean:\t"+ person);
        doBusiness(person);
        ChannelFuture channelFuture = ctx.writeAndFlush(new HttpXmlResponse(null,person));
        if(!HttpUtil.isKeepAlive(request)){
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    ctx.close();
                }
            });
        }

    }

    private void doBusiness(Person person) {
        person.setName("新龙门客栈");
        person.setId(10086);
        person.getAddress().setCompanyAddress("中关村");
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status, Unpooled.copiedBuffer("ERROR:"+status.toString(), CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.TEXT_PLAIN+";"+ CharsetUtil.UTF_8.name());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }
}
