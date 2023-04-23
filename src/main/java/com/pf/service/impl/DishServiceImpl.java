package com.pf.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pf.dao.DishMapper;
import com.pf.dto.DishDto;
import com.pf.enums.Deleted;
import com.pf.pojo.Category;
import com.pf.pojo.Dish;
import com.pf.pojo.DishFlavor;
import com.pf.service.ICategoryService;
import com.pf.service.IDishFlavorService;
import com.pf.service.IDishService;
import com.pf.utils.BaseContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

    @Autowired
    private IDishFlavorService dishFlavorService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private DishMapper dishMapper;

    @CreateCache(area = "dish", name = "id_", expire = 15, timeUnit = TimeUnit.DAYS)
    private Cache<Long, DishDto> cache;

    @Override
    @Transactional
    public boolean saveWithFlavor(DishDto dishDto) {
        if (this.save(dishDto)) {
            // 获取存入mysql后主键id
            Long dishId = dishDto.getId();
            /* 获取口味数据，设置dishId后存入dish_flavor表 */
            List<DishFlavor> flavorList = dishDto.getFlavorList();
            // 使用stream流效率更高
            flavorList = flavorList.stream().map(e -> {
                e.setDishId(dishId);
                return e;
            }).collect(Collectors.toList());
            dishFlavorService.saveBatch(flavorList);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateWithFlavor(DishDto dishDto) {
        if (this.updateById(dishDto)) {
            Long dishId = dishDto.getId();
            assert dishId != null;

            // 清除口味数据
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, dishId);
            dishFlavorService.remove(wrapper);

            List<DishFlavor> flavorList = dishDto.getFlavorList();
            flavorList = flavorList.stream().map(e -> {
                e.setDishId(dishId);
                return e;
            }).collect(Collectors.toList());
            dishFlavorService.saveBatch(flavorList);

            // 清除redis缓存
            cache.remove(dishId);
            return true;
        }
        return false;
    }

    @Override
    // 多表查询：效率低
    public List<DishDto> getWithCategory(String name, Integer currentPage, Integer size) {
        return dishMapper.getWithCategory(name, currentPage, size);
    }

    @Override
    public DishDto getByIdWithCategoryAndFlavor(Long id) {
        Dish dish = this.getById(id);
        return cloenDishToDishDto(dish);
    }

    @Override
    public DishDto cloenDishToDishDto(Dish dish) {
        // 创建disDto对象，将之前查询到的dish对象信息克隆
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        // 通过id查询category，设置categoryName
        Long categoryId = dishDto.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) dishDto.setCategoryName(category.getName());

        // 通过id查询dish_flavor，设置flavorList
        Long dishId = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishId);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(wrapper);
        if (dishFlavorList != null) dishDto.setFlavorList(dishFlavorList);

        return dishDto;
    }

    @Override
    @Transactional
    public boolean delete(List<Long> ids) {
        if (this.removeByIds(ids)) {
            // 用update代替remove，解决remove不自动注入字段的问题
            LambdaUpdateWrapper<DishFlavor> wrapper = new LambdaUpdateWrapper<>();
            wrapper.in(DishFlavor::getDishId, ids)
                    .set(DishFlavor::getDeleted, Deleted.YES)
                    .set(DishFlavor::getUpdateUser, BaseContext.getRequestUser())
                    .set(DishFlavor::getUpdateTime, new Date());
            dishFlavorService.update(wrapper);
            // 清除缓存
            ids.forEach(e -> cache.remove(e));
            return true;
        }
        return false;
    }
}
