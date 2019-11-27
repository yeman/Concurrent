package com.yjt.concurrent.netty.day09;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * TODO
 * ClassName: HttpXmlRequestDecoder
 * Date: 2019-11-20 22:02
 * author Administrator
 * version V1.0
 */
public class HttpXmlRequestDecoder extends AbstractHttpXmlDecoder<FullHttpRequest>{

    protected HttpXmlRequestDecoder(Class<?> clz) {
        super(clz);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        if(!msg.decoderResult().isSuccess()){
            sendError(ctx,HttpResponseStatus.BAD_REQUEST);
            return;
        }
        HttpXmlRequest request = new HttpXmlRequest(msg,decodeXml(ctx,msg.content()));
        out.add(request);
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status, Unpooled.copiedBuffer("ERROR:"+status.toString(),CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.TEXT_PLAIN+";"+ CharsetUtil.UTF_8.name());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }
}
