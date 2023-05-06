package cn.myeit.Interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.myeit.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 判断是否登录的拦截器
 */
public class ToLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("判断登录拦截器");
        HttpSession session = request.getSession();
        //判断是否登录
        User user = (User) session.getAttribute("user");
        //没登录 跳转到登录页面 去登录
        if(user == null){
            response.sendRedirect("/his/toLogin");
            return false;
        }
        return true;
    }
}
