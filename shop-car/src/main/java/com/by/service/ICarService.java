package com.by.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.by.domain.CarGoods;
import com.by.entity.Car;
import com.by.entity.Goods;

import java.util.List;

public interface ICarService extends IService<Car> {
    List<CarGoods> getMySQLUserCarList(Integer id);

    CarGoods getCarGoodsById(Object gid);
}
