package com.markerhub.shiro;

import com.markerhub.entity.User;
import com.markerhub.service.UserService;
import com.markerhub.util.JwtUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//登录逻辑：AuthenticationInfo
//身份验证  密码校验
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        //return super.supports(token);
        //判断token是否是jwttoken  如果是 下面进行强转
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        JwtToken jwtToken = (JwtToken) token;

        String userId = jwtUtil.getClaimByToken((String)jwtToken.getPrincipal()).getSubject();
        User user = userService.getById(Long.valueOf(userId));

        if(user == null){
            throw new UnknownAccountException("用户不存在");
        }

        if(user.getStatus()==-1){
            throw new LockedAccountException("账户被锁定");
        }

        AccountProfile profile = new AccountProfile();
        BeanUtils.copyProperties(user,profile);

        return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());
    }
}
