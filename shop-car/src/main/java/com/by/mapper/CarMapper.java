package com.by.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.by.domain.CarGoods;
import com.by.entity.Car;

import java.util.List;

public interface CarMapper extends BaseMapper<Car> {

    List<CarGoods> getMySQLUserCarList(Integer id);

    CarGoods getCarGoodsById(Object gid);

}
