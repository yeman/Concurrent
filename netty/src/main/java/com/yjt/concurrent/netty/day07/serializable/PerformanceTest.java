package com.yjt.concurrent.netty.day07.serializable;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * TODO
 * ClassName: PerformanceTest
 * Date: 2019-11-09 13:10
 * author Administrator
 * version V1.0
 */
public class PerformanceTest {

    /**
     * 测试java对象序列化 和 字节序列化 存储性能
     */
    @Test
    public void test01() {
        UserInfo userInfo = UserInfo.builder().userId(1234).userName("测试123").build();
        byte[] codec = userInfo.codeC();
        System.out.println("codeC序列长度:" + codec.length);

        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(userInfo);
            oos.flush();

            System.out.println("java对象序列化长度:"+bos.toByteArray().length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
           if(oos!=null){
               try {
                   oos.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
           if(bos!=null){
               try {
                   bos.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        }
    }

    /**
     * 测试java对象序列化 和 字节序列化性能
     */
    @Test
    public void test02() {
        UserInfo userInfo = UserInfo.builder().userId(1234).userName("测试1234").build();
        int loop = 1000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i <loop ; i++) {
           try(
               ByteArrayOutputStream byteArrayOutputStream  = new ByteArrayOutputStream();
               ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            ){
                objectOutputStream.writeObject(userInfo);
                objectOutputStream.flush();
                byte[] result = byteArrayOutputStream.toByteArray();
            }catch (Exception e){
              e.printStackTrace();
           }


        }
        long end = System.currentTimeMillis();
        System.out.println("java序列化耗时:"+ (end-start) + "ms!");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        start = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            byte[] result = userInfo.codeC(byteBuffer);
        }
        end = System.currentTimeMillis();
        System.out.println("codeC耗时:"+ (end-start) + "ms!");
    }
}
