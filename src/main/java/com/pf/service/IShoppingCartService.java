package com.pf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pf.pojo.ShoppingCart;

public interface IShoppingCartService extends IService<ShoppingCart> {
    void clean();
}
