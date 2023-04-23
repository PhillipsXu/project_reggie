package com.pf.dto;

import com.pf.pojo.Setmeal;
import com.pf.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishList;

    private String categoryName;
}
