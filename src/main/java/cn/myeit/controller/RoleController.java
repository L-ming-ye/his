package cn.myeit.controller;

import cn.myeit.domain.Role;
import cn.myeit.domain.Roles;
import cn.myeit.domain.User;
import cn.myeit.util.AutoUtil;
import cn.myeit.util.SendUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Api(value = "role",tags = "角色接口")
@RestController
@RequestMapping("/role")
public class RoleController extends AutoUtil {

    @ApiOperation(value = "展示角色新增页面",notes = "展示角色新增页面")
    @GetMapping("/add")
    public ModelAndView toAdd(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/view/my/role/add.html");
        return modelAndView;
    }

    @ApiOperation(value = "添加角色",notes = "添加角色")
    @PostMapping("/add")
    public SendUtil add(Role role, @SessionAttribute("user") User user){
        if (role == null && (role.getRname() == null || "".equals(role.getRname())) ){
            return SendUtil.ok("参数不完整");
        }

        //判断是否有相同的角色名 且没有删除的
        if(roleService.checkRoleName(role.getRname()) != null){
            //这个角色名字已存在直接返回
            return SendUtil.ok("这个角色名字已存在了");
        }

        //开始添加用户
        role.setCreateTime(new Date(System.currentTimeMillis()));
        role.setCreateUid(user);
        role.setStatus(0);
        Integer count = roleService.add(role);
        if(count == 1){
            return SendUtil.ok("创建成功");
        }else{
            return SendUtil.ok("创建失败");
        }
    }

    @ApiOperation(value = "展示角色数据页面",notes = "展示角色数据页面")
    @GetMapping("/get")
    public ModelAndView toGet(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/view/my/role/data.html");
        return modelAndView;
    }

    @ApiOperation(value = "获取角色信息",notes = "获取角色信息")
    @PostMapping("/get")
    public SendUtil get(Roles roles){
        //响应的工具
        SendUtil sendUtil = SendUtil.ok();
        //获取页码
        if(roles.getPage() == null || roles.getPage() <= 0){//如果页码为空或小于零
            //默认第一页
            roles.setPage(1);
        }
        //获取每页显示几个
        if(roles.getLimit() == null || roles.getLimit() <= 0){//如果每页数量为空 或小于等于0
            //默认5条
            roles.setLimit(5);
        }
        //获取数据总量
        Long count = roleService.count();
        roles.setCount(count);
        //判断是否有数据 有数据才查找数据
        if(count != null && count > 0){
            //有数据查找数据
            roles.setData(roleService.getRoles(roles.getPage(), roles.getLimit()));
        }
        sendUtil.put("data",roles);
        return sendUtil;
    }

}
