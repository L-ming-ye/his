package cn.myeit.controller;


import cn.myeit.util.JsonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ErrorController {
    @ResponseBody
    @ExceptionHandler({Exception.class})
    public JsonUtil errorHandle(Exception e){
        e.printStackTrace();
        System.out.println("出现异常了：" + e);
        JsonUtil error = JsonUtil.error();
        return error;
    }

}
