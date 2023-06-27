package com.by.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Email implements Serializable {

    private String title;

    private String content;

    private String toUser; // 收件人

    private String ccUser; // 抄送人

}
