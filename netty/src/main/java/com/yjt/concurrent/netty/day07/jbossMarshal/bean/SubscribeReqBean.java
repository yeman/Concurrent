package com.yjt.concurrent.netty.day07.jbossMarshal.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 * ClassName: SubscribeReqBean
 * Date: 2019-11-10 10:25
 * author Administrator
 * version V1.0
 */
@Data
public class SubscribeReqBean implements Serializable {
    private static final long serialVersionUID = -4613056338847353908L;
    private int subReqId;
    private String userName;
    private String productName;
    private String phoneNumber;
    private String address;
}
