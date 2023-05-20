package cn.myeit.controller;


import cn.myeit.util.SendUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ErrorController {
    @ResponseBody
    @ExceptionHandler({Exception.class})
    public SendUtil errorHandle(Exception e){
        e.printStackTrace();
        System.out.println("出现异常了：" + e);
        SendUtil error = SendUtil.error();
        return error;
    }

}
