package com.ict.config;


import com.ict.sender.DefaultRabbitmqMessageSender;
import com.ict.sender.RabbitmqMessageSender;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * RABBITMQ服务器的自动配置
 */
@Configuration
@EnableConfigurationProperties(RabbitmqProperties.class)
public class RabbitmqAutoConfig {

    @Autowired
    private RabbitmqProperties rabbitmqProperties;

    @Bean(name = "defaultRabbitmqConnectionFactory")
    @Primary
    public ConnectionFactory defaultRabbitmqConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitmqProperties.getServer().getHost(), rabbitmqProperties.getServer().getPort());
        connectionFactory.setUsername(rabbitmqProperties.getServer().getUsername());
        connectionFactory.setPassword(rabbitmqProperties.getServer().getPassword());
        connectionFactory.setVirtualHost(rabbitmqProperties.getServer().getVirtualHost());
//        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
        connectionFactory.setPublisherConfirms(true);
        //TODO 连接池信息
        return connectionFactory;
    }

    @Bean(name = "defaultRabbitTemplate")
    @Primary
    public RabbitTemplate defaultRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(defaultRabbitmqConnectionFactory());
        return rabbitTemplate;
    }

    @Bean
    public RabbitmqMessageSender rabbitmqMessageSender() {
        DefaultRabbitmqMessageSender rabbitmqMessageSender = new DefaultRabbitmqMessageSender(defaultRabbitTemplate());
        return rabbitmqMessageSender;
    }

    @Bean
    @AutoConfigureOrder(Integer.MAX_VALUE)
    public QueueConsumerProcessor queueConsumerProcessor() {
        QueueConsumerProcessor queueConsumerProcessor = new QueueConsumerProcessor();
        return queueConsumerProcessor;
    }
}
