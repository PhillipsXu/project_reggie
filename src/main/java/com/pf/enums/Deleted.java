package com.pf.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Deleted implements IEnum<Integer> {
    YES(1, "已删除"),
    NO(0, "未删除");

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
