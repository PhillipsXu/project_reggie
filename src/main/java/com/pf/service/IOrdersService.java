package com.pf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pf.pojo.Orders;

public interface IOrdersService extends IService<Orders> {
    void submit(Orders orders);
}
