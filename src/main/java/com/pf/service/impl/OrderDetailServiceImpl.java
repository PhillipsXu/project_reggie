package com.pf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pf.dao.OrderDetailMapper;
import com.pf.pojo.OrderDetail;
import com.pf.service.IOrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {
}
