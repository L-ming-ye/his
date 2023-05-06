package cn.myeit.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 用于用户表格数据的实体类
 */
public class Users {
    @ApiModelProperty("页码")
    private Integer page;
    @ApiModelProperty("数据总数")
    private Long count;
    @ApiModelProperty("每页几条数据")
    private Integer limit;
    @ApiModelProperty("数据")
    public List<User> data;

    public Users() {
    }

    public Users(Integer page, Long count, Integer limit, List<User> data) {
        this.page = page;
        this.count = count;
        this.limit = limit;
        this.data = data;
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

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Users{" +
                "page=" + page +
                ", count=" + count +
                ", limit=" + limit +
                ", data=" + data +
                '}';
    }
}
