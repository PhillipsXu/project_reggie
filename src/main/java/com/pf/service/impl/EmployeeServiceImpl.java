package com.pf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pf.dao.EmployeeMapper;
import com.pf.pojo.Employee;
import com.pf.service.IEmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
}
