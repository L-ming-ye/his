package cn.myeit.controller;

import cn.myeit.domain.User;
import cn.myeit.util.JsonUtil;
import cn.myeit.util.AutoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 用户的控制层
 */

@Api(value = "user",tags = "用户接口")
@RestController()
@RequestMapping("/user")
public class UserController extends AutoUtil{

    @ApiOperation(value = "用户登录",notes = "用户登录")
    @PostMapping("/login")
    public JsonUtil login(String username, String password, String verify, HttpSession session){
        String sessionVerify = (String) session.getAttribute("verify");
        session.setAttribute("verify", null);

        //判断验证码是否正确
//        if(!verify.equals(sessionVerify)){
//            return JsonUtil.error("验证码错误");
//        }
        //验证码正确 查找用户信息
        User user = userService.login(username,password);
        if(user == null){
            //没有查到数据 账号或密码错误
            return JsonUtil.ok("账号或密码错误");
        }else{
            //查到数据 登录成功
            return JsonUtil.ok("登录成功");
        }
    }
}
