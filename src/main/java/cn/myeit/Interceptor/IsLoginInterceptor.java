package cn.myeit.Interceptor;

import cn.myeit.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class IsLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user != null){
            //登录了直接跳转到后台控制台
            response.sendRedirect("/his/view/my/desktop.html");
            return false;
        }
        return true;
    }
}
