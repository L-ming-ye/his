# his权限管理系统

## 项目介绍

​	实现员工内部系统的权限管理(角色管理 功能管理 添加员工.....)

## 技术栈

​	后端：ssm(spring springmvc mybatis)

​	前端：html  css javascript layui库

​	数据库：mysql

## MySQL数据库

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

## Redis数据库

### index 0

​	存储的数据为hset的用户邮箱验证码 

​		key->用户邮箱 

​		MapKey->

​			uid

​			verifyData

### index 1

​		服务器单个值的set内容

​			verifyFlag -> 是否可以发送邮箱 value-> 当前时间的时间戳



## 用户登录功能

### 功能设计

前端通过ajax发送登录请求 服务器调用service将密码加密查询mysql 返回User实体对象 查询数据库时联合查询对应的创建人id和修改人id返回User实体对象 并且是懒加载模式

### 代码

**Controller**

```java
    @ApiOperation(value = "用户登录",notes = "用户登录")
    @PostMapping("/login")
    public JsonUtil login(String username, String password, String verify, HttpSession session){
        String sessionVerify = (String) session.getAttribute("verify");
        session.setAttribute("verify", null);

        //判断验证码是否正确
        if(!verify.equals(sessionVerify)){
            return JsonUtil.error("验证码错误");
        }
        //验证码正确 查找用户信息
        User user = userService.login(username,password);
        if(user == null){
            //没有查到数据 账号或密码错误
            return JsonUtil.ok("账号或密码错误");
        }else{
            //查到数据 判断用户的状态
            Integer status = user.getStatus();
            if(status == 0){
                return JsonUtil.ok("登录成功");
            }else if(status == 1) {
                return JsonUtil.ok("账号被封禁");
            }else{
                return JsonUtil.ok("用户状态异常");
            }
        }
    }
```

**Service**

```java
/**
 * 用户登录
 * @param uname
 * @param password
 * @return
 */
public User login(String uname,String password){
    //加密密码
    password = SecureUtil.md5(password);
    return userMapper.findUserByUsernameAndPassword(uname,password);
}
```

**Mapper**

```java
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
            @Result(column = "createUid", property = "createUid", one = @One(select = "cn.myeit.mapper.UserMapper.findUserByUid", fetchType = FetchType.LAZY)),
            @Result(column = "updateTime", property = "updateTime"),
            @Result(column = "updateUid", property = "updateUid", one = @One(select = "cn.myeit.mapper.UserMapper.findUserByUid", fetchType = FetchType.LAZY)),
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

```

## 找回密码功能

### 功能设计

分为两个请求：

​	**发送验证码**

​		根据用户助记码/邮箱 发送验证码 如果助记码对应用户没有邮箱则返回该用户没绑定邮箱 然后发送验证码 将验证码存入redis中过期时间为30分钟

​		**注意：**

​			为了防止点击多次发送验证码前端设置过了60秒后才可以再次点击 但是如果有人抓包疯狂发包就没用了 然后首先想到设置单个session60秒内不能频繁发送 可是又想到如果有人每一次请求都使用不同的cookie 对应的session也不同然后也可以暴力发送邮件 然后用了一个比较笨的方法在redis[1]中加了一个verifyFlag的数据 每一次调用发送邮箱请求就上锁过期时间为一分钟 其余发送验证码的请求就等着(每过一秒去查看一次是否可以上锁) 防止这次请求因为网络问题一分钟后才发送出去跑去解锁解的锁已经不是自己的了 所以我给flag的值设置为当前的时间撮在上锁时设置个时间撮 在最后判断两个时间戳相等才解锁 否则已经不是原来的锁了不需要执行(因为找回密码量不大可行) 为了防止有人发送大量请求堵塞住找回密码(排队等着发送)可以设置一分钟内如果没拿到锁则直接返回超时-这里就不多做了

​			

​	**修改新密码**

​		发送请求到后端(用户名,密码,确认密码,验证码)

​		后端先检查两次密码是否相同				

​		先根据用户名查找用户信息(邮箱) 判断是否存在 然后根据邮箱去查找对应的redis数据 如果没有直接返回验证码错误(可能验证码错误了/压根没发/防止黑客扫扫描用户不管怎么样都返回验证码错误)

​		redis有数据拿出验证码和用户请求传递的验证码对比 正确的话再取出redis中的uid修改密码 修改成功需要将redis对应的数据清空

### 代码

​	**Controller**

