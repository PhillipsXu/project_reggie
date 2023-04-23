package com.pf.controller;

import com.pf.pojo.Orders;
import com.pf.service.IOrdersService;
import com.pf.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;

    @ApiOperation("提交订单")
    @PostMapping("/submit")
    public R submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("订单提交成功！", null);
    }
}
