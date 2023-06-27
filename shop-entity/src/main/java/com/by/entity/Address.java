package com.by.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_address")
public class Address {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String phone;

    private String address;

    private Integer uid;

    private Integer def;

}
