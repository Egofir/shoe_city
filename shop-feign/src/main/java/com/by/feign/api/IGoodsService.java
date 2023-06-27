package com.by.feign.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.by.entity.Goods;
import com.by.entity.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@FeignClient("shop-goods")
public interface IGoodsService {

    @RequestMapping("/goods/addGoods")
    ResultEntity<?> addGoods(@RequestBody Goods goods);

    @RequestMapping("/goods/getGoodsPage")
    Page<Goods> getGoodsPage(@RequestBody Map<String, Object> map);

    @RequestMapping("/goods/getGoodsById/{id}")
    Goods getGoodsById(@PathVariable("id") Integer id);

    @RequestMapping("/goods/updateGoods")
    ResultEntity<?> updateGoods(@RequestBody Goods goods);

    @RequestMapping("/goods/goodsBatchDel")
    ResultEntity<?> goodsBatchDel(@RequestParam("goodsIdList") ArrayList<Integer> goodsIdList);

    @RequestMapping("/goods/getGoodsAll")
    List<Goods> getGoodsAll();

}
