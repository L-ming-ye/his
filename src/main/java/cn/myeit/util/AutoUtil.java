package cn.myeit.util;

import cn.myeit.domain.Role;
import cn.myeit.service.RoleService;
import cn.myeit.service.UserAndRoleService;
import cn.myeit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自动注入
 */
public class AutoUtil {
    @Autowired
    public UserService userService;

    @Autowired
    public VerifyUtil verifyUtil;

    @Autowired
    public RedisUtil redisUtil;

    @Autowired
    public RoleService roleService;

    @Autowired
    public UserAndRoleService userAndRoleService;
}
