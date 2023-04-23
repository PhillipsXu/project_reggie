package com.pf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pf.pojo.Dish;
import com.pf.pojo.Setmeal;
import com.pf.pojo.ShoppingCart;
import com.pf.service.IDishService;
import com.pf.service.ISetmealService;
import com.pf.service.IShoppingCartService;
import com.pf.utils.R;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private IDishService dishService;

    @Autowired
    private ISetmealService setmealService;

    @Autowired
    private HttpSession session;

    @ApiOperation("添加菜品或套餐至购物车")
    @PostMapping("/add")
    public R add(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(1L);

        if (shoppingCart.getDishId() == null && shoppingCart.getSetmealId() == null) return R.fail("请添加菜品或套餐信息！");
        if (shoppingCart.getDishId() != null && shoppingCart.getSetmealId() != null) return R.fail("菜品或套餐信息不能共存！");

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        if (shoppingCart.getDishId() != null) {
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());

            ShoppingCart dishIdCart = shoppingCartService.getOne(wrapper);
            if (dishIdCart == null) {
                Dish dish = dishService.getById(shoppingCart.getDishId());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
            } else {
                dishIdCart.setNumber(dishIdCart.getNumber() + 1);
                shoppingCartService.updateById(dishIdCart);
                return R.success("数量增加成功！", null);
            }
        }
        if (shoppingCart.getSetmealId() != null) {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

            ShoppingCart setmealIdCart = shoppingCartService.getOne(wrapper);
            if (setmealIdCart == null) {
                Setmeal setmeal = setmealService.getById(shoppingCart.getSetmealId());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
            } else {
                setmealIdCart.setNumber(setmealIdCart.getNumber() + 1);
                shoppingCartService.updateById(setmealIdCart);
                return R.success("数量增加成功！", null);
            }
        }

        shoppingCartService.save(shoppingCart);

        return R.success("添加成功！", null);
    }

    @ApiOperation("减少菜品或套餐数量")
    @PostMapping("/reduce")
    public R reduce(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(1L);

        if (shoppingCart.getDishId() == null && shoppingCart.getSetmealId() == null) return R.fail("请添加菜品或套餐信息！");
        if (shoppingCart.getDishId() != null && shoppingCart.getSetmealId() != null) return R.fail("菜品或套餐信息不能共存！");

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());

        if (shoppingCart.getDishId() != null) {
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());

            ShoppingCart dishIdCart = shoppingCartService.getOne(wrapper);
            if (dishIdCart == null) return R.fail("数据错误，购物车信息不存在！");
            if (dishIdCart.getNumber() == 1) {
                shoppingCartService.removeById(dishIdCart.getId());
                return R.success("菜品删除成功！", null);
            }
            if (dishIdCart.getNumber() > 1) {
                dishIdCart.setNumber(dishIdCart.getNumber() - 1);
                shoppingCartService.updateById(dishIdCart);
                return R.success("菜品数量减少成功！", null);
            }
        }
        if (shoppingCart.getSetmealId() != null) {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

            ShoppingCart setmealIdCart = shoppingCartService.getOne(wrapper);
            if (setmealIdCart == null) return R.fail("数据错误，购物车信息不存在！");
            if (setmealIdCart.getNumber() == 1) {
                shoppingCartService.removeById(setmealIdCart.getId());
                return R.success("套餐删除成功！", null);
            }
            if (setmealIdCart.getNumber() > 1) {
                setmealIdCart.setNumber(setmealIdCart.getNumber() - 1);
                shoppingCartService.updateById(setmealIdCart);
                return R.success("套餐数量减少成功！", null);
            }
        }
        return R.fail("未知错误，操作失败！");
    }

    @ApiOperation("查询购物车信息")
    @GetMapping("/list")
    public R list() {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, 1L)
                        .orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> cartList = shoppingCartService.list(wrapper);
        return R.success("查询成功！", cartList);
    }

    @ApiOperation("清空购物车")
    @GetMapping("/clean")
    public R clean() {
        shoppingCartService.clean();
        return R.success("购物车已清空！", null);
    }
}
