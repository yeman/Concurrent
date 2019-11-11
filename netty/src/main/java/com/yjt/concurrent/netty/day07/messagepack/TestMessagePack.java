package com.yjt.concurrent.netty.day07.messagepack;

import com.alibaba.fastjson.JSONObject;
import com.yjt.concurrent.netty.day07.messagepack.bean.UserInfo;
import org.junit.Test;
import org.msgpack.MessagePack;
import org.msgpack.template.StringTemplate;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * ClassName: TestMessagePack
 * Date: 2019-11-09 15:12
 * author Administrator
 * version V1.0
 */
public class TestMessagePack {

    /**
     * 测试api
     */
    @Test
    public void test01() throws IOException {
        List<String> list = new ArrayList<String>();
        list.add("jack");
        list.add("lucy");
        list.add("john");
        MessagePack messagePack = new MessagePack();
        byte[] bytes = messagePack.write(list);
        List<String> streamsList = messagePack.read(bytes, Templates.tList(StringTemplate.getInstance()));
        System.out.println(streamsList.toString());
    }

    // 对象序列化和反序列化注意 password 用了ignore
    @Test
    public void test02() throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(10000);
        userInfo.setUserName("华安");
        userInfo.setUserCode("9527");
        userInfo.setPassword("123456");
        userInfo.setCompact(true);
        MessagePack mp = new MessagePack();
        byte[] userInfoBytes = mp.write(userInfo);
        UserInfo deserializeUserInfo = mp.read(userInfoBytes,UserInfo.class);
        System.out.println("反序列化:"+deserializeUserInfo);
    }

    //将json序列化,并观察json大小
    @Test
    public void test03() throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(10000);
        userInfo.setUserName("华安");
        userInfo.setUserCode("9527");
        userInfo.setPassword("123456");
        userInfo.setCompact(true);
       //###################
        long start = System.currentTimeMillis();
        byte[] jsonBytes =  JSONObject.toJSONBytes(userInfo);
        long end = System.currentTimeMillis();
        System.out.println("fastjson 耗时:"+(end-start)+",序列json字节长度:"+jsonBytes.length);
        //###################

        //###################
        start = System.currentTimeMillis();
        JSONObject json = (JSONObject) JSONObject.toJSON(userInfo);
        String jsonStr = json.toJSONString();
        end = System.currentTimeMillis();
        //序列json字符串:{"password":"123456","userName":"华安","userId":10000,"userCode":"9527"},字节长度:74
        System.out.println("fastjson 耗时:"+(end-start)+",序列json字符串:"+jsonStr+",字节长度:"+jsonStr.getBytes().length);
        //###################

        //###################
        start = System.currentTimeMillis();
        UserInfo userInfo1 = JSONObject.toJavaObject(json,UserInfo.class);
        end = System.currentTimeMillis();
        System.out.println("fastjson 耗时:"+(end-start)+",反序列化:"+userInfo1);
        //###################

        //###################
        start = System.currentTimeMillis();
        MessagePack messagePack = new MessagePack();
        byte[] bytes = messagePack.write(userInfo);
        end = System.currentTimeMillis();
        System.out.println("MessagePack 耗时:"+(end-start)+",序列化字节长度:"+bytes.length);

        start = System.currentTimeMillis();
        UserInfo userInfo2 = messagePack.read(bytes,UserInfo.class);
        end = System.currentTimeMillis();
        System.out.println("MessagePack 耗时:"+(end-start)+",反序列化:"+userInfo2);
    }

}
