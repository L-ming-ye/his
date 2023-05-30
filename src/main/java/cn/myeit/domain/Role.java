package cn.myeit.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

import java.util.Date;

/**
 * 角色的实体类
 */
public class Role {
    @ApiModelProperty("角色id")
    private Long rid;
    @ApiModelProperty("角色名字")
    private String rname;
    @ApiModelProperty("创建人id")
    private User createUid;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("修改人id")
    private User updateUid;
    @ApiModelProperty("修改时间")
    private Date updateTime;
    @ApiModelProperty("状态")
    private Integer status;

    public Role() {
    }

    public Role(Long rid, String rname, User createUid, Date createTime, User updateUid, Date updateTime, Integer status) {
        this.rid = rid;
        this.rname = rname;
        this.createUid = createUid;
        this.createTime = createTime;
        this.updateUid = updateUid;
        this.updateTime = updateTime;
        this.status = status;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public User getCreateUid() {
        return createUid;
    }

    public void setCreateUid(User createUid) {
        this.createUid = createUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(User updateUid) {
        this.updateUid = updateUid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Role{" +
                "rid=" + rid +
                ", rname='" + rname + '\'' +
                ", createUid=" + createUid +
                ", createTime=" + createTime +
                ", updateUid=" + updateUid +
                ", updateTime=" + updateTime +
                ", status='" + status + '\'' +
                '}';
    }
}
