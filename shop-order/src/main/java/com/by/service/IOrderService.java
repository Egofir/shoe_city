package com.by.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.by.entity.Order;

import java.util.Map;

public interface IOrderService extends IService<Order> {

    Map<String, Object> createOrder(Integer id, Integer addressId);

}
