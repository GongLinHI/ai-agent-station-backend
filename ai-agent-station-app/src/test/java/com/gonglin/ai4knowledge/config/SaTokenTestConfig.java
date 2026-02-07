package com.gonglin.ai4knowledge.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoDefaultImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class SaTokenTestConfig {

    @Bean
    @Primary
    public SaTokenDao saTokenDao() {
        return new SaTokenDaoDefaultImpl();
    }
}