```java

    @ApiOperation(value = "用户找回密码",notes = "用户找回密码发送邮件")
    @PostMapping("/find")
    public JsonUtil find(String username){
        User user = userService.find(username);
        //判断是否查询到用户
        if(user == null){
            //没有数据
            return JsonUtil.ok("没有找到这个用户");
        }else{
            //查到数据 判断用户的状态
            Integer status = user.getStatus();
            if(status == 0){
                //状态正常 判断用户是否有邮箱
                if(user.getEmail() == null){
                    return JsonUtil.ok("用户没有绑定邮箱");
                }else {
                    //绑定了邮箱 判断一分钟内是否有发送 如果有返回发送频繁
                    Jedis jedis = null;
                    String flag = null;
                    try{
                        jedis = redisUtil.open();
                        //连接到了数据库
                        while (true){
                            //抢锁
                            jedis.select(1);
                            flag = String.valueOf(System.currentTimeMillis());
                            long verifyFlag = jedis.setnx("verifyFlag", flag);
                            if(verifyFlag == 1){
                                //抢到锁了
                                jedis.expire("verifyFlag",60);//设置过期时间60秒
                                break;
                            }else{
                                Thread.sleep(1000);//暂停一秒在抢锁
                            }
                        }
                        jedis.select(0);
                        //查询redis获取这个用户对应的数据
                        long time = jedis.ttl(user.getEmail());
                        //查询上一次请求的时间判断是否是一分钟内发送
                        if(time >= 1740){
                            //超时 一分钟内发送多次
                            return JsonUtil.ok("请求频繁");
                        }
                        // 都没问题 发送邮箱
                        //生成验证码
                        String verifyCode = verifyUtil.createVerifyCode(6);
                        //将验证码存入缓存
                        jedis.hset(user.getEmail(),"verifyCode",verifyCode);
                        jedis.hset(user.getEmail(),"uid",user.getUid().toString());
                        //有效时间30分钟
                        jedis.expire(user.getEmail(),1800);
                        EmailUtil.send(user.getEmail(),"找回密码","<h2>你的验证码为：<font color=\"#ff0000\" style=\"border-bottom:3px solid #f00\">"+verifyCode+"</font> 有效时间为30分钟  </h2>",true);
                        return JsonUtil.ok("发送成功");
                    }catch (Exception e){
                        //发送邮件失败
                        return JsonUtil.ok("发送邮件失败");
                    }finally {
                        jedis.select(1);
                        if(flag != null && flag.equals(jedis.get("verifyFlag"))){
                            //如果时间撮还是一样就是同一个锁 释放
                            jedis.del("verifyFlag");
                        }
                        //如果连接不为空 则关闭
                        if (jedis != null){
                            jedis.close();
                        }

                    }
                }
            }else if(status == 1) {
                return JsonUtil.ok("账号被封禁");
            }else{
                return JsonUtil.ok("用户状态异常");
            }
        }
    }

    @ApiOperation(value = "修改密码",notes = "修改密码")
    @PostMapping("/change")
    public JsonUtil change(String username, String password, String checkPassword, String verify){
        //判断两次密码是否不一致
        if(password == null || !password.equals(checkPassword)){
            return JsonUtil.ok("两次密码不一致");
        }
        Jedis jedis = null;
        try{
            //根据助记码/邮箱查询用户信息
            User user = userService.find(username);
            //判断是否查询到用户
            if(user == null){
                //没有数据
                return JsonUtil.ok("没有找到这个用户");
            }else {
                //查到数据 判断用户的状态
                Integer status = user.getStatus();
                if (status == 0) {
                    //状态正常 判断用户是否有邮箱
                    if(user.getEmail() == null){
                        return JsonUtil.ok("用户没有绑定邮箱");
                    }else {
                        //打开reids 查询邮箱对应的数据
                        jedis = redisUtil.open();
                        jedis.select(0);
                        Map<String, String> userVerifyData = jedis.hgetAll(user.getEmail());
                        if(userVerifyData == null){
                            //该用户的数据是空的 （可能过期了 也可能压根没有数据）返回验证码错误 让用户重新获取一遍
                            return JsonUtil.ok("验证码错误");
                        }
                        //有这个用户的数据
                        String verifyCode = userVerifyData.get("verifyCode");
                        //判断传入进来的验证码 和 redis数据库里的验证码是否对应
                        if(verifyCode == null || verify ==null || !verifyCode.toLowerCase().equals(verify.toLowerCase())){
                            //不对应 验证码不正确
                            return JsonUtil.ok("验证码错误");
                        }
                        //验证码正确 根据redis对应的uid修改密码
                        Long uid = new Long(userVerifyData.get("uid"));
                        Integer i = userService.changePass(uid, password);
                        if(i == 1){
                            //修改成功后删除用户在redis验证码的信息
                            jedis.del(user.getEmail());
                            return JsonUtil.ok("修改成功");
                        }else{
                            return JsonUtil.ok("修改失败");
                        }
                    }
                } else if (status == 1) {
                    return JsonUtil.ok("账号被封禁");
                } else {
                    return JsonUtil.ok("用户状态异常");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.error();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

```

**Service**

```java
    /**
     * 用户找回密码发送邮件
     */
    public User find(String username){
        return userMapper.findUserByZjmOrEmail(username);
    }

    /**
     * 修改密码
     */
    public Integer changePass(Long uid,String password){
        password = SecureUtil.md5(password);
        System.out.println(password);
        //将加密后的密码传入
        return userMapper.UpdateUserPasswordByUid(uid,password);
    }
```

Mapper

```java
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
    Integer UpdateUserPasswordByUid(@Param("uid") Long uid,@Param("password") String password);
```

