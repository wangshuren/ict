package com.ict.config;

import com.ict.annotation.QueueConsumer;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Random;

public class QueueConsumerProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        QueueConsumer queueConsumer = AnnotationUtils.findAnnotation(bean.getClass(), QueueConsumer.class);
        if (queueConsumer != null) {
            //注册
            ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SimpleMessageListenerContainer.class);
            beanDefinitionBuilder.addPropertyReference("connectionFactory", "defaultRabbitmqConnectionFactory");
            beanDefinitionBuilder.addPropertyValue("queueNames", new String[]{queueConsumer.queue()});
            beanDefinitionBuilder.addPropertyReference("messageListener", beanName);
            beanDefinitionBuilder.addPropertyValue("concurrentConsumers", queueConsumer.currentConsumer());
            beanDefinitionBuilder.addPropertyValue("acknowledgeMode", AcknowledgeMode.MANUAL);
            Random random = new Random();
            int randonInt = random.nextInt(1000000);
            defaultListableBeanFactory.registerBeanDefinition(randonInt + "#queue_listener_container_of_" + queueConsumer.queue(), beanDefinitionBuilder.getBeanDefinition());

        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
