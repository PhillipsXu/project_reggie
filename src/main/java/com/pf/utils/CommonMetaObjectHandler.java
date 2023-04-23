package com.pf.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName("createTime", metaObject);
        if (createTime == null) {
            setFieldValByName("createTime", new Date(), metaObject);
        }
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (updateTime == null) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }
        Object createUser = getFieldValByName("createUser", metaObject);
        if (createUser == null) {
            setFieldValByName("createUser", BaseContext.getRequestUser(), metaObject);
        }
        Object updateUser = getFieldValByName("updateUser", metaObject);
        if (updateUser == null) {
            setFieldValByName("updateUser", BaseContext.getRequestUser(), metaObject);
        }

        Object orderTime = getFieldValByName("orderTime", metaObject);
        if (orderTime == null) {
            setFieldValByName("orderTime", new Date(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (updateTime == null) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }
        Object updateUser = getFieldValByName("updateUser", metaObject);
        if (updateUser == null) {
            setFieldValByName("updateUser", BaseContext.getRequestUser(), metaObject);
        }
    }
}
