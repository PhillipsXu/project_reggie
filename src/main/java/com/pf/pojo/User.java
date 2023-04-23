package com.pf.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("user")
public class User implements Serializable {
    @TableId
    private Long id;
    private String name;
    private String phone;
    private String sex;
    @TableField("id_number")
    private String idNumber;
    private String avatar;
    private Integer status;
}
