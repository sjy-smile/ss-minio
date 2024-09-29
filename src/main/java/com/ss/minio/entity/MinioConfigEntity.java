package com.ss.minio.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * minio配置信息
 * </p>
 *
 * @author ${author}
 * @since 2024-09-25
 */
@Data
@TableName("minio_config")
@EqualsAndHashCode(callSuper = false)
public class MinioConfigEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 对象存储URL
     */
    private String endpoint;

    /**
     * 账户
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;

    /**
     * 默认桶名
     */
    private String defaultBucket;

    /**
     * 是否选择 1 选择 2 未选择
     */
    private Integer isChoose;

    /**
     * 过期时间
     */
    private Integer expire;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}
