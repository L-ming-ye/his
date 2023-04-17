# his权限管理系统

## 项目介绍

​	实现员工内部系统的权限管理(角色管理 功能管理 添加员工.....)

## 技术栈

​	后端：ssm(spring springmvc mybatis)

​	前端：html  css javascript layui库

​	数据库：mysql

## 数据库

用户表 user

| 字段名     | 类型         | 备注                       |
| ---------- | ------------ | -------------------------- |
| uid        | bigint       | 用户id 主键自增            |
| uname      | varchar(20)  | 用户名                     |
| age        | int          | 年龄                       |
| sex        | int(1)       | 0男 1女                    |
| zjm        | varchar(20)  | 助记码用于登录             |
| email      | varchar(255) | 用户邮箱                   |
| password   | varchar(255) | 密码                       |
| cerateTime | date         | 创建时间                   |
| createUid  | bigint       | 创建人id                   |
| updateTime | date         | 修改时间                   |
| updateUid  | bigint       | 修改人id                   |
| status     | int          | 用户状态 0正常 1禁用 2删除 |

```sql
CREATE TABLE `his`.`user`(  
  `uid` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户id 主键自增',
  `uname` VARCHAR(20) NOT NULL COMMENT '用户名',
  `sex` INT(1) NOT NULL DEFAULT 0 COMMENT '0男 1女',
  `zjm` VARCHAR(20) COMMENT '助记码用于登录',
  `email` VARCHAR(255) NOT NULL COMMENT '用户邮箱',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `createTime` DATE COMMENT '创建时间',
  `createUid` BIGINT COMMENT '创建人id',
  `updateTime` DATE COMMENT '修改时间',
  `updateUid` BIGINT COMMENT '修改人id',
  PRIMARY KEY (`uid`),
  UNIQUE INDEX `ZJM_UNIQUE` (`zjm`)
) CHARSET=utf8mb4;

ALTER TABLE `his`.`user`  
  ADD CONSTRAINT `user_user_caeateUid` FOREIGN KEY (`createUid`) REFERENCES `his`.`user`(`uid`),
  ADD CONSTRAINT `user_user_updateUid` FOREIGN KEY (`updateUid`) REFERENCES `his`.`user`(`uid`);

ALTER TABLE `his`.`user`   
  ADD COLUMN `age` INT NOT NULL   COMMENT '年龄' AFTER `uname`;
ALTER TABLE `his`.`user`   
  ADD COLUMN `status` INT DEFAULT 0  NULL   COMMENT '用户状态 0正常 1禁用 2删除' AFTER `updateUid`;


```

