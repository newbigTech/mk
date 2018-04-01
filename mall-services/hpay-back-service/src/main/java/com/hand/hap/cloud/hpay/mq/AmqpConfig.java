package com.hand.hap.cloud.hpay.mq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author alaowan
 *         Created at 2017/8/16 15:42
 * @description
 */
@Configuration
public class AmqpConfig {

    public static final String EXCHANGE = "thirdparty_api.logs";
    public static final String ROUTER_KEY = "DEFAULT_ROUTER";

    @Autowired
    private RabbitConfiguration rabbitConfiguration;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setAddresses(rabbitConfiguration.getAddress());
        connectionFactory.setUsername(rabbitConfiguration.getUsername());
        connectionFactory.setPassword(rabbitConfiguration.getPassword());
        connectionFactory.setVirtualHost(rabbitConfiguration.getVirtualHost());
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }
}
