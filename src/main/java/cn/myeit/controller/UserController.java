package cn.myeit.controller;

import cn.myeit.domain.User;
import cn.myeit.util.AutoUtil;
import cn.myeit.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Api(value = "用户接口",tags = "用户接口")
@RestController()
@RequestMapping("/user")
public class UserController extends AutoUtil {
    @ApiOperation(value="展示新增用户功能页面", notes = "展示新增用户功能页面")
    @GetMapping("/add")
    public ModelAndView toAdd(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/view/my/user/add.html");
        return modelAndView;
    }

    @ApiOperation(value = "新增用户功能" ,notes="新增用户功能")
    @PostMapping("/add")
    public JsonUtil add(User user, HttpSession session){

        User createUser = (User) session.getAttribute("user");
//        System.out.println(createUser);
        //如果session为空 >> 没登陆 >> 直接返回
        if(createUser == null){
            return JsonUtil.ok("请登录");
        }
        //判断防止数据为空
        if(user.getUname() == null || "".equals(user.getUname())){
            return JsonUtil.ok("请输入用户名");
        }else if (user.getAge() == null){
            return JsonUtil.ok("请输入年龄");
        }else if (user.getSex() == null || (user.getSex() != 0 && user.getSex() != 1)) {
            return JsonUtil.ok("请选择性别");
        }else if (user.getZjm() == null || "".equals(user.getZjm())) {
            return JsonUtil.ok("请输入助记码");
        } else if (user.getEmail() == null || "".equals(user.getEmail())) {
            return JsonUtil.ok("请输入邮箱");
        }
        //判断zjm/邮箱 是否重复
        List<User> users = userService.changeZjmAndEmail(user.getZjm(), user.getEmail());
        if(users.size()>0){
            for(User u:users){
                if(user.getZjm().equals(u.getZjm())){
                    return JsonUtil.ok("助记码已存在");
                }else if(user.getEmail().equals(u.getEmail())){
                    return JsonUtil.ok("邮箱已存在");
                }
            }
        }

        //登录了 将数据全部装入
        user.setCreateTime(new Date(System.currentTimeMillis()));
        User u = new User();
        u.setUid(createUser.getUid());
        user.setCreateUid(u);
        //将user传入service 新增
        Integer count = userService.addUser(user);
        //如果大于0则新增成功
        if(count > 0){
            return JsonUtil.ok("创建用户成功");
        }else{
            return JsonUtil.ok("创建用户失败");
        }
    }
}
