package com.yjt.concurrent.multithread.threadLocal;

import lombok.Data;

/**
 * @className LoginConstant
 * @description TODO
 * @author YM
 * @date 2020-09-24 16:33
 * @version V1.0
 * @since 1.0
 **/
@Data
public class LoginConstant {

    private static ThreadLocal loginInfo = new ThreadLocal();

    public static ThreadLocal getLoginInfo() {
        return loginInfo;
    }

    public static void setLoginInfo(ThreadLocal loginInfo) {
        LoginConstant.loginInfo = loginInfo;
    }
}
