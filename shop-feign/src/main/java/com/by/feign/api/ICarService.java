package com.by.feign.api;

import com.by.domain.CarGoods;
import com.by.entity.Car;
import com.by.entity.ResultEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("shop-car")
public interface ICarService {

    @RequestMapping("/carController/addCarMySQL")
    ResultEntity<?> addCarMySQL(@RequestBody Car car);

    @RequestMapping("/carController/getCarGoodsList/{uid}")
    List<CarGoods> getCarGoodsList(@PathVariable("uid") Integer uid);

    @RequestMapping("/carController/clearUserCar")
    void clearUserCar(@RequestParam("id") Integer id);
}
