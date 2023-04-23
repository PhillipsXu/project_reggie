package com.pf.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("employee")
public class Employee implements Serializable {
    private Long id;
    private String name;
    private String username;
    @TableField(select = false)
    private String password;
    private String phone;
    private String sex;
    @TableField("id_number")
    private String idNumber;
    private Integer status;
    @TableField(value = "create_time", fill = FieldFill.INSERT, select = false)
    private Date createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE, select = false)
    private Date updateTime;
    @TableField(value = "create_user", fill = FieldFill.INSERT, select = false)
    private String createUser;
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE, select = false)
    private String updateUser;
}
