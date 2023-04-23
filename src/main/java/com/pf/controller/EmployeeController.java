package com.pf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pf.pojo.Employee;
import com.pf.service.IEmployeeService;
import com.pf.utils.R;
import com.pf.utils.TokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation("登录验证")
    @PostMapping("/login")
    public R login(@RequestBody(required = false) Employee employee) {
        /* 对密码进行MD5加密 */
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee queryEmp = employeeService.getOne(queryWrapper);

        /* 验证用户名是否存在 */
        if (queryEmp == null) return R.fail("用户名错误！");
        /* 验证密码是否正确 */
        if (!Objects.equals(queryEmp.getPassword(), password)) return R.fail("密码错误！");
        /* 验证用户是否可用 */
        if (queryEmp.getStatus() == 0) return R.fail("用户已禁用！");
        /* 验证全部通过，返回token */
        Map<String, String> data = new HashMap<>();
        String token = tokenUtil.generateToken(employee.getUsername());
        data.put("token", token);

        return R.success("登录成功！", data);
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public R logout(@RequestParam("username") String username) {
        if (tokenUtil.removeToken(username)) {
            return R.success("登出成功！", null);
        }
        return R.fail("登出失败！请确认登录状态");
    }

    @ApiOperation("新增员工")
    @PostMapping("/add")
    public R add(@RequestBody Employee employee) {
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);
        if (employeeService.save(employee)) {
            return R.success("添加成功！", null);
        }
        return R.fail("添加失败！");
    }

    Integer currentPage = 1;
    Integer size = 10;
    @ApiOperation("查询员工信息")
    @PostMapping("/get")
    public R get(@RequestBody(required = false) Map<String, Object> map) {
        if (map == null) map = new HashMap<>();
        if (map.get("currentPage") != null) currentPage = (Integer) map.get("currentPage");
        if (map.get("size") != null) size = (Integer) map.get("size");

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(map.get("name") != null, Employee::getName, map.get("name"))
                        .select(Employee::getId,
                                Employee::getName,
                                Employee::getUsername,
                                Employee::getPhone,
                                Employee::getSex,
                                Employee::getIdNumber,
                                Employee::getStatus);

        PageHelper.startPage(currentPage, size);
        List<Employee> queryEmployeeList = employeeService.list(wrapper);
        PageInfo<Employee> employeePageInfo = new PageInfo<>(queryEmployeeList);
        return R.success("查询成功！", employeePageInfo);
    }

    @ApiOperation("修改账户")
    @PostMapping("/update")
    public R update(@RequestBody Employee employee) {
        if (employeeService.updateById(employee)) {
            return R.success("修改成功！", null);
        }
        return R.fail("修改失败！");
    }

    @ApiOperation("按ID查询账户信息")
    @GetMapping("/getById")
    public R getById(@RequestParam("id") Long id) {
        Employee queryEmployee = employeeService.getById(id);
        if (employeeService != null) {
            return R.success("查询成功！", queryEmployee);
        }
        return R.fail("查询失败！");
    }
}
