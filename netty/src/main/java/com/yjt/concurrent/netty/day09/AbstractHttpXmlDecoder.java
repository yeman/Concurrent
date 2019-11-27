package com.yjt.concurrent.netty.day09;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;

import java.io.StringReader;

/**
 * TODO
 * ClassName: AbstractHttpXmlDecoder
 * Date: 2019-11-20 22:24
 * author Administrator
 * version V1.0
 */
public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {
    private Class<?> clz ;
    private StringReader stringReader;
    private IBindingFactory bindingFactory;

    protected AbstractHttpXmlDecoder( Class<?> clz){
        this.clz = clz;
    }

    protected  Object decodeXml(ChannelHandlerContext chc, ByteBuf body) throws Exception {
        bindingFactory =  BindingDirectory.getFactory(clz);
        IUnmarshallingContext unmarshallingContext = bindingFactory.createUnmarshallingContext();
        String inputXml = body.toString(CharsetUtil.UTF_8);
        System.out.println("-->xml:"+ inputXml);
        stringReader = new StringReader(inputXml);
        Object object =  unmarshallingContext.unmarshalDocument(stringReader,null);
        stringReader.close();
        stringReader = null;
        return object;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(stringReader!=null){
            stringReader.close();
            stringReader = null;
        }
    }
}
