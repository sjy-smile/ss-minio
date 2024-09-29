package com.ss.minio.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * mybatis-plus 自动填充设置
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    // 插入的时候填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        // 时间格式或者使用以下时间戳
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
    }

    // 修改的时候填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
        // 起始版本 3.3.0(推荐)
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }
}
