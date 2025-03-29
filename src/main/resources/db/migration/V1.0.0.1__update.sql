-- 重命名token字段为api_token
ALTER TABLE `user`
    CHANGE COLUMN `token` `api_token` varchar (1024) NOT NULL DEFAULT '' COMMENT 'API访问令牌';

-- 更新索引名称
ALTER TABLE `user`
DROP INDEX `token`,
ADD INDEX `api_token` (`api_token`(768)) USING BTREE;
