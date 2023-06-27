package com.by.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.by.aop.annotation.LoginUser;
import com.by.constants.ShopConstants;
import com.by.domain.CarGoods;
import com.by.entity.Car;
import com.by.entity.ResultEntity;
import com.by.entity.User;
import com.by.service.ICarService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/carController")
public class CarController {

    @Resource
    private ICarService carService;

    @Resource
    private RedisTemplate redisTemplate;

    @PostConstruct // 在controller之前执行
    public void init() {

        // 设置key的序列化方式为字符串
        redisTemplate.setKeySerializer(new StringRedisSerializer());

    }

    @RequestMapping("/addCarMySQL")
    public ResultEntity<?> addCarMySQL(@RequestBody Car car) {
        return ResultEntity.success(carService.save(car));
    }

    @RequestMapping("/getCarGoodsList/{uid}")
    public List<CarGoods> getCarGoodsList(@PathVariable Integer uid) {
        return carService.getMySQLUserCarList(uid);
    }

    /**
     * 添加购物车
     * @param user 当前登录用户，没有登录就是null
     * @param car 购物车
     * @return 结果集
     */
    @RequestMapping("/addCar")
    @LoginUser // 代表接口需要注入当前登录的用户（登录就注入user，没有登录就注入null）
    public ResultEntity<?> addCar(@CookieValue(name = ShopConstants.ANON_ID, required = false)
                                      String anonId, User user, Car car,
                                  HttpServletResponse response) {

        // 判断用户是否登录
        if (user.getId() != null) {
            car.setUid(user.getId());

            // 先通过uid和gid查询是否存在购物车
            QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", user.getId());
            queryWrapper.eq("gid", car.getGid());
            Car one = carService.getOne(queryWrapper);

            // 如果存在就只更新购物车商品数量
            if (one != null) {
                car.setCount(one.getCount() + car.getCount());
                carService.update(car, queryWrapper);
            } else {

                // 如果不存在把购物车添加到数据库
                carService.save(car);

            }

        } else {

            // 1、生成匿名用户的唯一标识
            if (StringUtils.isEmpty(anonId)) { // 没有匿名用户唯一标识才创建uuid
                anonId = UUID.randomUUID().toString();
                // 把匿名用户的唯一标识写到浏览器中
                Cookie cookie = new Cookie(ShopConstants.ANON_ID, anonId);
                cookie.setPath("/"); // 解决跨域问题
                cookie.setMaxAge(60 * 60 * 24 * 7); // cookie的有效时间

                response.addCookie(cookie);
            }

            // 2、把购物车添加到redis
            Integer gid = car.getGid();
            Integer count = car.getCount();

            redisTemplate.opsForHash().put(anonId, gid, count);

        }
        return ResultEntity.success();
    }

    @RequestMapping("/getUserCarList")
    @LoginUser
    public List<CarGoods> getUserCarList(User user, @CookieValue(name = ShopConstants.ANON_ID,
            required = false) String anonId) {

        List<CarGoods> carList = new ArrayList<>();

        if (user.getId() != null) {

            // 查询数据库
            carList = carService.getMySQLUserCarList(user.getId());

        } else {

            // 查询redis
            if (!StringUtils.isEmpty(anonId)) {

                // 根据key获取所有的filed集合
                Set keys = redisTemplate.opsForHash().keys(anonId);

                // 判断当前匿名用户是否存在购物车
                if (keys != null && !keys.isEmpty()) {

                    // 遍历购物车商品
                    for (Object gid : keys) {

                        // 根据key和商品id获取商品数量
                        Object count = redisTemplate.opsForHash().get(anonId, gid);

                        // 根据商品id查询商品信息
                        CarGoods carGoods = carService.getCarGoodsById(gid);

                        // 把商品数量封装到CarGoods对象中
                        carGoods.setCount(Integer.parseInt(count.toString()));

                        // 添加到集合中
                        carList.add(carGoods);
                    }
                }
            }
        }
        return carList; // 购物车列表
    }

    @RequestMapping("/updateCar")
    @LoginUser
    public ResultEntity<?> updateCar(User user, @CookieValue(name = ShopConstants.ANON_ID,
            required = false) String anonId, Integer gid, Integer count) {

        if (user.getId() != null) {

            // 修改数据库
            Car car = new Car();
            car.setCount(count);

            QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("gid", gid);
            queryWrapper.eq("uid", user.getId());

            // 根据用户id和商品id来修改购物车中的商品数量
            carService.update(car, queryWrapper);

        } else {

            redisTemplate.opsForHash().put(anonId, gid, count);

        }

        return ResultEntity.success();
    }

    @RequestMapping("/deleteCar")
    @LoginUser
    public ResultEntity<?> deleteCar(User user, @CookieValue(name = ShopConstants.ANON_ID,
            required = false) String anonId, Integer gid) {

        if (user.getId() != null) {

            // 根据用户id和商品id删除
            QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("gid", gid);
            queryWrapper.eq("uid", user.getId());

            carService.remove(queryWrapper);

        } else {

            // 删除购物车中的某一件商品
            redisTemplate.opsForHash().delete(anonId, gid);

        }

        return ResultEntity.success();
    }

    @RequestMapping("/shopCarNum")
    @LoginUser
    public ResultEntity<?> shopCarNum(User user, @CookieValue(name = ShopConstants.ANON_ID,
            required = false) String anonId) {

        long count = 0;

        if (user.getId() != null) {

            QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", user.getId());

            count = carService.count(queryWrapper);

        } else {

            count = redisTemplate.opsForHash().keys(anonId).size();

        }

        return ResultEntity.success(count);
    }

    @RequestMapping("/clearUserCar")
    public ResultEntity<?> clearUserCar(Integer id) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", id);

        carService.remove(queryWrapper);

        return ResultEntity.success();
    }


}


