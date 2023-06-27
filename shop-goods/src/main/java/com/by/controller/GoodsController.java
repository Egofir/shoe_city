package com.by.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.by.constants.ShopConstants;
import com.by.entity.Goods;
import com.by.entity.GoodsPic;
import com.by.entity.ResultEntity;
import com.by.service.IGoodsPicService;
import com.by.service.IGoodsService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private IGoodsService goodsService;

    @Resource
    private IGoodsPicService goodsPicService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/addGoods")
    public ResultEntity<?> addGoods(@RequestBody Goods goods) {
        System.out.println(goods.getGname());
        System.out.println(goods.getGsale());

        // 1、添加商品和图片
        boolean insert = goodsService.save(goods);

        // 2、同步到es
        rabbitTemplate.convertAndSend(ShopConstants.GOODS_EXCHANGE, ShopConstants.GOODS_ROUTING_KEY,
                goods);

        return ResultEntity.responseClient(insert, "添加商品失败");
    }

    @RequestMapping("/getGoodsPage")
    public Page<Goods> getGoodsPage(@RequestBody Map<String, Object> map) {

        // 1、从map中取出需要的参数
        Object current = map.get("current");
        Object size = map.get("size");
        Object gid = map.get("id");
        Object gname = map.get("gname");
        Object gtype = map.get("gtype");
        Object gprice = map.get("gprice");

        // 2、创建分页对象
        Page<Goods> page = new Page<>();
        if (!StringUtils.isEmpty(current)) {
            page.setCurrent(Integer.parseInt(current.toString()));
        }
        if (!StringUtils.isEmpty(size)) {
            page.setSize(Integer.parseInt(size.toString()));
        }

        // 3、创建条件查询对象
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(gid)) {
            queryWrapper.like("id", gid.toString());
        }
        if (!StringUtils.isEmpty(gname)) {
            queryWrapper.like("gname", gname.toString());
        }
        if (!StringUtils.isEmpty(gtype)) {
            queryWrapper.like("gtype", gtype.toString());
        }
        if (!StringUtils.isEmpty(gprice)) {
            queryWrapper.like("gprice", gprice.toString());
        }

        // 4、查询数据
        Page<Goods> page1 = goodsService.page(page, queryWrapper);

        // 获取当前页要展示的数据
        List<Goods> records = page1.getRecords();
        for (Goods record : records) {
            Integer id = record.getId(); // 商品id

            // 根据商品id查询图片
            QueryWrapper<GoodsPic> goodsPicQueryWrapper = new QueryWrapper<>();
            goodsPicQueryWrapper.eq("gid", id);
            List<GoodsPic> goodsPicList = goodsPicService.list(goodsPicQueryWrapper);

            // 把商品图片设置到商品对象
            record.setGoodsPicList(goodsPicList);
        }

        return page1;
    }

    @RequestMapping("/getGoodsById/{id}")
    public Goods getGoodsById(@PathVariable Integer id) {
        return goodsService.getById(id);
    }

    @RequestMapping("/updateGoods")
    public ResultEntity<?> updateGoods(@RequestBody Goods goods) {
        return ResultEntity.responseClient(goodsService.updateById(goods), "更新商品信息失败");
    }

    @RequestMapping("/goodsBatchDel")
    public ResultEntity<?> goodsBatchDel(@RequestParam("goodsIdList") ArrayList<Integer> goodsIdList) {
        return ResultEntity.responseClient(goodsService.removeBatchByIds(goodsIdList),
                "删除商品失败");
    }

}
