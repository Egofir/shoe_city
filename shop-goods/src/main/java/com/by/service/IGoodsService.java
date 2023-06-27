package com.by.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.by.entity.Goods;

public interface IGoodsService extends IService<Goods> {

    boolean addGoods(Goods goods);

}
