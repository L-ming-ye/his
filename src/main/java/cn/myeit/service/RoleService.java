package cn.myeit.service;

import cn.myeit.domain.Role;
import cn.myeit.mapper.RoleMapper;
import cn.myeit.mapper.UserAndRoleMapper;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 修改角色信息
     */
    public Integer change(Role role){
        return roleMapper.updateRnameAndUpdateByRid(role);
    }

    /**
     * 删除单个角色
     */
    @Transactional(rollbackFor = Exception.class)
    public Long del(List<Long> ridBox){
        Long count = roleMapper.delRoleByRid(ridBox);
        if(count != ridBox.size()){
            //数量不同 回滚返回-1
            throw new RuntimeException("删除失败");
        }else{
            //一样删除成功 返回数量
            return count;
        }
    }
}
