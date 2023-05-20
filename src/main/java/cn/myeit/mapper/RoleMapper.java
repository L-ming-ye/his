package cn.myeit.mapper;

import cn.myeit.domain.Role;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;

public interface RoleMapper {


    @Select("SELECT rid,rname,create_uid,create_time,update_uid,update_time,status FROM role WHERE rname=#{rname} and status in (0,1);")
    @Results({
            @Result(column = "rid",property = "rid"),
            @Result(column = "rname",property = "rname"),
            @Result(column = "create_uid",property = "createUid",one = @One(select = "cn.myeit.mapper.UserMapper.findUserByUid")),
            @Result(column = "create_time",property = "createTime"),
            @Result(column = "update_uid",property = "updateUid",one = @One(select = "cn.myeit.mapper.UserMapper.findUserByUid")),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "status",property = "status")
    })
    Role findRoleByRnameAndStatus(String name);

    @Insert("INSERT role(rname,create_uid,create_time,status) VALUE(#{role.rname},#{role.createUid.uid},#{role.createTime},#{role.status});")
    Integer insertRole(@Param("role") Role role);
}
