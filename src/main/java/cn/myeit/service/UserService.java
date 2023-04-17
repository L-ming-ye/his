package cn.myeit.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.myeit.domain.User;
import cn.myeit.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;
    public User login(String uname,String password){
        //加密密码
        password = SecureUtil.md5(password);
        return userMapper.findUserByUsernameAndPassword(uname,password);
    }
}
