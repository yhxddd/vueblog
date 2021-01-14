package com.markerhub.util;

import com.markerhub.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

//工具类
public class ShiroUtil {

    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
