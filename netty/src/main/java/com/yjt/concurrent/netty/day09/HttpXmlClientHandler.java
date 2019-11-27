package com.yjt.concurrent.netty.day09;

import com.yjt.concurrent.netty.day09.bean.Address;
import com.yjt.concurrent.netty.day09.bean.Person;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * TODO
 * ClassName: HttpXmlClientHandler
 * Date: 2019-11-21 21:24
 * author Administrator
 * version V1.0
 */
public class HttpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Person person = buildRequestObject();
        HttpXmlRequest request = new HttpXmlRequest(null, person);
        System.out.println("发起请求:" + request.toString());
        ctx.writeAndFlush(request);

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
        System.out.println("客户端收到响应:" + msg);
    }

    private Person buildRequestObject() {
        Address address = new Address();
        address.setCompanyAddress("公司地址");
        address.setHomeAddress("大陆");
        address.setContactAddress("联系地址");

        Person p = new Person();
        p.setId(1000);
        p.setName("景甜");
        p.setAddress(address);
        return p;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
