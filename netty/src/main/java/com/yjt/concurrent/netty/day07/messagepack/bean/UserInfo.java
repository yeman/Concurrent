package com.yjt.concurrent.netty.day07.messagepack.bean;

import lombok.Data;
import org.msgpack.annotation.Ignore;
import org.msgpack.annotation.Message;

/**
 * TODO
 * ClassName: UserInfo
 * Date: 2019-11-09 15:25
 * author Administrator
 * version V1.0
 */
@Data
@Message
public class UserInfo {
    private int userId;
    private String userCode;
    private String userName;
    @Ignore
    private String password;
    private boolean compact;
}
