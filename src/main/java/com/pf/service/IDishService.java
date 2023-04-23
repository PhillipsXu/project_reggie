package com.pf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pf.dto.DishDto;
import com.pf.pojo.Dish;

import java.util.List;


public interface IDishService extends IService<Dish> {
    boolean saveWithFlavor(DishDto dishDto);
    boolean updateWithFlavor(DishDto dishDto);
    List<DishDto> getWithCategory(String name, Integer currentPage, Integer size);
    DishDto getByIdWithCategoryAndFlavor(Long id);
    DishDto cloenDishToDishDto(Dish dish);
    boolean delete(List<Long> ids);
}
