# ss-minio

表结构

```sql
CREATE DATABASE ss_minio;

USE ss_minio;

CREATE TABLE `minio_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(128) DEFAULT NULL COMMENT '名称',
  `endpoint` varchar(128) DEFAULT NULL COMMENT '对象存储URL',
  `access_key` varchar(64) DEFAULT NULL COMMENT '账户',
  `secret_key` varchar(64) DEFAULT NULL COMMENT '密码',
  `default_bucket` varchar(128) DEFAULT NULL COMMENT '默认桶名',
  `is_choose` tinyint(4) DEFAULT NULL COMMENT '是否选择 1 选择 2 未选择',
  `expire` int(11) DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='minio配置信息';
```

