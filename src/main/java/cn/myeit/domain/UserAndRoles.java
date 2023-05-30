package cn.myeit.domain;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class UserAndRoles {
    @ApiModelProperty("页码")
    private Integer page;
    @ApiModelProperty("数据总数")
    private Long count;
    @ApiModelProperty("每页几条数据")
    private Integer limit;
    @ApiModelProperty("数据")
    private List<UserAndRole> data;
    @ApiModelProperty("模糊查询查询的助记码")
    private String likeZjm;

    public UserAndRoles() {
    }

    public UserAndRoles(Integer page, Long count, Integer limit, List<UserAndRole> data, String likeZjm) {
        this.page = page;
        this.count = count;
        this.limit = limit;
        this.data = data;
        this.likeZjm = likeZjm;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<UserAndRole> getData() {
        return data;
    }

    public void setData(List<UserAndRole> data) {
        this.data = data;
    }

    public String getLikeZjm() {
        return likeZjm;
    }

    public void setLikeZjm(String likeZjm) {
        this.likeZjm = likeZjm;
    }

    @Override
    public String toString() {
        return "UserAndRoles{" +
                "page=" + page +
                ", count=" + count +
                ", limit=" + limit +
                ", data=" + data +
                ", likeZjm='" + likeZjm + '\'' +
                '}';
    }
}
