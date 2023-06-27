package com.by.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.by.entity.Goods;
import com.by.entity.GoodsPic;
import com.by.mapper.GoodsMapper;
import com.by.service.IGoodsPicService;
import com.by.service.IGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Resource
    private IGoodsPicService goodsPicService;

    @Override
    public boolean addGoods(Goods goods) {
        return baseMapper.addGoods(goods) > 0;
    }

    @Override
    public boolean save(Goods goods) {

        // 1、插入商品
        baseMapper.insert(goods); // 自动完成主键回填

        // 2、获取图片路径
        String tempPng = goods.getTempPng();
        if (tempPng == null) {
            return true;
        }

        // 3、拆分
        String[] pngArray = tempPng.split("@");

        // 4、装商品图片对象
        List<GoodsPic> goodsPicList = new ArrayList<>();

        // 5、商品图片封装成对象放到list中
        for (String s : pngArray) {
            GoodsPic goodsPic = new GoodsPic();
            goodsPic.setPng(s);
            goodsPic.setGid(goods.getId());
            goodsPicList.add(goodsPic);
        }

        // 6、商品图片集合设置到商品对象中
        goods.setGoodsPicList(goodsPicList);

        // 7、插入图片
        return goodsPicService.saveBatch(goodsPicList);
    }
}
