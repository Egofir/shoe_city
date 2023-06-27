package com.by.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_appraisement")
public class Appraisement {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer gid;

    private Integer uid;

    private Integer oid;

    private String gapp;

}
