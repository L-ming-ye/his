package cn.myeit.Interceptor;

import cn.myeit.domain.User;
import cn.myeit.service.UserService;
import cn.myeit.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 自动登录的拦截器
 */
public class AutoLoginInterceptor implements HandlerInterceptor {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserService userService;
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("自动登录拦截器");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String uuid = null;
        if(user == null){
            //获取cookie数组
            //遍历数组
            Cookie[] cookies = request.getCookies();
            for(Cookie cookie:cookies){
                if("auto".equals(cookie.getName())){
                    uuid = cookie.getValue();
                    break;
                }
            }

            if(uuid == null){
                //没有uuid直接结束
                return true;
            }
            //解码 防止cookie特殊符号
            uuid = URLDecoder.decode(uuid);
            //如果有uuid去redis数据库寻找
            Jedis redis = redisUtil.open();
            redis.select(2);
            String username = redis.hget(uuid,"username");
            String password = redis.hget(uuid,"password");
            //关闭redis
            if(redis != null){
                redis.close();
            }
            //判断是否查找到了
            if(username == null || password == null){
                //如果为空则直接返回
                return true;
            }
            user = userService.login(username,password);
            if(user != null){
                //登录成功了 将user存入session
                session.setAttribute("user",user);
            }
        }
        return true;
    }
}
