package cn.myeit.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.myeit.domain.User;
import cn.myeit.mapper.UserMapper;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.PAData;

import javax.annotation.Resource;
import java.util.List;

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
    public User findUsers(String username){
        return userMapper.findUserByZjmOrEmail(username);
    }


    /**
     * 根据uid修改密码
     * @param uid
     * @param password
     * @return
     */
    public Integer changePass(Long uid,String password){
        //将加密后的密码传入
        return userMapper.updateUserPasswordByUid(uid,SecureUtil.md5(password));
    }


    public Integer addUser(User user){
        String password = "123456";//初始密码
        password = SecureUtil.md5(password);
        return userMapper.insertUser(user,password);
    }

    public List<User> changeZjmAndEmail(String zjm,String email){
        return userMapper.findUserByZjmAndEmail(zjm,email);
    }

}
