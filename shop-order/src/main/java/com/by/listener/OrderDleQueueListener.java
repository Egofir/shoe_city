package com.by.listener;

import com.by.constants.ShopConstants;
import com.by.entity.Order;
import com.by.service.IOrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

@Configuration
public class OrderDleQueueListener {

    @Resource
    private IOrderService orderService;

    @RabbitListener(queues = ShopConstants.ORDER_DLE_QUEUE)
    public void updateOrderStatus(Order order, Channel channel, Message message) {

        Order newOrder = orderService.getById(order);

        // 判断订单是否未支付
        if (1 == newOrder.getStatus()) {

            // 说明订单目前还是未支付状态，可以改为已超时
            order.setStatus(4);
            orderService.updateById(order);
        } else {

        }

        try {

            // 手动ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
