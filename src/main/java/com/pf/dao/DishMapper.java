package com.pf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pf.dto.DishDto;
import com.pf.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    List<DishDto> getWithCategory(@Param("name")String name, @Param("currentPage") Integer currentPage, @Param("size") Integer size);
}
