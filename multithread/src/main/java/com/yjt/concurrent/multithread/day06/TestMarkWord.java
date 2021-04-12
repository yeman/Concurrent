package com.yjt.concurrent.multithread.day06;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

/**
 * TODO
 * ClassName: TestMarkWord
 * Date: 2020-08-19 23:12
 * author Administrator
 * version V1.0
 */
@Slf4j
public class TestMarkWord {

    @Test
    public void testPrintSimpleObject(){
        Object obj = new Object();
        log.info(ClassLayout.parseInstance(obj).toPrintable());
    }
}
