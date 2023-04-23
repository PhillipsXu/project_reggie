package com.pf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pf.dao.ShoppingCartMapper;
import com.pf.pojo.ShoppingCart;
import com.pf.service.IShoppingCartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {

    @Autowired
    private HttpSession session;

    @Override
    public void clean() {
//        Long userId = (Long) session.getAttribute("userId");
        Long userId = 1L; // 测试数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        this.remove(wrapper);
    }
}
