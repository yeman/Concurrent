package com.yjt.concurrent.netty.day07.jbossMarshal.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 * ClassName: SubscribeRespBean
 * Date: 2019-11-10 10:28
 * author Administrator
 * version V1.0
 */
@Data
public class SubscribeRespBean implements Serializable {
    private static final long serialVersionUID = 4454065827387297013L;
    private int subSeqID;
    private int respCode;
    private String desc;
}
