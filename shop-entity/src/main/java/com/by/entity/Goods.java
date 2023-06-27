package com.by.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@TableName("t_goods")
public class Goods implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String gname;

    // 1-板鞋，2-运动鞋，3-高跟鞋，4-拖鞋
    private Integer gtype;

    private BigDecimal gprice;

    private String gdesc;

    private String gversion;

    private Double gsize;

    private Integer gstock;

    private Integer gsale;

    @TableField(exist = false)
    private String tempPng;

    @TableField(exist = false)
    private List<GoodsPic> goodsPicList;

}
