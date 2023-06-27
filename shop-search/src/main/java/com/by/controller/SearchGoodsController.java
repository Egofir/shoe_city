package com.by.controller;

import com.by.entity.Goods;
import com.by.service.IGoodsSearchService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/searchGoodsController")
public class SearchGoodsController {

    @Resource
    private IGoodsSearchService goodsSearchService;

    @RequestMapping("/searchGoodsList")
    public List<Goods> searchGoodsList(String keyword, Integer psort, Integer rule) throws IOException, InvocationTargetException, IllegalAccessException {
        return goodsSearchService.searchGoodsList(keyword, psort, rule);
    }

}
