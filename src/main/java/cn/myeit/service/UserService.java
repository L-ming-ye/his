package cn.myeit.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.myeit.domain.User;
import cn.myeit.mapper.UserMapper;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.PAData;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    /**
     * 用户登录
     * @param uname
     * @param password
     * @return
     */
    public User login(String uname,String password){
        //加密密码
        password = SecureUtil.md5(password);
        return userMapper.findUserByUsernameAndPassword(uname,password);
    }

    /**
     * 用户找回密码发送邮件
     */
    public User find(String username){
        return userMapper.findUserByZjmOrEmail(username);
    }

    /**
     * 修改密码
     */
    public Integer changePass(Long uid,String password){
        password = SecureUtil.md5(password);
        System.out.println(password);
        //将加密后的密码传入
        return userMapper.UpdateUserPasswordByUid(uid,password);
    }

}
