package cn.myeit.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public class User {
    @ApiModelProperty("用户id")
    private Long uid;
    @ApiModelProperty("用户名字")
    private String uname;
    @ApiModelProperty("用户年龄")
    private Integer age;
    @ApiModelProperty("用户性别")
    private Integer sex;
    @ApiModelProperty("助记码")
    private String zjm;
    @ApiModelProperty("用户邮箱")
    private String email;
    @ApiModelProperty("用户创建时间")
    private Date createTime;
    @ApiModelProperty("创建人的id")
    private User createUid;
    @ApiModelProperty("用户修改时间")
    private Date updateTime;
    @ApiModelProperty("修改人id")
    private User updateUid;
    @ApiModelProperty("用户状态")
    private Integer status;

    public User() {
    }

    public User(Long uid, String uname, Integer age, Integer sex, String zjm, String email, Date createTime, User createUid, Date updateTime, User updateUid, Integer status) {
        this.uid = uid;
        this.uname = uname;
        this.age = age;
        this.sex = sex;
        this.zjm = zjm;
        this.email = email;
        this.createTime = createTime;
        this.createUid = createUid;
        this.updateTime = updateTime;
        this.updateUid = updateUid;
        this.status = status;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getZjm() {
        return zjm;
    }

    public void setZjm(String zjm) {
        this.zjm = zjm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getCreateUid() {
        return createUid;
    }

    public void setCreateUid(User createUid) {
        this.createUid = createUid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public User getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(User updateUid) {
        this.updateUid = updateUid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", zjm='" + zjm + '\'' +
                ", email='" + email + '\'' +
                ", createTime=" + createTime +
                ", createUid=" + createUid +
                ", updateTime=" + updateTime +
                ", updateUid=" + updateUid +
                ", status=" + status +
                '}';
    }
}
