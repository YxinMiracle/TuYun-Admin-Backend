package com.yxinmiracle.common.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("classpath:redis.properties")
@ConfigurationProperties(prefix = "spring.redis")
public class redisConfig {

}
