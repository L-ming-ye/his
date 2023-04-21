package cn.myeit.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class RedisUtil {

    @Value("${redis.ip}")
    private String ip;
    @Value("${redis.port}")
    private Integer port;

    public RedisUtil(){
    }
    public Jedis open(){
        return new Jedis(ip, port);
    }


}