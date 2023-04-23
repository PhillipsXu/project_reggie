package com.pf.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("category")
public class Category implements Serializable {
    @TableId
    private Long id;
    private Integer type;
    private String name;
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
