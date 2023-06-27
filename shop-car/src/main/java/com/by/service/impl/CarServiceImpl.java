package com.by.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.by.domain.CarGoods;
import com.by.entity.Car;
import com.by.entity.Goods;
import com.by.mapper.CarMapper;
import com.by.service.ICarService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements ICarService {

    @Override
    public List<CarGoods> getMySQLUserCarList(Integer id) {
        return baseMapper.getMySQLUserCarList(id);
    }

    @Override
    public CarGoods getCarGoodsById(Object gid) {
        return baseMapper.getCarGoodsById(gid);
    }

}
