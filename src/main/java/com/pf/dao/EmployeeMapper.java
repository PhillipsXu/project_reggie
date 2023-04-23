package com.pf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pf.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
