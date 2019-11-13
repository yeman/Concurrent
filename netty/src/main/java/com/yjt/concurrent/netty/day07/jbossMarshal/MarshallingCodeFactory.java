package com.yjt.concurrent.netty.day07.jbossMarshal;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * TODO
 * ClassName: MarshallingCodeFactory
 * Date: 2019-11-12 21:53
 * author Administrator
 * version V1.0
 */
public final class MarshallingCodeFactory {

    public static MarshallingEncoder buildMarshallingEncoder() {
        //1 创建 MarshallingEncoder
        //2 创建 MarshallerProvider
        //3 创建 MarshallerFactory  MarshallingConfiguration
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
        MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }

    public static MarshallingDecoder buildMarshallingDecoder() {
        //1 创建 MarshallingDecoder
        //2 创建 MarshallerProvider
        //3 创建 MarshallerFactory  MarshallingConfiguration
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
        MarshallingDecoder decoder = new MarshallingDecoder(provider);
        return decoder;
    }


}
