package com.by.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("t_order")
public class Order implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer uid;

    private Date createTime; // 自动完成驼峰转下划线

    private String address;

    private String phone;

    private String username;

    private BigDecimal totalPrice;

    // 1：支付宝，2：微信
    private Integer payType;

    // 1：未支付，2：已支付，3：已取消，4：已超时，5：已发货，6：已收货， 7：已退货
    private Integer status;

    @TableField(exist = false)
    private List<OrderDetail> orderDetailList;

}
