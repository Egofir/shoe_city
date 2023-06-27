package com.by.domain;

import com.by.entity.GoodsPic;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CarGoods {

    private Integer id;

    private String gname;

    private Integer gtype;

    private BigDecimal gprice;

    private String gdesc;

    private String gversion;

    private Double gsize;

    private Integer gstock;

    private Integer gsale;

    private Integer count;

    private List<GoodsPic> goodsPicList;

}
