package com.by.service.impl;

import com.by.entity.Goods;
import com.by.service.IGoodsSearchService;
import com.google.gson.Gson;
import org.apache.commons.beanutils.BeanUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class GoodsSearchServiceImpl implements IGoodsSearchService {

    @Value("${es.index}")
    private String shopIndex;

    @Value("${es.type}")
    private String shopType;

    @Resource
    private RestHighLevelClient client;

    // 添加数据到ES
    @Override
    public void addGoods(Goods goods) throws IOException {

        // 1、创建索引
        IndexRequest indexRequest = new IndexRequest(shopIndex, shopType);

        // 2、把对象转成JSON
        String json = new Gson().toJson(goods);

        // 3、设置发送给ES数据的类型
        indexRequest.source(json, XContentType.JSON);

        // 4、发送给ES
        client.index(indexRequest, RequestOptions.DEFAULT);

    }

    // 商品名字和商品描述都要匹配关键字
    @Override
    public List<Goods> searchGoodsList(String keyword, Integer psort, Integer rule) throws IOException, InvocationTargetException, IllegalAccessException {

        // 1、创建查询请求对象
        SearchRequest request = new SearchRequest(shopIndex); // 设置索引
        request.types(shopType); // 设置type

        // 2、设置条件
        SearchSourceBuilder builder = new SearchSourceBuilder();

        // 查询条件
        if (StringUtils.isEmpty(keyword)) {
            builder.query(QueryBuilders.matchAllQuery()); // 查询全部
        } else {
            builder.query(QueryBuilders.multiMatchQuery(keyword, "gname", "gdesc"));
        }

        // 设置高亮属性
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("gname", 30);
        highlightBuilder.preTags("<font color = 'red'>");
        highlightBuilder.postTags("</font>");

        // 设置排序规则（默认，价格_1，名字_2，ASC_1，DESC_2）
        if (psort != null) {
            if ("1".equals(psort.toString())) {
                if ("1".equals(rule.toString())) {
                    builder.sort("gprice", SortOrder.ASC);
                } else {
                    builder.sort("gprice", SortOrder.DESC);
                }
            } else if ("2".equals(psort.toString())) {
                if ("1".equals(rule.toString())) {
                    builder.sort("id", SortOrder.ASC);
                } else {
                    builder.sort("id", SortOrder.DESC);
                }
            }
        }

        // 把高亮属性设置到builder
        builder.highlighter(highlightBuilder);

        // 3、把查询条件设置到request中
        request.source(builder);

        // 4、发送请求
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        // 5、准备一个集合
        ArrayList<Goods> goodsList = new ArrayList<>();

        // 6、获取查询结果集
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {

            // 获取商品信息
            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();

            // 创建商品对象
            Goods goods = new Goods();

            // 把map中的数据拷贝到goods中
            BeanUtils.populate(goods, sourceAsMap);

            // 获取高亮字段，有些数据启动没有高亮字段
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField gnameHigh = highlightFields.get("gname");
            if (gnameHigh != null) {

                // 当前数据有高亮字段
                goods.setGname(gnameHigh.getFragments()[0].toString());
            }

            goodsList.add(goods);
        }

        return goodsList;
    }

}
