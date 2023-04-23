package com.pf.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pf.dao.SetmealMapper;
import com.pf.dto.SetmealDto;
import com.pf.enums.Deleted;
import com.pf.pojo.Setmeal;
import com.pf.pojo.SetmealDish;
import com.pf.service.ISetmealDishService;
import com.pf.service.ISetmealService;
import com.pf.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {

    @Autowired
    private ISetmealDishService setmealDishService;

    @Override
    @Transactional
    public boolean saveWithDish(SetmealDto setmealDto) {
        if (this.save(setmealDto)) {
            Long setmealId = setmealDto.getId();
            List<SetmealDish> dishList = setmealDto.getSetmealDishList();
            dishList = dishList.stream().map(e -> {
                e.setSetmealId(setmealId);
                return e;
            }).collect(Collectors.toList());
            setmealDishService.saveBatch(dishList);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteWithDish(List<Long> ids) {
        if (this.removeByIds(ids)) {
            LambdaUpdateWrapper<SetmealDish> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(SetmealDish::getSetmealId, ids)
                    .set(SetmealDish::getDeleted, Deleted.YES)
                    .set(SetmealDish::getUpdateTime, new Date())
                    .set(SetmealDish::getCreateUser, BaseContext.getRequestUser());
            setmealDishService.update(updateWrapper);
            return true;
        }
        return false;
    }
}
