package com.yjt.concurrent.netty.day09;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.net.InetAddress;
import java.util.List;

/**
 * TODO
 * ClassName: HttpXmlRequestEncoder
 * Date: 2019-11-20 22:02
 * author Administrator
 * version V1.0
 */
public class HttpXmlRequestEncoder extends AbstractHttpXmlEncoder<HttpXmlRequest>{
    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlRequest msg, List out) throws Exception {
        ByteBuf byteBuf = encodeObject(ctx,msg);
        FullHttpRequest httpRequest = msg.getHttpRequest();
        if(httpRequest ==null){
            httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,"/xml",byteBuf);
            httpRequest.headers().set(HttpHeaderNames.HOST, InetAddress.getLocalHost().getHostAddress())
                                 .set(HttpHeaderNames.CONNECTION,HttpHeaderValues.CLOSE)
                                 .set(HttpHeaderNames.ACCEPT_ENCODING,HttpHeaderValues.GZIP_DEFLATE)
                                 .set(HttpHeaderNames.ACCEPT_CHARSET,"ISO-8859-1,utf-8;q=0.7,*;q=0.7")
                                .set(HttpHeaderNames.ACCEPT_LANGUAGE,"zh")
                                .set(HttpHeaderNames.USER_AGENT,"Netty xml")
                                .set(HttpHeaderNames.ACCEPT,"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.readableBytes());
            out.add(msg);

        }
    }

}
