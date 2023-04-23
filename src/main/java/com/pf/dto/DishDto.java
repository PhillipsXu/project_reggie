package com.pf.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pf.pojo.Dish;
import com.pf.pojo.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DishDto extends Dish {

    private List<DishFlavor> flavorList = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
