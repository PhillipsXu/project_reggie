package com.pf.pojo;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("dish")
public class Dish implements Serializable {
    @TableId
    private Long id;
    private String name;
    @TableField("category_id")
    private Long categoryId;
    private BigDecimal price;
    private String code;
    private String image;
    private String description;
    private Integer status;
    private Integer sort;
    @TableField(value = "create_time", fill = FieldFill.INSERT, select = false)
    private Date createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE, select = false)
    private Date updateTime;
    @TableField(value = "create_user", fill = FieldFill.INSERT, select = false)
    private String createUser;
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE, select = false)
    private String updateUser;
    @TableLogic
    @TableField(select = false)
    private Integer deleted;
}
