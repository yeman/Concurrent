package com.yjt.concurrent.netty.day09;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import java.io.StringWriter;

/**
 * TODO
 * ClassName: AbstractHttpXmlEncoder
 * Date: 2019-11-20 21:46
 * author Administrator
 * version V1.0
 */
public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {
    IBindingFactory bindingFactory;
    StringWriter stringWriter;

    protected ByteBuf encodeObject(ChannelHandlerContext chc,Object body) throws Exception {
        bindingFactory = BindingDirectory.getFactory(body.getClass());
        IMarshallingContext marshallingContext = bindingFactory.createMarshallingContext();
        marshallingContext.setIndent(2);
        stringWriter = new StringWriter();
        marshallingContext.marshalDocument(body,CharsetUtil.UTF_8.name(),null,stringWriter);
        String xml = stringWriter.toString();
        stringWriter.close();
        stringWriter = null;
        ByteBuf byteBuf = Unpooled.copiedBuffer(xml,CharsetUtil.UTF_8);
        return byteBuf;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(stringWriter!=null){
            stringWriter.close();
            stringWriter = null;
        }
    }
}
