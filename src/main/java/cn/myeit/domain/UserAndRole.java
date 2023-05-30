package cn.myeit.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

public class UserAndRole {
    @ApiModelProperty("用户id")
    private User uid;
    @ApiModelProperty("角色表")
    private Role rid;

    public UserAndRole() {
    }

    public UserAndRole(User uid, Role rid) {
        this.uid = uid;
        this.rid = rid;
    }

    public User getUid() {
        return uid;
    }

    public void setUid(User uid) {
        this.uid = uid;
    }

    public Role getRid() {
        return rid;
    }

    public void setRid(Role rid) {
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "UserAndRole{" +
                "uid=" + uid +
                ", rid=" + rid +
                '}';
    }
}
