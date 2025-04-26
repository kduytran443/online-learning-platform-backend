package com.kduytran.categoryservice.config;

import com.kduytran.olpcommon.validation.CommonValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    public CommonValidatorService commonValidatorService() {
        return new CommonValidatorService();
    }
}
