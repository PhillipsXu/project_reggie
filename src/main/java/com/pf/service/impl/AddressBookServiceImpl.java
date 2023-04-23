package com.pf.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pf.dao.AddressBookMapper;
import com.pf.pojo.AddressBook;
import com.pf.service.IAddressBookService;
import com.pf.utils.BaseContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

    @Autowired
    private HttpSession session;

    @Override
    @Transactional
    public AddressBook setDefault(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, session.getAttribute("userId"))
                .set(AddressBook::getIsDefault, 0);
        this.update(updateWrapper);

        addressBook.setIsDefault(1);
        this.updateById(addressBook);
        return addressBook;
    }

    @Override
    @Transactional
    public boolean updateAddr(AddressBook addressBook) {
        if (addressBook.getIsDefault() == 1) {
            LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(AddressBook::getUserId, session.getAttribute("userId"))
                    .set(AddressBook::getIsDefault, 0);
            this.update(updateWrapper);
        }
        return this.updateById(addressBook);
    }
}
