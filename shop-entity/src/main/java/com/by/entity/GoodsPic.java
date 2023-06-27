package com.by.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_goods_pic")
public class GoodsPic implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String png;

    private Integer gid;

}
