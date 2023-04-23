package com.pf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pf.dto.SetmealDto;
import com.pf.pojo.Setmeal;

import java.util.List;

public interface ISetmealService extends IService<Setmeal> {
    boolean saveWithDish(SetmealDto setmealDto);

    boolean deleteWithDish(List<Long> ids);
}
