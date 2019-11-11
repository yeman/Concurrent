package com.yjt.concurrent.netty.day07.serializable;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * TODO
 * ClassName: UserInfo
 * Date: 2019-11-09 12:59
 * author Administrator
 * version V1.0
 */
@Builder
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 742957336012594663L;
    private int userId;
    private String userName;

    //序列成字节数组
    public byte[] codeC(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] userNameBytes = this.userName.getBytes();
        byteBuffer.putInt(userNameBytes.length).put(userNameBytes).putInt(this.getUserId()).flip();
        userNameBytes = null;
        byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);
        return result;
    }

    //序列成字节数组
    public byte[] codeC(ByteBuffer byteBuffer){
        byteBuffer.clear();
        byte[] userNameBytes = this.userName.getBytes();
        byteBuffer.putInt(userNameBytes.length).put(userNameBytes).putInt(this.getUserId()).flip();
        userNameBytes = null;
        byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);
        return result;
    }
}
