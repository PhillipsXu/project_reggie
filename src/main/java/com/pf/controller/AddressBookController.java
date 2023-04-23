package com.pf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pf.pojo.AddressBook;
import com.pf.service.IAddressBookService;
import com.pf.utils.R;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressBookController {

    @Autowired
    private IAddressBookService addressBookService;

    @Autowired
    private HttpSession session;

    @ApiOperation("新增地址")
    @PostMapping("/save")
    public R save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId((Long) session.getAttribute("userId"));
        if (addressBookService.save(addressBook)) {
            return R.success("新增地址成功！", null);
        }
        return R.fail("新增地址失败！");
    }

    @ApiOperation("设置默认地址")
    @PostMapping("/default")
    public R setDefault(@RequestBody AddressBook addressBook) {
        AddressBook book = addressBookService.setDefault(addressBook);
        if (book != null) return R.success("设置成功！", book);
        return R.fail("设置失败！");
    }

    @ApiOperation("查询所有地址")
    @GetMapping("/list")
    public R list() {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, (Long) session.getAttribute("userId"))
                .orderByAsc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressBookService.list(wrapper);
        if (list != null) return R.success("查询成功！", list);
        return R.fail("查询失败！");
    }

    @ApiOperation("按ID查询地址")
    @GetMapping("/get")
    public R get(@RequestParam("id") Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) return R.success("查询成功！", addressBook);
        return R.fail("查询失败！");
    }

    @ApiOperation("修改地址")
    @PostMapping("/update")
    public R update(@RequestBody AddressBook addressBook) {
        if (addressBookService.updateAddr(addressBook)) {
            return R.success("修改成功！", null);
        }
        return R.fail("修改失败！");
    }

    @ApiOperation("删除地址")
    @GetMapping("/delete")
    public R delete(@RequestBody List<Long> ids) {
        if (addressBookService.removeByIds(ids)) {
            return R.success("删除成功！", null);
        }
        return R.fail("删除失败！");
    }
}
