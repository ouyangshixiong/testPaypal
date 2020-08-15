package com.mmgss.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

/**
 * 配置类，注入PayPal需要的认证信息
 */
@Configuration
public class PaypalConfig {

    @Value("${paypal.client.app}")
    private String clientId;
    @Value("${paypal.client.secret}")
    private String clientSecret;
    @Value("${paypal.mode}")
    private String mode;


    /**
     * 注入APIContext，APP的认证信息clientId，Secret，开发者账号创建APP时提供
     * 每次调用时都需要创建一次
     * @return
     * @throws PayPalRESTException
     */
    @Bean
    public APIContext apiContext() throws PayPalRESTException{
        APIContext apiContext = new APIContext(clientId,clientSecret,mode);
        return apiContext;
    }
}
