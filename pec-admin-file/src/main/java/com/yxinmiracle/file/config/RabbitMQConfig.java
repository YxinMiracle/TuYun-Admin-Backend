package com.yxinmiracle.file.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RabbitMQConfig {

    public static final String EX_MEDIA_PROCESSTASK = "ex_media_processor";

    public static final int DEFAULT_CONCURRENT = 10;

    @Bean("customContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setMaxConcurrentConsumers(DEFAULT_CONCURRENT);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Autowired
    private Environment environment;
    // ===================================== file rabbbitmq settings start =====================================
    @Bean
    public Queue Queue(){
        return new Queue(environment.getProperty("mq.file.queue-media-video-processor"));
    }

    @Bean
    public TopicExchange Exchange(){
        return new TopicExchange(EX_MEDIA_PROCESSTASK);
    }


    @Bean
    public Binding resumeTagBinding(){
        String property = environment.getProperty("mq.file.routingkey-media-video");
        return BindingBuilder.bind(Queue()).to(Exchange()).with(property);
    }
    // ===================================== file rabbbitmq settings end =====================================


}
