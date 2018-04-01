package com.hand.promotion.config;

import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.net.UnknownHostException;

/**
 * @author xinyang.Mei
 * @version V1.0
 * @date 2018/2/7
 * @description
 */
@Configuration
public class MongoConfig {
    @Value("${spring.data.mongodb.uri}")
    private String MONGO_URI1;

    @Bean
    public MongoMappingContext mongoMappingContext() {
        MongoMappingContext mappingContext = new MongoMappingContext();
        return mappingContext;
    }


    @Bean
    @Primary
    public MongoDbFactory dbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_URI1));
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() throws UnknownHostException {
        return new MongoTemplate(this.dbFactory());
    }
}
