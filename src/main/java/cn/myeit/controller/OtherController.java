package cn.myeit.controller;

import cn.myeit.util.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class OtherController {
    @Autowired
    VerifyUtil verifyUtil;

    @GetMapping("/verify")
    public void Verify(HttpServletResponse response, HttpSession session){
        String code = verifyUtil.createVerify(4,4,response);
        session.setAttribute("verify",code);
    }
}
