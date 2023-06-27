package com.by.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.by.aop.annotation.LoginUser;
import com.by.domain.CarGoods;
import com.by.entity.*;
import com.by.feign.api.IAddressService;
import com.by.feign.api.ICarService;
import com.by.feign.api.IGoodsService;
import com.by.service.IAppService;
import com.by.service.IOrderDetailService;
import com.by.service.IOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderController")
public class OrderController {

    @Resource
    private IAddressService addressService;

    @Resource
    private ICarService carService;

    @Resource
    private IOrderService orderService;

    @Resource
    private IOrderDetailService orderDetailService;

    @Resource
    private IGoodsService goodsService;

    @Resource
    private IAppService appService;

    @RequestMapping("/orderConfirm")
    @LoginUser
    public ResultEntity<?> orderConfirm(User user) {

        // 1、判断用户是否登录
        if (user.getId() == null) {

            return ResultEntity.error("用户未登录");

        }

        // 2、根据用户id查询用户地址
        List<Address> addresses = addressService.getAddressListByUid(user.getId());

        // 3、查询用户购物车信息
        List<CarGoods> carGoodsList = carService.getCarGoodsList(user.getId());

        // 4、计算总价
        BigDecimal totalPrice = new BigDecimal(0.0);
        for (CarGoods carGoods : carGoodsList) {

            Integer count = carGoods.getCount(); // 商品数量
            BigDecimal gprice = carGoods.getGprice(); // 商品价格

            // 计算商品小计
            BigDecimal multiply = gprice.multiply(BigDecimal.valueOf(count));

            totalPrice = totalPrice.add(multiply);

        }

        return ResultEntity.success(addresses);
    }

    @RequestMapping("/createOrder")
    @LoginUser
    public ResultEntity<?> createOrder(User user, Integer addressId) {

        // 1、判断用户是否登录
        if (user.getId() == null) {
            return ResultEntity.error("用户未登录");
        }

        // 2、生成订单
        Map<String, Object> order = orderService.createOrder(user.getId(), addressId);

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", user.getId());

        List<Order> orderList = orderService.list(queryWrapper);

        for (Order order1 : orderList) {
            QueryWrapper<OrderDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("oid", order1.getId());

            List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper1);
            for (OrderDetail orderDetail : orderDetailList) {
                Goods goods = goodsService.getGoodsById(orderDetail.getGid());
                Integer gstock = goods.getGstock();
                goods.setGstock(gstock - orderDetail.getCount());

                // 修改销量
                goodsService.updateGoods(goods);
            }
        }

        // 清空购物车
        carService.clearUserCar(user.getId());

        return ResultEntity.success(order);
    }

    @RequestMapping("/getOrderById")
    public ResultEntity<?> getOrderById(Integer id) {

        // 查询订单
        Order order = orderService.getById(id);

        // 查询订单详情
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("oid", id);

        List<OrderDetail> list = orderDetailService.list(queryWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("order", order);
        map.put("orderDetailList", list);

        return ResultEntity.success(map);
    }

    /**
     * 更改订单状态
     * @param id 订单id
     * @param status 订单状态
     * @return 结果集
     */
    @RequestMapping("/updateOrderStatus")
    public ResultEntity<?> updateOrderStatus(Integer id, Integer status) {

        // 查询订单
        Order order = orderService.getById(id);

        // 设置状态
        order.setStatus(status);

        // 更新状态
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        orderService.update(order, queryWrapper);

        // 如果状态为已收货，则加销量
        if (status == 6) {

            // 通过订单id查询订单详情
            QueryWrapper<OrderDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("oid", id);
            List<OrderDetail> list = orderDetailService.list(queryWrapper1);

            // 通过订单详情查询商品
            for (OrderDetail orderDetail : list) {
                Goods goods = goodsService.getGoodsById(orderDetail.getGid());
                Integer gsale = goods.getGsale();
                goods.setGsale(gsale + orderDetail.getCount());

                // 修改销量
                goodsService.updateGoods(goods);
            }

        } else if (status == 3 || status == 4 || status == 7) {

            // 通过订单id查询订单详情
            QueryWrapper<OrderDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("oid", id);
            List<OrderDetail> list = orderDetailService.list(queryWrapper1);

            // 通过订单详情查询商品
            for (OrderDetail orderDetail : list) {
                Goods goods = goodsService.getGoodsById(orderDetail.getGid());
                Integer gstock = goods.getGstock();
                goods.setGstock(gstock + orderDetail.getCount());

                // 修改库存
                goodsService.updateGoods(goods);
            }
        }

        return ResultEntity.success();
    }

    @RequestMapping("/getOrderListByUserId")
    public ResultEntity<?> getOrderListByUserId(Integer userId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", userId);

        List<Order> orderList = orderService.list(queryWrapper);

        List<List<OrderDetail>> orderDetailList = new ArrayList<>();
        for (Order order : orderList) {
            Integer oid = order.getId();

            QueryWrapper<OrderDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("oid", oid);

            List<OrderDetail> list = orderDetailService.list(queryWrapper1);

            orderDetailList.add(list);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("orderList", orderList);
        map.put("orderDetailList", orderDetailList);

        return ResultEntity.success(map);
    }

    @RequestMapping("/delOrder")
    public ResultEntity<?> delOrder(Integer id) {
        orderService.removeById(id);
        return ResultEntity.success();
    }

    @RequestMapping("/addApp")
    @LoginUser
    public ResultEntity<?> addApp(User user, String app, Integer gid, Integer oid) {

        if (user.getId() == null) {
            return ResultEntity.error("用户未登录");
        }

        Appraisement appraisement = new Appraisement();

        appraisement.setUid(user.getId());
        appraisement.setGid(gid);
        appraisement.setOid(oid);
        appraisement.setGapp(app);

        appService.save(appraisement);

        return ResultEntity.success();
    }

    @RequestMapping("/getAppByGid")
    public ResultEntity<?> getAppByGid(Integer gid) {

        QueryWrapper<Appraisement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("gid", gid);

        List<Appraisement> list = appService.list(queryWrapper);

        return ResultEntity.success(list);

    }

    @RequestMapping("/updateAppByOid")
    public ResultEntity<?> updateAppByOid(Integer oid, String app) {
        QueryWrapper<Appraisement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("oid", oid);

        Appraisement appraisement = appService.getOne(queryWrapper);
        appraisement.setGapp(app);

        appService.updateById(appraisement);
        return ResultEntity.success();
    }

    @RequestMapping("/getAppList")
    public ResultEntity<?> getAppList() {
        List<Appraisement> list = appService.list();

        return ResultEntity.success(list);
    }

    @RequestMapping("/getOrderList")
    public ResultEntity<?> getOrderList() {

        List<Order> orderList = orderService.list();

        for (Order order : orderList) {
            Integer oid = order.getId();
            QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("oid", oid);

            List<OrderDetail> list = orderDetailService.list(queryWrapper);
            order.setOrderDetailList(list);
        }


        return ResultEntity.success(orderList);
    }

    @RequestMapping("/delApp")
    public ResultEntity<?> delApp(Integer id) {
        appService.removeById(id);

        return ResultEntity.success();
    }

}
