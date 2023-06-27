package com.by.service;

import com.by.entity.Goods;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

// 操作ES
public interface IGoodsSearchService {

    void addGoods(Goods goods) throws IOException;

    List<Goods> searchGoodsList(String keyword, Integer psort, Integer rule) throws IOException, InvocationTargetException, IllegalAccessException;
}
