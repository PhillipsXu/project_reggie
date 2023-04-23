package com.pf.controller;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pf.dto.DishDto;
import com.pf.pojo.Category;
import com.pf.service.ICategoryService;
import com.pf.utils.R;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    Integer currentPage = 1;
    Integer size = 10;

    @ApiOperation(value = "查询分类", tags = {"菜品、套餐分类设置"})
    @GetMapping("/get")
    public R get(@RequestParam(value = "currentPage", required = false) Integer currentPage, @RequestParam(value = "size", required = false) Integer size) {
        if (currentPage != null) this.currentPage = currentPage;
        if (currentPage != null) this.size = size;
        PageHelper.startPage(this.currentPage, this.size);
        List<Category> queryCategoryList = categoryService.list();
        PageInfo<Category> pageInfo = new PageInfo<>(queryCategoryList);
        return R.success("查询成功！", pageInfo);
    }

    @ApiOperation(value = "查询所有分类", tags = {"菜品分类", "套餐分类"})
    @GetMapping("/getAll")
    public R getAll(@RequestParam("type") Integer type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Category::getId, Category::getName)
                .eq(Category::getType, type)
                .orderByAsc(Category::getSort);
        List<Category> categoryList = categoryService.list(wrapper);
        if (categoryList != null) {
            return R.success("查询成功！", categoryList);
        }
        return R.fail("查询失败！");
    }

    @ApiOperation("按ID查询分类")
    @GetMapping("/getById")
    public R getById(@RequestParam("id") Long id) {
        Category category = categoryService.getById(id);
        if (category != null) {
            return R.success("查询成功！", category);
        }
        return R.fail("查询失败！");
    }

    @ApiOperation("修改分类")
    @PostMapping("/update")
    public R update(@RequestBody Category category) {
        if (categoryService.updateById(category)) {
            return R.success("修改成功！", null);
        }
        return R.fail("修改失败！");
    }

    @CreateCache(area = "dish", name = "categoryId_", expire = 15, timeUnit = TimeUnit.DAYS)
    private Cache<Long, List<DishDto>> dishCategoryCache;

    @ApiOperation("删除分类")
    @PostMapping("/delete")
    public R delete(@RequestBody List<Long> ids) {
        if (categoryService.removeByIds(ids)) {
            dishCategoryCache.removeAll(new HashSet<>(ids));
            log.info("Redis          : {}", "删除分类缓存,id：" + ids);
            return R.success("删除成功！", null);
        }
        return R.fail("删除失败！");
    }

    @ApiOperation("创建分类")
    @PostMapping("/create")
    public R create(@RequestBody Category category) {
        if (categoryService.save(category)) {
            return R.success("添加成功！", null);
        }
        return R.fail("添加失败！");
    }
}
