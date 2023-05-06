package cn.myeit.controller;

import cn.hutool.core.codec.Base64;
import cn.myeit.domain.User;
import cn.myeit.util.EmailUtil;
import cn.myeit.util.JsonUtil;
import cn.myeit.util.AutoUtil;
import cn.myeit.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 用户的控制层
 */

@Api(value = "user",tags = "用户接口")
@RestController()
@RequestMapping("/user")
public class UserController extends AutoUtil{

    @ApiOperation(value = "用户登录",notes = "用户登录")
    @PostMapping("/login")
    public JsonUtil login(String username, String password, String verify, Boolean auto, HttpSession session){
        String sessionVerify = (String) session.getAttribute("verify");
        session.setAttribute("verify", null);

        //判断验证码是否正确
        if(verify == null || sessionVerify == null || !verify.toLowerCase().equals(sessionVerify.toLowerCase())){
            return JsonUtil.error("验证码错误");
        }
        //验证码正确 查找用户信息
        User user = userService.login(username,password);
        if(user == null){
            //没有查到数据 账号或密码错误
            return JsonUtil.ok("账号或密码错误");
        }else{
            //查到数据 判断用户的状态
            Integer status = user.getStatus();
            if(status == 0){
                session.setAttribute("user",user);
                //判断是否有自动登录 7天过期
                JsonUtil jsonUtil = JsonUtil.ok("登录成功");
                if(auto){
                    //创建一个唯一标识
                    String uuid = UUID.randomUUID().toString().replaceAll("-","");
                    uuid = Base64.encode(uuid + "-" + System.currentTimeMillis());
                    //将数据写入redis里 并设置7天过期
                    Jedis redis = redisUtil.open();
                    redis.select(2);
                    redis.hset(uuid,"username",username);
                    redis.hset(uuid,"password",password);
                    redis.expire(uuid,6048000);
                    if(redis != null){
                        redis.close();
                    }
                    jsonUtil.put("flag",uuid);
                }
                return jsonUtil;
            }else if(status == 1) {
                return JsonUtil.ok("账号被封禁");
            }else{
                return JsonUtil.ok("用户状态异常");
            }
        }
    }

    @ApiOperation(value = "用户找回密码",notes = "用户找回密码发送邮件")
    @PostMapping("/find")
    public JsonUtil find(String username){
        User user = userService.findUsers(username);
        //判断是否查询到用户
        if(user == null){
            //没有数据
            return JsonUtil.ok("没有找到这个用户");
        }else{
            //查到数据 判断用户的状态
            Integer status = user.getStatus();
            if(status == 0){
                //状态正常 判断用户是否有邮箱
                if(user.getEmail() == null){
                    return JsonUtil.ok("用户没有绑定邮箱");
                }else {
                    //绑定了邮箱 判断一分钟内是否有发送 如果有返回发送频繁
                    Jedis jedis = null;
                    String flag = null;
                    try{
                        jedis = redisUtil.open();
                        //连接到了数据库
                        while (true){
                            //抢锁
                            jedis.select(1);
                            flag = String.valueOf(System.currentTimeMillis());
                            long verifyFlag = jedis.setnx("verifyFlag", flag);
                            if(verifyFlag == 1){
                                //抢到锁了
                                jedis.expire("verifyFlag",60);//设置过期时间60秒
                                break;
                            }else{
                                Thread.sleep(1000);//暂停一秒在抢锁
                            }
                        }
                        jedis.select(0);
                        //查询redis获取这个用户对应的数据
                        long time = jedis.ttl(user.getEmail());
                        //查询上一次请求的时间判断是否是一分钟内发送
                        if(time >= 1740){
                            //超时 一分钟内发送多次
                            return JsonUtil.ok("请求频繁");
                        }
                        // 都没问题 发送邮箱
                        //生成验证码
                        String verifyCode = verifyUtil.createVerifyCode(6);
                        //将验证码存入缓存
                        jedis.hset(user.getEmail(),"verifyCode",verifyCode);
                        jedis.hset(user.getEmail(),"uid",user.getUid().toString());
                        //有效时间30分钟
                        jedis.expire(user.getEmail(),1800);
                        EmailUtil.send(user.getEmail(),"找回密码","<h2>你的验证码为：<font color=\"#ff0000\" style=\"border-bottom:3px solid #f00\">"+verifyCode+"</font> 有效时间为30分钟  </h2>",true);
                        return JsonUtil.ok("发送成功");
                    }catch (Exception e){
                        //发送邮件失败
                        return JsonUtil.ok("发送邮件失败");
                    }finally {
                        jedis.select(1);
                        if(flag != null && flag.equals(jedis.get("verifyFlag"))){
                            //如果时间撮还是一样就是同一个锁 释放
                            jedis.del("verifyFlag");
                        }
                        //如果连接不为空 则关闭
                        if (jedis != null){
                            jedis.close();
                        }

                    }
                }
            }else if(status == 1) {
                return JsonUtil.ok("账号被封禁");
            }else{
                return JsonUtil.ok("用户状态异常");
            }
        }
    }

