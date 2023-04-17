package cn.myeit.util;

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
}
