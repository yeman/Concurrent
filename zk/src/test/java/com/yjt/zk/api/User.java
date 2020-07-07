package com.yjt.zk.api;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @className User
 * @description TODO
 * @author YM
 * @date 2020-07-06 18:40
 * @version V1.0
 * @since 1.0
 **/
@Data
@Builder
@Accessors(chain = true)
public class User implements Serializable {
    private String userName;
    private String userPass;
}
