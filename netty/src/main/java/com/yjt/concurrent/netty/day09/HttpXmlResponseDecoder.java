package com.yjt.concurrent.netty.day09;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

/**
 * 应答消息解码器
 * ClassName: HttpXmlResponseDecoder
 * Date: 2019-11-20 22:02
 * author Administrator
 * version V1.0
 */
public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<DefaultFullHttpResponse> {


    protected HttpXmlResponseDecoder(Class<?> clz) {
        super(clz);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultFullHttpResponse msg, List<Object> out) throws Exception {
        HttpXmlResponse response = new HttpXmlResponse(msg, decodeXml(ctx, msg.content()));
        out.add(response);
    }
}
