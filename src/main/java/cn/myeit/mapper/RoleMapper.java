package cn.myeit.mapper;

import cn.myeit.domain.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Select("SELECT rid,rname,create_uid,create_time,update_uid,update_time,status FROM role WHERE rid=#{rid} and status in (0,1);")
    Role findRoleByRid(Long rid);

    @Insert("INSERT role(rname,create_uid,create_time,status) VALUE(#{role.rname},#{role.createUid.uid},#{role.createTime},#{role.status});")
    Integer insertRole(@Param("role") Role role);

    @Select("SELECT count(*) FROM role WHERE status IN (0,1)")
    Long findCount();

    @Results({
            @Result(column = "rid",property = "rid"),
            @Result(column = "rname",property = "rname"),
            @Result(column = "create_uid",property = "createUid",one = @One(select = "cn.myeit.mapper.UserMapper.findUserByUid")),
            @Result(column = "create_time",property = "createTime"),
            @Result(column = "update_uid",property = "updateUid",one = @One(select = "cn.myeit.mapper.UserMapper.findUserByUid")),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "status",property = "status")
    })
    @Select("SELECT rid,rname,create_uid,create_time,update_uid,update_time,status FROM role WHERE status IN (0,1) LIMIT #{start},#{end};")
    List<Role> findRolesByStatusAndLimit(@Param("start") Integer start,@Param("end") Integer end);

    @Update("UPDATE role SET rname = #{role.rname},update_uid = #{role.updateUid.uid},update_time = #{role.updateTime},status=#{role.status} WHERE rid = #{role.rid}")
    Integer updateRnameAndUpdateByRid(@Param("role") Role role);

    @Update("<script>" +
            "UPDATE `role` SET STATUS = 2 WHERE rid IN " +
            "<foreach collection='ridBox' item='item' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    Long delRoleByRid(@Param("ridBox") List ridBox);
}
