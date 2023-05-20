package cn.myeit.controller;

import cn.myeit.domain.User;
import cn.myeit.domain.Users;
import cn.myeit.util.AutoUtil;
import cn.myeit.util.SendUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public SendUtil add(User user, @SessionAttribute("user") User createUser){
        //如果session为空 >> 没登陆 >> 直接返回
        if(createUser == null){
            return SendUtil.ok("请登录");
        }
        //判断防止数据为空
        if(user.getUname() == null || "".equals(user.getUname())){
            return SendUtil.ok("请输入用户名");
        }else if (user.getAge() == null){
            return SendUtil.ok("请输入年龄");
        }else if (user.getSex() == null || (user.getSex() != 0 && user.getSex() != 1)) {
            return SendUtil.ok("请选择性别");
        }else if (user.getZjm() == null || "".equals(user.getZjm())) {
            return SendUtil.ok("请输入助记码");
        } else if (user.getEmail() == null || "".equals(user.getEmail())) {
            return SendUtil.ok("请输入邮箱");
        }
        //判断zjm/邮箱 是否重复
        List<User> users = userService.findjmAndEmail(user.getZjm(), user.getEmail());
        if(users.size()>0){
            for(User u:users){
                if(user.getZjm().equals(u.getZjm())){
                    return SendUtil.ok("助记码已存在");
                }else if(user.getEmail().equals(u.getEmail())){
                    return SendUtil.ok("邮箱已存在");
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
            return SendUtil.ok("创建用户成功");
        }else{
            return SendUtil.ok("创建用户失败");
        }
    }

    @ApiOperation(value="展示用户数据页面", notes = "展示用户数据页面")
    @GetMapping("/get")
    public ModelAndView toGet(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/view/my/user/data.html");
        return modelAndView;
    }



    @ApiOperation(value = "获取用户数据",notes = "获取用户数据")
    @PostMapping("/get")
    public SendUtil get(Users users){
        //检查每页几条数据的参数是否正常
        Integer limit = users.getLimit();
        if(limit == null || limit <= 0){
            //为空或小于等于0 给个默认值 5
            limit = 5;
        }
        //检查页码是否正确
        Integer page = users.getPage();
        if(page == null || page <= 0){
            page = 1;//默认第一页
        }

        //获取用户数据总数
        Long count = userService.count();
        if(count == null)count = 0L;
 
        //将值存入users
        //获取users
        users.setData(userService.getUsers(page,limit));
        users.setPage(page);
        users.setLimit(limit);
        users.setCount(count);
        SendUtil sendUtil = SendUtil.ok();
        sendUtil.put("data",users);
        return sendUtil;
    }

    @ApiOperation(value = "修改用户信息",notes = "修改用户信息")
    @PostMapping("/change")
    public SendUtil change(User user ,String password,@SessionAttribute("user") User updateUser){
        //重新修改一下用户的修改人为发送请求的人 修改时间为当前时间
        user.setUpdateUid(updateUser);
        user.setUpdateTime(new Date(System.currentTimeMillis()));
        //判断zjm/邮箱 是否重复
        List<User> users = userService.findjmAndEmail(user.getZjm(), user.getEmail());
        if(users.size()>0){
            for(User u:users){
                if(user.getZjm().equals(u.getZjm())){
                    return SendUtil.ok("助记码已存在");
                }else if(user.getEmail().equals(u.getEmail())){
                    return SendUtil.ok("邮箱已存在");
                }
            }
        }
        //获取到 user 和 password 修改用户信息
        Integer count = userService.changeUser(user, password);
        if(count == 1){
            //修改成功
            return SendUtil.ok("修改成功");
        }else{
            return SendUtil.ok("修改失败");
        }
    }

    @ApiOperation(value = "删除用户",notes = "删除用户")
    @PostMapping("/del")
    public SendUtil del(@RequestBody List<Long> uidBox){

        if (uidBox == null){
            return SendUtil.ok("数据不完整");
        }
        try {
            Long del = userService.del(uidBox);
            return SendUtil.ok("删除成功");
        }catch (Exception e){
            return SendUtil.ok("删除失败");
        }
    }
}
