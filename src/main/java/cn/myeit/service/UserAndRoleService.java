package cn.myeit.service;

import cn.myeit.domain.UserAndRole;
import cn.myeit.domain.UserAndRoles;
import cn.myeit.mapper.UserAndRoleMapper;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserAndRoleService {
    @Resource
    UserAndRoleMapper userAndRoleMapper;

    public List<UserAndRole> getsUserAndRole(Long rid, String likeZjm, Integer page, Integer limit) {
        Integer statr = (page-1)*limit;
        Integer end = statr + limit;
        likeZjm = "%"+likeZjm+"%";
        return userAndRoleMapper.findAll(rid,likeZjm,statr,end);
    }

    public Long move(Long rid,Long uid,boolean status){
        if(status){
            //移入
            return userAndRoleMapper.insertUserAndRole(uid, rid);
        } else{
            return userAndRoleMapper.delectUserAndRoleByUidAndRid(uid,rid);
        }
    }
}
