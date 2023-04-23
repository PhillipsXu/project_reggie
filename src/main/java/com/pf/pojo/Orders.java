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
@TableName("orders")
public class Orders implements Serializable {
    @TableId
    private Long id;
    private String number;
    private Integer status;
    @TableField("user_id")
    private Long userId;
    @TableField("address_book_id")
    private Long addressBookId;
    @TableField(value = "order_time", fill = FieldFill.INSERT)
    private Date orderTime;
    @TableField("checkout_time")
    private Date checkoutTime;
    @TableField("pay_method")
    private Integer payMethod;
    private BigDecimal amount;
    private String remark;
    private String consignee;
    private String phone;
    private String address;
    @TableField("user_name")
    private String username;
}
