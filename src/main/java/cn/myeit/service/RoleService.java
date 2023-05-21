package cn.myeit.service;

import cn.myeit.domain.Role;
import cn.myeit.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleService {
    @Resource
    RoleMapper roleMapper;
    public Integer add(Role role){
        return roleMapper.insertRole(role);
    }


    /**
     * 检查名字
     */
    public Role checkRoleName(String name){
        return roleMapper.findRoleByRnameAndStatus(name);
    }

    /**
     * 获取数据总数
     */
    public Long count(){
        return roleMapper.findCount();
    }

    /**
     *获取角色数据
     */
    public List<Role> getRoles(Integer page, Integer limit){
        Integer statr = (page-1)*limit;
        Integer end = statr + limit;
        return roleMapper.findRolesByStatusAndLimit(statr,end);
    }
}
