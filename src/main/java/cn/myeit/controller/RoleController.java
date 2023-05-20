package cn.myeit.controller;

import cn.myeit.domain.Role;
import cn.myeit.domain.User;
import cn.myeit.util.AutoUtil;
import cn.myeit.util.SendUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(value = "role",tags = "角色接口")
@RestController
@RequestMapping("/role")
public class RoleController extends AutoUtil {

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


}
