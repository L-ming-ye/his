package cn.myeit.controller;

import cn.myeit.domain.*;
import cn.myeit.util.AutoUtil;
import cn.myeit.util.SendUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

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
    @GetMapping("/gets")
    public ModelAndView toGets(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/view/my/role/data.html");
        return modelAndView;
    }

    @ApiOperation(value = "获取角色信息",notes = "获取角色信息")
    @PostMapping("/gets")
    public SendUtil gets(Roles roles){
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

    @ApiOperation(value = "获取该角色对应的用户信息",notes = "所有用户在该角色和不在该角色的数据")
    @PostMapping("/get")
    public SendUtil get(Long rid, UserAndRoles userAndRoles){
        //获取数据
        Integer page = userAndRoles.getPage();
        Integer limit = userAndRoles.getLimit();
        //检测数据完整性
        if(rid == null){
            return SendUtil.ok("参数不完整");
        }
        //判断页面和每页数量和助记码的搜索的信息完整性
        if(page == null || page <=0){
            page = 1;//默认
            userAndRoles.setPage(page);
        }

        if(limit == null || limit <= 0){
            limit = 5;//默认
            userAndRoles.setLimit(limit);
        }

        if(userAndRoles.getLikeZjm() == null){
            userAndRoles.setLikeZjm("");
        }

        //开始查询数据
        SendUtil sendUtil = SendUtil.ok();
        List<UserAndRole> data = userAndRoleService.getsUserAndRole(rid,userAndRoles.getLikeZjm(),userAndRoles.getPage(),userAndRoles.getLimit());
        userAndRoles.setData(data);
        //因为是根据用户总数查找的 所以说最大数量 = 用户表可用用户最大数量所以只有查找用户表数量
        Long count = userService.count(userAndRoles.getLikeZjm());
        userAndRoles.setCount(count);
        sendUtil.put("data",userAndRoles);
        return sendUtil;
    }

    @ApiOperation(value = "修改角色信息",notes = "修改角色信息")
    @PostMapping("/change")
    public SendUtil change(Role role,@SessionAttribute("user") User user){
        //将用户数据置入
        role.setUpdateUid(user);
        role.setUpdateTime(new Date(System.currentTimeMillis()));
        System.out.println(role);
        //判断角色信息完整性
        if(role.getRname() == null || role.getStatus() == null){
            return SendUtil.ok("参数不完整");
        }
        Role checkRole = roleService.checkRoleName(role.getRname());
        if(checkRole != null && checkRole.getRid() != role.getRid()){
            //这个名字存在
            return SendUtil.ok("这个角色名字已存在");
        }
        //开始修改角色信息
        Integer count = roleService.change(role);
        if(count == 0){
            return SendUtil.ok("修改失败");
        }else{
            return SendUtil.ok("修改成功");
        }
    }

    @ApiOperation(value = "删除角色",notes = "删除角色")
    @PostMapping("/del")
    public SendUtil del(@RequestBody List<Long> ridBox){
        if(ridBox == null){
            return SendUtil.ok("参数不完整");
        }
        try {
            Long count = roleService.del(ridBox);
            return SendUtil.ok("删除成功");
        }catch (Exception e){
            return SendUtil.ok("删除失败");
        }
    }

    @ApiOperation(value = "将用户移入/移出角色",notes = "将用户移入/移出角色")
    @PostMapping("/move")
    public SendUtil move(Long rid,String zjm,Boolean status){
        //rid角色id zjm用户助记码 status状态(true移入 false移出)
        if(rid == null || zjm == null || status == null){
            return SendUtil.ok("参数不完整");
        }
        //根据zjm获取用户
        User user = userService.findUser(zjm);
        if(user == null){
            return SendUtil.ok("找不到用户");
        }
        //找到用户了 调用移动(移入/移出)的方法
        Long count = userAndRoleService.move(rid, user.getUid(), status);
        if(count == 1){
            return SendUtil.ok("操作成功");
        }else{
            return SendUtil.ok("操作失败");
        }
    }
}
