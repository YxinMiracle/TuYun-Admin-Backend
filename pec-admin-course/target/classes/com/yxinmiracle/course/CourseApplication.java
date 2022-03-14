package com.yxinmiracle.course;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.yxinmiracle.apis.user.feign","com.yxinmiracle.apis.advert.feign"})
public class CourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class);
    }


    @Autowired
    private Environment environment;

    // ===================================== neo4j rabbbitmq settings start =====================================
    @Bean
    public Queue graphAddRelationAndNodeQueue(){
        return new Queue(environment.getProperty("mq.graph.queue.graphAddRelationAndNode"));
    }

    @Bean
    public Queue updateRelationNameQueue(){
        return new Queue(environment.getProperty("mq.graph.queue.updateRelationName"));
    }

    @Bean
    public Queue updateTagPropertyQueue(){
        return new Queue(environment.getProperty("mq.graph.queue.updateTagProperty"));
    }

    @Bean
    public Queue deleteTagQueue(){
        return new Queue(environment.getProperty("mq.graph.queue.deleteTag"));
    }

    @Bean
    public Queue resumeTagQueue(){
        return new Queue(environment.getProperty("mq.graph.queue.resumeTag"));
    }

    @Bean
    public TopicExchange neo4jExchange(){
        return new TopicExchange(environment.getProperty("mq.graph.exchange"));
    }

    @Bean
    public Binding neo4jAddRelationAndNodeBinding(){
        String property = environment.getProperty("mq.graph.routing.graphAddRelationAndNodeRouting");
        return BindingBuilder.bind(graphAddRelationAndNodeQueue()).to(neo4jExchange()).with(property);
    }

    @Bean
    public Binding updateRelationNameBinding(){
        String property = environment.getProperty("mq.graph.routing.updateRelationNameRouting");
        return BindingBuilder.bind(updateRelationNameQueue()).to(neo4jExchange()).with(property);
    }

    @Bean
    public Binding updateTagPropertyBinding(){
        String property = environment.getProperty("mq.graph.routing.updateTagPropertyRouting");
        return BindingBuilder.bind(updateTagPropertyQueue()).to(neo4jExchange()).with(property);
    }

    @Bean
    public Binding deleteTagBinding(){
        String property = environment.getProperty("mq.graph.routing.deleteTagRouting");
        return BindingBuilder.bind(deleteTagQueue()).to(neo4jExchange()).with(property);
    }

    @Bean
    public Binding resumeTagBinding(){
        String property = environment.getProperty("mq.graph.routing.resumeTagRouting");
        return BindingBuilder.bind(resumeTagQueue()).to(neo4jExchange()).with(property);
    }
    // ===================================== neo4j rabbbitmq settings end =====================================
}
