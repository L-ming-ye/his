package cn.myeit.util;

import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求返回的json工具类
 */
@ApiModel(value = "SendUtil" , description = "请求返回的json工具类")
public class SendUtil extends HashMap<String,Object>{
    public SendUtil() {
        put("code",0);
        put("msg","success");
    }

    public static SendUtil ok(){
        return new SendUtil();
    }

    public static SendUtil ok(Map<String,Object> map){
        SendUtil sendUtil = new SendUtil();
        sendUtil.putAll(map);
        return sendUtil;
    }

    public static SendUtil ok(int code,String msg){
        SendUtil sendUtil = new SendUtil();
        sendUtil.put("code",code);
        sendUtil.put("msg",msg);
        return sendUtil;
    }

    public static SendUtil ok(String msg){
        SendUtil sendUtil = new SendUtil();
        sendUtil.put("msg",msg);
        return sendUtil;
    }

    public static <T> SendUtil data(T data){
        SendUtil sendUtil = new SendUtil();
        sendUtil.put("data",data);
        return sendUtil;
    }

    public static SendUtil error(){
        return error(500,"程序出现异常");
    }

    public static SendUtil error(Map<String,Object> map){
        SendUtil sendUtil = error();
        sendUtil.putAll(map);
        return sendUtil;
    }

    public static SendUtil error(String msg){
        SendUtil sendUtil = SendUtil.error();
        sendUtil.put("msg",msg);
        return sendUtil;
    }

    public static SendUtil error(int code,String msg){
        SendUtil sendUtil = new SendUtil();
        sendUtil.put("code",code);
        sendUtil.put("msg",msg);
        return sendUtil;
    }

    public static String alert(String msg){
        return "<head><meta charset='utf-8'><script>alert('"+msg+"')</script></head>";
    }

}
