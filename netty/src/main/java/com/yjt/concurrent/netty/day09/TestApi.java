package com.yjt.concurrent.netty.day09;

import com.yjt.concurrent.netty.day09.bean.Address;
import com.yjt.concurrent.netty.day09.bean.Person;
import com.yjt.concurrent.netty.day09.marshal.MarshalUtil;
import org.junit.Test;

/**
 * TODO
 * ClassName: TestApi
 * Date: 2019-11-19 22:52
 * author Administrator
 * version V1.0
 */
public class TestApi {

    @Test
    public void testMarshall(){
        Address address = new Address();
        address.setCompanyAddress("公司地址");
        address.setHomeAddress("大陆");
        address.setContactAddress("联系地址");

        Person p = new Person();
        p.setId(1000);
        p.setName("景甜");
        p.setAddress(address);
        String xml = MarshalUtil.marshal(p);
        System.out.println("bean序列化成xml:"+ xml);
    }

    @Test
    public void testUnmarshall(){
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<person xmlns=\"http://yjt.com/concurrent/netty/day09/bean\" id=\"1000\">\n" +
                "  <name>景甜</name>\n" +
                "  <address>\n" +
                "    <contactAddress>联系地址</contactAddress>\n" +
                "    <homeAddress>大陆</homeAddress>\n" +
                "    <companyAddress>公司地址</companyAddress>\n" +
                "  </address>\n" +
                "</person>";
        Person person = MarshalUtil.unmarshal(xml,Person.class);
        System.out.println("反序列化:"+ person.toString());
    }
}
