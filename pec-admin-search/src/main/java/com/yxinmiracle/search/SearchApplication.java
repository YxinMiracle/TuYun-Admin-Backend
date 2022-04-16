package com.yxinmiracle.search;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.yxinmiracle.apis.services.feign"})
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class,args);
    }

    @Autowired
    private Environment environment;
    // ===================================== es rabbbitmq settings start =====================================

    @Bean
    public Queue addChoiceQuestionQueue(){
        return new Queue(environment.getProperty("mq.search.queue.addChoiceQuestion"));
    }

    @Bean
    public Queue addAnswerQuestionQueue(){
        return new Queue(environment.getProperty("mq.search.queue.addAnswerQuestion"));
    }

    @Bean
    public Queue addQuestionTagQueue(){
        return new Queue(environment.getProperty("mq.search.queue.addQuestionTag"));
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(environment.getProperty("mq.search.searchExchange"));
    }

    @Bean
    public Binding addQuestionTagBinding(){
        String property = environment.getProperty("mq.search.routing.addQuestionTagRouting");
        return BindingBuilder.bind(addQuestionTagQueue()).to(exchange()).with(property);
    }

    @Bean
    public Binding addAnswerQuestionBinding(){
        String property = environment.getProperty("mq.search.routing.addAnswerQuestionRouting");
        return BindingBuilder.bind(addAnswerQuestionQueue()).to(exchange()).with(property);
    }

    @Bean
        public Binding addChoiceQuestionBinding(){
        String property = environment.getProperty("mq.search.routing.addChoiceQuestionRouting");
        return BindingBuilder.bind(addChoiceQuestionQueue()).to(exchange()).with(property);
    }
    // ===================================== es rabbbitmq settings end =====================================

}
