package com.yjt.concurrent.netty.day09.bean;

import lombok.Data;

/**
 * TODO
 * ClassName: Person
 * Date: 2019-11-18 21:21
 * author Administrator
 * version V1.0
 */
@Data
public class Person {
    private long id;
    private String name;
    private Address address;

}
