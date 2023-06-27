package com.by.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultEntity<T> {

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    private String status; // 请求是否成功

    private String msg; // 失败原因

    private T data; // 返回的数据

    // 只返回成功的状态
    public static <T> ResultEntity<T> success() {
        return new ResultEntity<>(SUCCESS, null, null);
    }

    // 返回成功的状态和数据
    public static <T> ResultEntity<T> success(T t) {
        return new ResultEntity<>(SUCCESS, null, t);
    }

    public static <T> ResultEntity<T> error(String msg) {
        return new ResultEntity<>(ERROR, msg, null);
    }

    public static ResultEntity<?> responseClient(boolean flag, String msg) {
        if (flag) {
            return ResultEntity.success();
        } else {
            return ResultEntity.error(msg);
        }
    }

}
