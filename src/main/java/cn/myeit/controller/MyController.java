package cn.myeit.controller;

import cn.hutool.core.codec.Base64;
import cn.myeit.domain.User;
import cn.myeit.util.EmailUtil;
import cn.myeit.util.JsonUtil;
import cn.myeit.util.AutoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 用户的控制层
 */

@Api(value = "my",tags = "用户自己的接口")
@RestController()
@RequestMapping("/my")
public class MyController extends AutoUtil{
    @ApiOperation( value = "获取个人信息", notes = "获取个人信息")
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
        if(cookies != null){
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
}


