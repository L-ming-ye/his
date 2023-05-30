package cn.myeit.mapper;

import cn.myeit.domain.UserAndRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserAndRoleMapper {
    @Select("SELECT u.uid,IF(ur.rid = #{rid},ur.`rid`,NULL) AS rid FROM `user_role` ur RIGHT JOIN `user` u ON ur.uid = u.uid WHERE u.zjm LIKE #{likeZjm} LIMIT #{start},#{end};")
    @Results({
            @Result(property = "uid",column = "uid",one = @One(select = "cn.myeit.mapper.UserMapper.findUserByUid")),
            @Result(property = "rid",column = "rid",one = @One(select = "cn.myeit.mapper.RoleMapper.findRoleByRid"))
    })
    List<UserAndRole> findAll(@Param("rid")Long rid, @Param("likeZjm") String likeZjm,@Param("start") Integer start, @Param("end") Integer end);

    @Insert("INSERT INTO user_role value(#{uid},#{rid});")
    Long insertUserAndRole(@Param("uid") Long uid,@Param("rid") Long rid);

    @Delete("DELETE FROM user_role WHERE uid=#{uid} AND rid=#{rid}")
    Long delectUserAndRoleByUidAndRid(@Param("uid") Long uid,@Param("rid") Long rid);
}
