package com.pf.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R implements Serializable {
    private Integer status;
    private String message;
    private Object data;

    public R (Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static R success(String message, Object data) {
        return new R(200, message, data);
    }

    public static R fail(String message) {
        return new R(400, message);
    }
}
