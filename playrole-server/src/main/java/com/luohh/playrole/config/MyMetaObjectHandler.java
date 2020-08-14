package com.luohh.playrole.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 属性动态拼装
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
//        this.setFieldValByName("createDate", new Date(), metaObject);
//        this.setFieldValByName("updateDate", new Date(), metaObject);
//        this.setFieldValByName("createTime", new Date(), metaObject);
//        this.setFieldValByName("updateTime", new Date(), metaObject);
//        this.setFieldValByName("status", DataEntity.STATUS_NORMAL, metaObject);//版本号3.0.6以及之前的版本
//        this.setFieldValByName("invoiceStatus", "1", metaObject);//版本号3.0.6以及之前的版本
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        this.setFieldValByName("updateDate", new Date(), metaObject);
//        this.setFieldValByName("updateTime", new Date(), metaObject);//版本号3.0.6以及之前的版本
    }
}
