package com.pf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pf.dto.SetmealDto;
import com.pf.enums.Status;
import com.pf.pojo.Category;
import com.pf.pojo.Setmeal;
import com.pf.pojo.SetmealDish;
import com.pf.service.ICategoryService;
import com.pf.service.ISetmealDishService;
import com.pf.service.ISetmealService;
import com.pf.utils.BaseContext;
import com.pf.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private ISetmealService setmealService;

    @Autowired
    private ICategoryService categoryService;


    @ApiOperation("新增套餐")
    @PostMapping("/create")
    public R save(@RequestBody SetmealDto setmealDto) {
        if (setmealService.saveWithDish(setmealDto)) {
            return R.success("添加成功！", null);
        }
        return R.fail("新增失败！");
    }

    Integer currentPage = 1;
    Integer size = 10;

    @ApiOperation("套餐信息分页查询")
    @PostMapping("/page")
    public R page(@RequestBody Map<String, Object> map) {
        if (map.get("currentPage") != null) currentPage = (Integer) map.get("currentPage");
        if (map.get("size") != null) size = (Integer) map.get("size");
        String name = null;
        if (map.get("name") != null) name = (String) map.get("name");

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Setmeal::getName, name);

        PageHelper.startPage(currentPage, size);
        List<Setmeal> setmealList = setmealService.list(wrapper);
        PageInfo<Setmeal> setmealPageInfo = new PageInfo<>(setmealList);

        PageInfo<SetmealDto> setmealDtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(setmealPageInfo, setmealDtoPageInfo, "list");

        List<Setmeal> setmealPageInfoList = setmealPageInfo.getList();
        List<SetmealDto> setmealDtoList = setmealPageInfoList.stream().map(e -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(e, setmealDto);

            Long categoryId = setmealDto.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) setmealDto.setCategoryName(category.getName());

            return setmealDto;
        }).toList();

        setmealDtoPageInfo.setList(setmealDtoList);

        return R.success("查询成功！", setmealDtoPageInfo);
    }

    @ApiOperation("删除套餐")
    @PostMapping("/delete")
    public R delete(@RequestBody List<Long> ids) {
        if (ids.size() == 0) return R.fail("请提供需要删除的id！");

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getStatus, Status.Enable)
                .in(Setmeal::getId, ids);
        List<Setmeal> list = setmealService.list(wrapper);
        if (list.size() > 0) return R.fail("请先停售套餐！");

        if (setmealService.deleteWithDish(ids)) return R.success("删除成功！", null);

        return R.fail("删除失败！");
    }

    @ApiOperation("按分类查询套餐信息")
    @GetMapping("/getByCategory")
    public R getByCategory(@RequestParam("type") Long type) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId, type)
                .eq(Setmeal::getStatus, Status.Enable)
                .orderByAsc(Setmeal::getName);
        List<Setmeal> setmealList = setmealService.list(wrapper);
        return R.success("查询成功", setmealList);
    }
}
