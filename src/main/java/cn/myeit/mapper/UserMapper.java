package cn.myeit.mapper;

import cn.myeit.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface UserMapper {
    /**
     * 根据账号密码查找用户
     * @param username 账号
     * @param password 密码
     * @return 用户信息
     */

    @Select("SELECT uid,uname,age,sex,zjm,email,createTime,createUid,updateTime,updateUid,status FROM USER WHERE (zjm = #{username} OR email = #{username}) AND password = #{password}")
    @Results({
            @Result(column = "uid", property = "uid"),
            @Result(column = "uname", property = "uname"),
            @Result(column = "age", property = "age"),
            @Result(column = "sex", property = "sex"),
            @Result(column = "zjm", property = "zjm"),
            @Result(column = "email", property = "email"),
            @Result(column = "createTime", property = "createTime"),
            @Result(column = "createUid", property = "createUid", one = @One(select = "cn.myeit.mapper.UserMapper.findUserByUid", fetchType = FetchType.DEFAULT)),
            @Result(column = "updateTime", property = "updateTime"),
            @Result(column = "updateUid", property = "updateUid", one = @One(select = "cn.myeit.mapper.UserMapper.findUserByUid", fetchType = FetchType.DEFAULT)),
            @Result(column = "status", property = "status"),
    })
    User findUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户uid查询用户数据
     * @param uid
     * @return
     */
    @Select("SELECT uid,uname,age,sex,zjm,email,createTime,createUid,updateTime,updateUid,STATUS FROM USER WHERE uid = #{uid}")
    User findUserByUid(Long uid);

    /**
     * 根据用户助记码/邮箱查询用户数据
     * @param username 助记码/邮箱
     * @return
     */
    @Select("SELECT uid,uname,age,sex,zjm,email,createTime,createUid,updateTime,updateUid,STATUS FROM USER WHERE zjm = #{username} OR email = #{username}")
    User findUserByZjmOrEmail(String username);

    /**
     * 根据用户uid修改用户密码
     */
    @Update("UPDATE user SET password = #{password} WHERE uid = #{uid}")
    Integer updateUserPasswordByUid(@Param("uid") Long uid,@Param("password") String password);

    /**
     * 插入user新数据
     * @param user
     * @param password
     * @return
     */
    @Insert("INSERT USER(uname,age,sex,zjm,email,password,createTime,createUid) VALUE(#{user.uname},#{user.age},#{user.sex},#{user.zjm},#{user.email},#{password},#{user.createTime},#{user.createUid.uid})")
    Integer insertUser(@Param("user") User user,@Param("password")String password);

    /**
     * 根据用户助记码和邮箱查询用户数据
     * @param zjm 助记码
     * @param email 邮箱
     * @return
     */
    @Select("SELECT uid,uname,age,sex,zjm,email,createTime,createUid,updateTime,updateUid,STATUS FROM USER WHERE zjm = #{zjm} OR email = #{email}")
    List<User> findUserByZjmAndEmail(@Param("zjm") String zjm,@Param("email") String email);
}
