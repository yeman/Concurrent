package com.yjt.concurrent.netty.day09;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.List;

/**
 * 应答消息加密
 * ClassName: HttpXmlResponseEncoder
 * Date: 2019-11-20 22:02
 * author Administrator
 * version V1.0
 */
public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse>{

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlResponse msg, List out) throws Exception {
        ByteBuf body = encodeObject(ctx,msg.getResult());
        FullHttpResponse httpResponse = msg.getHttpResponse();
        if(httpResponse ==null){
            httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,body);
        }else{
            httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,msg.getHttpResponse().status(),body);
        }
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/xml")
                              .set(HttpHeaderNames.CONTENT_LENGTH,body.readableBytes());
        out.add(httpResponse);
    }

}
