package com.pf.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Status implements IEnum<Integer> {
    Enable(1, "启用"),
    Disable(0, "停用");

    private final int value;
    private final String status;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return status;
    }
}
