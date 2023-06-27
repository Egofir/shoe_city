package com.by.config;

import com.by.constants.ShopConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange emailExchange() {
        return new TopicExchange(ShopConstants.EMAIL_EXCHANGE, true, false);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(ShopConstants.EMAIL_QUEUE, true, false, false);
    }

    @Bean
    public Binding emailQueueToEmailExchange() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange()).with("email.*");
    }

}
