package test;

import cn.hutool.extra.mail.MailUtil;
import cn.myeit.util.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class Test{


    public static void main(String[] args){
        String decode = URLDecoder.decode("MWE4MzAwYTYwMmY2NDA0MGFmZWI1NDQyYzBlYzcwMjAtMTY4MzM4NTE3MzAwOA==");
        System.out.println(decode);
    }
}
