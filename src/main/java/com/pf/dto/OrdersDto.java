package com.pf.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pf.pojo.OrderDetail;
import com.pf.pojo.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrdersDto extends Orders {
    List<OrderDetail> orderDetailList;
    private String userName;
    private String phone;
    private String address;
    private String consignee;
}
