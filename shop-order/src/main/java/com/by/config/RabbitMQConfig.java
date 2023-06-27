package com.by.config;

import com.by.constants.ShopConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    // 创建订单交换机
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ShopConstants.ORDER_EXCHANGE, true, false);
    }

    // 创建订单延时队列
    @Bean
    public Queue orderTTlQueue() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-message-ttl", 60000); // 超时时间是60s
        map.put("x-dead-letter-exchange", ShopConstants.ORDER_DLE_EXCHANGE); // 配置死信交换机
        map.put("x-dead-letter-routing-key", "order.dle.ttl");

        return new Queue(ShopConstants.ORDER_QUEUE, true, false, false,
                map);
    }

    @Bean
    public Binding orderTTlQueueToOrderExchange() {
        return BindingBuilder.bind(orderTTlQueue()).to(orderExchange()).with("order.*");
    }

    // 创建死信交换机
    @Bean
    public TopicExchange orderDleExchange() {
        return new TopicExchange(ShopConstants.ORDER_DLE_EXCHANGE, true, false);
    }

    // 创建死信队列
    @Bean
    public Queue orderDelQueue() {
        return new Queue(ShopConstants.ORDER_DLE_QUEUE, true, false, false);
    }

    // 死信交换机和死信队列绑定
    @Bean
    public Binding orderDleExchangeToOrderDleQueue() {
        return BindingBuilder.bind(orderDelQueue()).to(orderDleExchange())
                .with("order.dle.*");
    }

}
