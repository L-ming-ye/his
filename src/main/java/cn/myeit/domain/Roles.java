package cn.myeit.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class Roles {
    /**
     * 角色表格数据实体类
     */
    @ApiModelProperty("页码")
    private Integer page;
    @ApiModelProperty("数据总数")
    private Long count;
    @ApiModelProperty("每页几条数据")
    private Integer limit;
    @ApiModelProperty("数据")
    public List<Role> data;

    public Roles() {
    }

    public Roles(Integer page, Long count, Integer limit, List<Role> data) {
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

    public List<Role> getData() {
        return data;
    }

    public void setData(List<Role> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Roles{" +
                "page=" + page +
                ", count=" + count +
                ", limit=" + limit +
                ", data=" + data +
                '}';
    }
}
