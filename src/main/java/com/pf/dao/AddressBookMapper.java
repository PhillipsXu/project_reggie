package com.pf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pf.pojo.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
