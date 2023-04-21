package cn.myeit.util;

import cn.hutool.extra.mail.MailUtil;
import org.springframework.stereotype.Component;

public class EmailUtil {

    public static void send(String email,String title,String text,Boolean isHtml){
        MailUtil.send(email, title, text, isHtml);
    }

    public static void send(String email,String title,String text){
        send(email,title,text,false);
    }
}
