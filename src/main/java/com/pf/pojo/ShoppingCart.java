package com.pf.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("shopping_cart")
public class ShoppingCart implements Serializable {
    @TableId
    private Long id;
    private String name;
    private String image;
    @TableField("user_id")
    private Long userId;
    @TableField("dish_id")
    private Long dishId;
    @TableField("setmeal_id")
    private Long setmealId;
    @TableField("dish_flavor")
    private String dishFlavor;
    private Integer number;
    private BigDecimal amount;
    @TableField(value = "create_time", fill = FieldFill.INSERT, select = false)
    private Date createTime;
}
