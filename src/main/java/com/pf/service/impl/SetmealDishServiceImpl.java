package com.pf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pf.dao.SetmealDishMapper;
import com.pf.pojo.SetmealDish;
import com.pf.service.ISetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements ISetmealDishService {
}
