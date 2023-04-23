package com.pf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pf.pojo.AddressBook;

public interface IAddressBookService extends IService<AddressBook> {
    AddressBook setDefault(AddressBook addressBook);

    boolean updateAddr(AddressBook addressBook);
}
