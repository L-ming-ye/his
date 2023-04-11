package cn.myeit.util;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ICaptcha;
import cn.hutool.captcha.ShearCaptcha;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码的工具类
 */
@Component
public class VerifyUtil {
    /**
     * @param count 生成的字符数 <br/>
     * @param thickness 干扰线宽度 <br/>
     * @param response 响应对象 <br/>
     * @return 生成的验证码
     */
    public String createVerify(Integer count, Integer thickness, HttpServletResponse response){
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        try {
            ICaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, count, thickness);
            captcha.write(response.getOutputStream());
            return captcha.getCode();//返回验证码
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
