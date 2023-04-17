package cn.myeit.util;

import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求返回的json工具类
 */
@ApiModel(value = "JsonUtil" , description = "请求返回的json工具类")
public class JsonUtil extends HashMap<String,Object>{
    public JsonUtil() {
        put("code",0);
        put("msg","success");
    }

    public static JsonUtil ok(){
        return new JsonUtil();
    }

    public static JsonUtil ok(Map<String,Object> map){
        JsonUtil jsonUtil = new JsonUtil();
        jsonUtil.putAll(map);
        return jsonUtil;
    }

    public static JsonUtil ok(int code,String msg){
        JsonUtil jsonUtil = new JsonUtil();
        jsonUtil.put("code",code);
        jsonUtil.put("msg",msg);
        return jsonUtil;
    }

    public static JsonUtil ok(String msg){
        JsonUtil jsonUtil = new JsonUtil();
        jsonUtil.put("msg",msg);
        return jsonUtil;
    }

    public static <T> JsonUtil data(T data){
        JsonUtil jsonUtil = new JsonUtil();
        jsonUtil.put("data",data);
        return jsonUtil;
    }

    public static JsonUtil error(){
        return error(500,"程序出现异常");
    }

    public static JsonUtil error(Map<String,Object> map){
        JsonUtil jsonUtil = error();
        jsonUtil.putAll(map);
        return jsonUtil;
    }

    public static JsonUtil error(String msg){
        JsonUtil jsonUtil = JsonUtil.error();
        jsonUtil.put("msg",msg);
        return jsonUtil;
    }

    public static JsonUtil error(int code,String msg){
        JsonUtil jsonUtil = new JsonUtil();
        jsonUtil.put("code",code);
        jsonUtil.put("msg",msg);
        return jsonUtil;
    }

}
