package com.watermelon.sso.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author watermelon
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private void setFiledByName(MetaObject metaObject, String name) {
        if (metaObject.hasGetter(name)) {
            if (metaObject.getGetterType(name).getTypeName().equals(Long.class.getTypeName())) {
                this.setFieldValByName(name, System.currentTimeMillis(), metaObject);
            } else {
                this.setFieldValByName(name, new Date(), metaObject);
            }
        }
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        setFiledByName(metaObject, "createTime");
        updateFill(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFiledByName(metaObject, "updateTime");
    }
}
