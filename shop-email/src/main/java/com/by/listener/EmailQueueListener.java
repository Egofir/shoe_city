package com.by.listener;

import com.by.constants.ShopConstants;
import com.by.entity.Email;
import com.by.service.IEmailService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.IOException;

@Configuration
public class EmailQueueListener {

    @Resource
    private IEmailService emailService;

    @RabbitListener(queues = ShopConstants.EMAIL_QUEUE)
    public void sendEmail(Email email, Channel channel, Message message) throws MessagingException {

        // 1、调用发送邮件的接口
        emailService.sendEmail(email);

        // 2、手动ack
        try {
            long deliveryTag = message.getMessageProperties().getDeliveryTag(); // 消息唯一标识
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
