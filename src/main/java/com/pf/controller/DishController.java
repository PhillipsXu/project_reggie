package com.pf.controller;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pf.dto.DishDto;
import com.pf.enums.Status;
import com.pf.pojo.Dish;
import com.pf.pojo.DishFlavor;
import com.pf.service.IDishFlavorService;
import com.pf.service.IDishService;
import com.pf.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = {"菜品管理", "口味管理"})
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private IDishService dishService;

    @Autowired
    private IDishFlavorService dishFlavorService;

    @CreateCache(area = "dish", name = "dishId_", expire = 1, timeUnit = TimeUnit.DAYS)
    private Cache<Long, DishDto> dishCache;

    @ApiOperation("新增菜品")
    @PostMapping("/create")
    public R create(@RequestBody DishDto dishDto) {
        if (dishService.saveWithFlavor(dishDto)) {
            Long id = dishDto.getId();
            if (dishCache.get(id) != null) {
                dishCache.remove(id);
            }
            return R.success("添加成功！", null);
        }
        return R.fail("添加失败！");
    }

    Integer currentPage = 1;
    Integer size = 10;
    @ApiOperation("分页查询菜品信息")
    @PostMapping("/get")
    public R get(@RequestBody(required = false) Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<>();
            map.put("name", null);
        }
        if (map.get("currentPage") != null) currentPage = (Integer) map.get("currentPage");
        if (map.get("size") != null) size = (Integer) map.get("size");

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(map.get("name") != null, Dish::getName, map.get("name"))
                        .orderByAsc(Dish::getSort);

        /* 查询菜品信息 */
        PageHelper.startPage(currentPage, size);
        List<Dish> dishList = dishService.list(wrapper);
        PageInfo<Dish> dishPageInfo = new PageInfo<>(dishList);

        /* 由菜品信息dish生成的pageInfo复制给DishDto类型的pageInfo */
        PageInfo<DishDto> dishDtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(dishPageInfo, dishDtoPageInfo, "list"); // 不复制list，因为需要修改

        /* 修改原pageInfo中的list，通过多次单表查询加入categoryName和flavorList */
        List<Dish> dishPageInfoList = dishPageInfo.getList();
        List<DishDto> dishDtoList = dishPageInfoList.stream().map(e -> dishService.cloenDishToDishDto(e)).toList();

        // 给DishDto类型的pageInfo设置list
        dishDtoPageInfo.setList(dishDtoList);

        // 多表查询：效率低
        /*List<DishDto> dishDtoList;
        if (map.get("name") != null) {
            String name = "%" + map.get("name") + "%";
            dishDtoList = dishService.getWithCategory(name, currentPage, size);
        } else {
            dishDtoList = dishService.getWithCategory(null, currentPage, size);
        }*/

        return R.success("查询成功！", dishDtoPageInfo);
    }

    @ApiOperation("通过ID查询菜品信息")
    @GetMapping("/getById")
    public R getById(@RequestParam("id") Long id) {
        /* 如果redis有，直接返回缓存数据 */
        if (dishCache.get(id) != null) {
            log.info("DataSource     : Redis");
            return R.success("查询成功！", dishCache.get(id));
        }

        /* 没有缓存，查询mysql */
        DishDto dishDto = dishService.getByIdWithCategoryAndFlavor(id);
        /* 查询一次后存入redis，以后直接从redis获取数据 */
        dishCache.put(id, dishDto);
        return R.success("查询成功！", dishDto);
    }


    @CreateCache(area = "dish", name = "categoryId_", expire = 15, timeUnit = TimeUnit.DAYS)
    private Cache<Long, List<DishDto>> dishCategoryCache;

    @ApiOperation("按分类查询菜品信息")
    @GetMapping("/getByCategory")
    public R getByType(@RequestParam("categoryId") Long categoryId) {
        List<DishDto> dishDtoList;

        dishDtoList = dishCategoryCache.get(categoryId);
        if (dishDtoList != null) {
            log.info("DataSource     : Redis");
            return R.success("查询成功！", dishDtoList);
        }

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, categoryId)
                .eq(Dish::getStatus, Status.Enable)
                .orderByAsc(Dish::getSort);
        List<Dish> dishList = dishService.list(wrapper);

        dishDtoList = dishList.stream().map(e -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(e, dishDto);

            Long dishId = dishDto.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper);
            if (dishFlavorList != null) dishDto.setFlavorList(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());

        dishCategoryCache.put(categoryId, dishDtoList, 1, TimeUnit.DAYS);

        return R.success("查询成功！", dishDtoList);
    }

    @ApiOperation("修改菜品信息")
    @PostMapping("/update")
    public R update(@RequestBody DishDto dishDto) {
        if (dishService.updateWithFlavor(dishDto)) {
            Long dishId = dishDto.getId();
            dishCache.remove(dishId);
            log.info("Redis          : {}", "删除菜品缓存,id：" + dishId);
            return R.success("修改成功！", null);
        }
        return R.fail("修改失败！");
    }

    @ApiOperation("删除菜品")
    @GetMapping("/delete")
    public R delete(@RequestBody List<Long> ids) {
        if (dishService.delete(ids)) {
            dishCache.removeAll(new HashSet<>(ids));
            log.info("Redis          : {}", "删除菜品缓存,id：" + ids);
            return R.success("删除成功！", null);
        }
        return R.fail("删除失败！");
    }
}
