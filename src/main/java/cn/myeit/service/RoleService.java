package cn.myeit.service;

import cn.myeit.domain.Role;
import cn.myeit.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
