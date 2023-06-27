package com.by.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_order_detail")
public class OrderDetail {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer oid;

    private Integer gid;

    private Integer count;

    private BigDecimal subtotal;

    private String gname;

    private String gdesc;

    private String gpng;

    private BigDecimal gprice;

}
