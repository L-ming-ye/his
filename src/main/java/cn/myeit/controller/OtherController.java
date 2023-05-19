package cn.myeit.controller;

import cn.hutool.core.codec.Base64;
import cn.myeit.domain.User;
import cn.myeit.util.AutoUtil;
import cn.myeit.util.EmailUtil;
import cn.myeit.util.JsonUtil;
import cn.myeit.util.VerifyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api(value = "other",tags = "其他的接口")
@Controller
public class OtherController extends AutoUtil {

    @ApiOperation(value="获取验证码",notes = "获取验证码")
    @GetMapping("/verify")
    public void Verify(HttpServletResponse response, HttpSession session){
        String code = verifyUtil.createVerify(4,4,response);
        session.setAttribute("verify",code);
    }

    @ApiOperation(value = "用户找回密码",notes = "用户找回密码发送邮件")
    @PostMapping("/find")
    @ResponseBody
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
    @ResponseBody
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

    @ApiOperation(value = "用户登录",notes = "用户登录")
    @PostMapping("/login")
    @ResponseBody
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

    @ApiOperation(value = "判断是否登录",notes = "没登录显示请登录弹出跳转登录 登录了跳转desktop")
    @GetMapping("/toLogin")
    @ResponseBody
    public void toLogin(HttpServletRequest request,HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String result;
        if(user == null){
            result = "<html><head><meta charset=\"UTF-8\"><script>alert('请登录');window.location.href='/his/view/login.html';</script></head></html>";
        }else {
            result = "<html><head><meta charset=\"UTF-8\"><script>window.location.href='/his/view/user/desktop.html';</script></head>/html>";
        }
        response.getWriter().println(result);
    }

    @GetMapping("/test")
    @ResponseBody
    public List<User> test(String user){
        System.out.println(user);
        return userService.users(user);
    }
}