    @ApiOperation(value = "找回密码的修改密码",notes = "找回密码的修改密码")
    @PostMapping("/change")
    public JsonUtil change(String username, String password, String checkPassword, String verify){
        //判断两次密码是否不一致
        if(password == null || !password.equals(checkPassword)){
            return JsonUtil.ok("两次密码不一致");
        }
        Jedis jedis = null;
        try{
            //根据助记码/邮箱查询用户信息
            User user = userService.findUsers(username);
            //判断是否查询到用户
            if(user == null){
                //没有数据
                return JsonUtil.ok("没有找到这个用户");
            }else {
                //查到数据 判断用户的状态
                Integer status = user.getStatus();
                if (status == 0) {
                    //状态正常 判断用户是否有邮箱
                    if(user.getEmail() == null){
                        return JsonUtil.ok("用户没有绑定邮箱");
                    }else {
                        //打开reids 查询邮箱对应的数据
                        jedis = redisUtil.open();
                        jedis.select(0);
                        Map<String, String> userVerifyData = jedis.hgetAll(user.getEmail());
                        if(userVerifyData == null){
                            //该用户的数据是空的 （可能过期了 也可能压根没有数据）返回验证码错误 让用户重新获取一遍
                            return JsonUtil.ok("验证码错误");
                        }
                        //有这个用户的数据
                        String verifyCode = userVerifyData.get("verifyCode");
                        //判断传入进来的验证码 和 redis数据库里的验证码是否对应
                        if(verifyCode == null || verify ==null || !verifyCode.toLowerCase().equals(verify.toLowerCase())){
                            //不对应 验证码不正确
                            return JsonUtil.ok("验证码错误");
                        }
                        //验证码正确 根据redis对应的uid修改密码
                        Long uid = new Long(userVerifyData.get("uid"));
                        Integer i = userService.changePass(uid, password);
                        if(i == 1){
                            //修改成功后删除用户在redis验证码的信息
                            jedis.del(user.getEmail());
                            return JsonUtil.ok("修改成功");
                        }else{
                            return JsonUtil.ok("修改失败");
                        }
                    }
                } else if (status == 1) {
                    return JsonUtil.ok("账号被封禁");
                } else {
                    return JsonUtil.ok("用户状态异常");
                }
            }
        }catch (Exception e){   
            return JsonUtil.error();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    @ApiOperation(value="展示新增用户功能页面", notes = "展示新增用户功能页面")
    @GetMapping("/add")
    public ModelAndView toAdd(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/view/user/add.html");
        return modelAndView;
    }

    @ApiOperation(value = "新增用户功能" ,notes="新增用户功能")
    @PostMapping("/add")
    public JsonUtil add(User user,HttpSession session){

        User createUser = (User) session.getAttribute("user");
//        System.out.println(createUser);
        //如果session为空 >> 没登陆 >> 直接返回
        if(createUser == null){
            return JsonUtil.ok("请登录");
        }
        //判断防止数据为空
        if(user.getUname() == null || "".equals(user.getUname())){
            return JsonUtil.ok("请输入用户名");
        }else if (user.getAge() == null){
            return JsonUtil.ok("请输入年龄");
        }else if (user.getSex() == null || (user.getSex() != 0 && user.getSex() != 1)) {
            return JsonUtil.ok("请选择性别");
        }else if (user.getZjm() == null || "".equals(user.getZjm())) {
            return JsonUtil.ok("请输入助记码");
        } else if (user.getEmail() == null || "".equals(user.getEmail())) {
            return JsonUtil.ok("请输入邮箱");
        }
        //判断zjm/邮箱 是否重复
        List<User> users = userService.changeZjmAndEmail(user.getZjm(), user.getEmail());
        if(users.size()>0){
            for(User u:users){
                if(user.getZjm().equals(u.getZjm())){
                    return JsonUtil.ok("助记码已存在");
                }else if(user.getEmail().equals(u.getEmail())){
                    return JsonUtil.ok("邮箱已存在");
                }
            }
        }

        //登录了 将数据全部装入
        user.setCreateTime(new Date(System.currentTimeMillis()));
        User u = new User();
        u.setUid(createUser.getUid());
        user.setCreateUid(u);
        //将user传入service 新增
        Integer count = userService.addUser(user);
        //如果大于0则新增成功
        if(count > 0){
            return JsonUtil.ok("创建用户成功");
        }else{
            return JsonUtil.ok("创建用户失败");
        }
    }

    @ApiOperation( value = "获取用户信息", notes = "获取用户信息")
    @GetMapping("/get")
    public JsonUtil getUser(HttpSession session){
        User user = (User) session.getAttribute("user");
        //判断是否登录
        if(user == null){
            return JsonUtil.ok("请登录");
        }
        JsonUtil jsonUtil = new JsonUtil();
        jsonUtil.put("data",user);
        return jsonUtil;
    }

    @ApiOperation(value = "退出登录", notes = "退出登录")
    @GetMapping("/exit")
    public JsonUtil exit(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            //如果有自动登录的cookie删除对应的cookie和redis
            if("auto".equals(cookie.getName())){
                Jedis open = redisUtil.open();
                open.select(2);
                open.del(cookie.getValue());
                if(open != null){
                    open.close();
                }
            }
        }
        HttpSession session = request.getSession();
        session.removeAttribute("user") ;
        return JsonUtil.ok("退出成功");
    }

    @ApiOperation(value = "用户使用原密码的修改密码", notes = "用户使用原密码的修改密码")
    @PostMapping("/update")
    public JsonUtil update(HttpSession session,String oldPassword,String password,String checkPassword){
        User user = (User) session.getAttribute("user");
        //判断是否登录
        if(user == null){
            return JsonUtil.ok("请登录");
        }
        //检查数据的完整性
        if(oldPassword == null || "".equals(oldPassword)){
            return JsonUtil.ok("请输入旧密码");
        }
        if(password == null || "".equals(password)){
            return JsonUtil.ok("请输入新密码");
        }
        if(checkPassword == null || "".equals(checkPassword)){
            return JsonUtil.ok("请输入确认密码");
        }
        //判断新密码的两次密码是否相同
        if(!checkPassword.equals(password)){
            return JsonUtil.ok("两次密码不一样");
        }
        //判断旧密码是否正确
        User checkUser = userService.login(user.getZjm(), oldPassword);
        if(checkUser == null){
            return JsonUtil.ok("原密码错误");
        }
        Integer count = userService.changePass(user.getUid(),password);
        if(count>0){
            return JsonUtil.ok("修改成功");
        }else{
            return JsonUtil.ok("修改失败");
        }
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}


