package com.pf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pf.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
