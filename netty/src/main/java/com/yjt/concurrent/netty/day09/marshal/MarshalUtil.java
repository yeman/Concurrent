package com.yjt.concurrent.netty.day09.marshal;

import org.jibx.runtime.*;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * TODO
 * ClassName: MarshalUtil
 * Date: 2019-11-19 21:49
 * author Administrator
 * version V1.0
 */
public class MarshalUtil {

   public static  <T> String marshal(T t){
       try {
           IBindingFactory bindingFactory =  BindingDirectory.getFactory(t.getClass());
           IMarshallingContext marshallingContext = bindingFactory.createMarshallingContext();
           marshallingContext.setIndent(2);
           StringWriter stringWriter = new StringWriter();
           marshallingContext.setOutput(stringWriter);
           marshallingContext.marshalDocument(t, "UTF-8", null);
           return stringWriter.toString();
       } catch (JiBXException e) {
           e.printStackTrace();
       }
       return null;
   }
   public static  <T> T unmarshal(String inputXml,Class<T> clz){
       try {
           IBindingFactory bindingFactory =  BindingDirectory.getFactory(clz);
           IUnmarshallingContext unmarshallingContext = bindingFactory.createUnmarshallingContext();
           StringReader stringReader = new StringReader(inputXml);
           return (T)unmarshallingContext.unmarshalDocument(stringReader,null);
       } catch (JiBXException e) {
           e.printStackTrace();
       }
       return null;
   }
}
