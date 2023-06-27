package com.by.listener;

import com.by.constants.ShopConstants;
import com.by.entity.Goods;
import com.by.service.IGoodsSearchService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class GoodsQueueListener {

    @Resource
    private IGoodsSearchService goodsSearchService;

    // 创建线程池
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @RabbitListener(queues = ShopConstants.GOODS_QUEUE)
    public void addGoodsToEs(Goods goods, Channel channel, Message message) {

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    // 把商品添加到ES中
                    goodsSearchService.addGoods(goods);

                    // 手动ack
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

}
