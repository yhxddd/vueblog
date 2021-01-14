package com.markerhub.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountProfile implements Serializable {

    //封装可以显示的用户信息
    private String username;
    private String avatar;
    private Long id;
    private String email;
}
