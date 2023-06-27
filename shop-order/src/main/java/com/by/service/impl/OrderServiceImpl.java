package com.by.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.by.constants.ShopConstants;
import com.by.domain.CarGoods;
import com.by.entity.Address;
import com.by.entity.Order;
import com.by.entity.OrderDetail;
import com.by.feign.api.IAddressService;
import com.by.feign.api.ICarService;
import com.by.mapper.OrderMapper;
import com.by.service.IOrderDetailService;
import com.by.service.IOrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    private IAddressService addressService;

    @Resource
    private ICarService carService;

    @Resource
    private IOrderDetailService orderDetailService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public Map<String, Object> createOrder(Integer uid, Integer addressId) {

        // 1、根据地址id查询地址信息
        Address address = addressService.getAddressById(addressId);

        // 2、查询用户购物车
        List<CarGoods> carGoodsList = carService.getCarGoodsList(uid);

        // 3、计算总价
        BigDecimal totalPrice = new BigDecimal(0.0);
        for (CarGoods carGoods : carGoodsList) {

            Integer count = carGoods.getCount();
            BigDecimal gprice = carGoods.getGprice();

            BigDecimal multiply = gprice.multiply(BigDecimal.valueOf(count));

            totalPrice = totalPrice.add(multiply);

        }

        // 4、封装
        Order order = new Order();
        order.setUid(uid);
        order.setAddress(address.getAddress());
        order.setUsername(address.getUsername());
        order.setPhone(address.getPhone());
        order.setCreateTime(new Date());
        order.setStatus(1); // 未支付
        order.setPayType(1);
        order.setTotalPrice(totalPrice); // 总价

        // 5、插入订单表
        baseMapper.insert(order);

        // 6、插入订单详情表
        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (CarGoods carGoods : carGoodsList) {

            BigDecimal gprice = carGoods.getGprice();
            Integer count = carGoods.getCount();

            // 创建订单详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOid(order.getId()); // 订单id
            orderDetail.setGid(carGoods.getId());
            orderDetail.setGdesc(carGoods.getGdesc());
            orderDetail.setGname(carGoods.getGname());
            orderDetail.setGprice(gprice);
            orderDetail.setCount(count);
            orderDetail.setGpng(carGoods.getGoodsPicList().get(0).getPng());
            orderDetail.setSubtotal(gprice.multiply(BigDecimal.valueOf(count)));

            orderDetailList.add(orderDetail);
        }

        // 7、批量插入订单详情
        orderDetailService.saveBatch(orderDetailList);

        // 把订单对象写入MQ，开始倒计时
        rabbitTemplate.convertAndSend(ShopConstants.ORDER_EXCHANGE, "order.ttl", order);

        Map<String, Object> map = new HashMap<>();
        map.put("order", order);
        map.put("orderDetailList", orderDetailList);

        return map;
    }

}
