package com.by.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.by.entity.Goods;

public interface GoodsMapper extends BaseMapper<Goods> {

    int addGoods(Goods goods);

}
