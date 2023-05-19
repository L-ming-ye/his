package cn.myeit.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.myeit.domain.User;
import cn.myeit.mapper.UserMapper;
import org.springframework.stereotype.Service;

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

    /**
     * 添加用户
     * @param user
     * @return
     */
    public Integer addUser(User user){
        String password = "123456";//初始密码
        password = SecureUtil.md5(password);
        return userMapper.insertUser(user,password);
    }

    /**
     * 查找zjm或邮箱相同的
     * @param zjm
     * @param email
     * @return
     */
    public List<User> findjmAndEmail(String zjm,String email){
        return userMapper.findUserByZjmAndEmail(zjm,email);
    }

    /**
     * 获取用户数据总条数
     * @return
     */
    public Long count(){
        return userMapper.findCountByUser();
    }

    public List<User> getUsers(Integer page, Integer limit){
        Integer statr = (page-1)*limit;
        Integer end = statr + limit;
        return userMapper.findUserByLimit(statr,end);
    }

    public List<User> users(String user){
        return userMapper.findUsers(user,1,2);
    }

    public Integer changeUser(User user,String password){
        //如果password为"" 直接变成null 为后续更好的做动态sql修改密码
        if (password != null && "".equals(password)){
            password = null;
        }else{
            //密码不为空加密
            password = SecureUtil.md5(password);
        }
        return userMapper.changeUser(user,password);
    }
}
