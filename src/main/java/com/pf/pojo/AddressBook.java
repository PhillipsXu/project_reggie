package com.pf.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
@TableName("address_book")
public class AddressBook implements Serializable {
    private Long id;
    @TableField("user_id")
    private Long userId;
    private String consignee;
    private String phone;
    private String sex;
    @TableField("province_code")
    private String provinceCode;
    @TableField("province_name")
    private String provinceName;
    @TableField("city_code")
    private String cityCode;
    @TableField("city_name")
    private String cityName;
    @TableField("district_code")
    private String districtCode;
    @TableField("district_name")
    private String districtName;
    private String detail;
    private String label;
    @TableField("is_default")
    private Integer isDefault;
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
