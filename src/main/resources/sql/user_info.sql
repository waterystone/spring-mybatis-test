CREATE TABLE `user_info` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键自增ID',
  `user_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '名字',
  `sex` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '性别。0:未知;1:男士;2:女士.',
  `age` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '年龄',
  `prof_info` TEXT COMMENT '属性信息，json格式字符串.',

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

  PRIMARY KEY (`id`),
  KEY `idx_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';