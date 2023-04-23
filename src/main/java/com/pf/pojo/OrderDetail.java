package com.pf.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("order_detail")
public class OrderDetail {
    @TableId
    private Long id;
    private String name;
    private String image;
    @TableField("order_id")
    private Long orderId;
    @TableField("dish_id")
    private Long dishId;
    @TableField("setmeal_id")
    private Long setmealId;
    @TableField("dish_flavor")
    private String dishFlavor;
    private Integer number;
    private BigDecimal amount;
}
