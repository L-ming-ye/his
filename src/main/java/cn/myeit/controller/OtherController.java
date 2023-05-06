package cn.myeit.controller;

import cn.myeit.domain.User;
import cn.myeit.util.AutoUtil;
import cn.myeit.util.JsonUtil;
import cn.myeit.util.VerifyUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class OtherController extends AutoUtil {

    @ApiOperation(value="获取验证码",notes = "获取验证码")
    @GetMapping("/verify")
    public void Verify(HttpServletResponse response, HttpSession session){
        String code = verifyUtil.createVerify(4,4,response);
        session.setAttribute("verify",code);
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

}
