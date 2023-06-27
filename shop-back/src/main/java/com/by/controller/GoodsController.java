package com.by.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.by.entity.Goods;
import com.by.entity.ResultEntity;
import com.by.feign.api.IGoodsService;
import com.by.util.QueryMapUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/goodsController")
public class GoodsController {

    @Resource
    private IGoodsService goodsService;

    @RequestMapping("/getGoodsPage")
    public Page<Goods> getGoodsPage(HttpServletRequest request) {
        Map<String, Object> map = QueryMapUtil.queryMap(request);

        // 调用service层查询数据
        return goodsService.getGoodsPage(map);
    }

    @RequestMapping("/addGoods")
    public ResultEntity<?> addGoods(Goods goods) {
        return goodsService.addGoods(goods);
    }

    @RequestMapping("/goodsBatchDel")
    public ResultEntity<?> goodsBatchDel(@RequestParam("goodsIdList") ArrayList<Integer> goodsIdList) {
        return goodsService.goodsBatchDel(goodsIdList);
    }

}
