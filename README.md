# treegraph-baas
数图baas系统

##一、结构模块
* --baas
* |----baas-api    存放对他接口dto
* |----baas-common 存放各模块公用的helper类等
* |----baas-core   业务逻辑处理层，包括service业务逻辑层和das数据库事务层
* |----baas-server 提供web接口直接访问层，存放controller，和请求拦截器等

##二、软件版本信息
* Springboot 2.x
* Mysql 5.7.x
* Java SE 11(LTS)

##三、数据库字典

SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for blockchain
-- ----------------------------
DROP TABLE IF EXISTS `blockchain`;
CREATE TABLE `blockchain` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `consortium_id` varchar(36) DEFAULT NULL COMMENT '联盟id',
  `blockchain_name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '区块链名称',
  `alarm_email` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '报警邮箱',
  `consensus_algorithm` int(2) DEFAULT NULL COMMENT '共识策略，1-treegraph',
  `encrypt_algorithm` int(2) DEFAULT NULL COMMENT '底层加密算法，1-treegraph',
  `status` int(2) DEFAULT NULL COMMENT '运行状态，0-未启动，1-运行中，2-已停止',
  `delete_flag` int(2) DEFAULT NULL COMMENT '0-未删除，1-已删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(36) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='区块链表';

-- ----------------------------
-- Table structure for certificate_request
-- ----------------------------
DROP TABLE IF EXISTS `certificate_request`;
CREATE TABLE `certificate_request` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `consortium_id` varchar(36) DEFAULT NULL COMMENT '联盟id',
  `blockchain_id` varchar(36) DEFAULT NULL COMMENT '区块链id',
  `apply_user_id` varchar(36) DEFAULT NULL COMMENT '申请人id',
  `certificate_url` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '证书url',
  `generate_time` datetime DEFAULT NULL COMMENT '生成时间',
  `download_time` datetime DEFAULT NULL COMMENT '下载时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='证书申请表';

-- ----------------------------
-- Table structure for consortium
-- ----------------------------
DROP TABLE IF EXISTS `consortium`;
CREATE TABLE `consortium` (
  `consortium_id` varchar(36) NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '联盟名称',
  `avatar` mediumblob COMMENT '联盟头像',
  `description` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '联盟描述',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人姓名',
  `contact_email` varchar(50) DEFAULT NULL COMMENT '联系人邮箱',
  `contact_phone` varchar(50) DEFAULT NULL COMMENT '联系人电话',
  `status` int(2) DEFAULT NULL COMMENT '联盟状态，0-创建中，1-运行中，2-已停止',
  `establish_step` int(2) DEFAULT NULL COMMENT '创建联盟步骤，0 - 创建联盟（一般不存在步骤）\n1 - 创建联盟管理员\n2 - 创建区块链\n3 - 添加节点\n4 - 等待部署',
  `delete_flag` int(2) DEFAULT NULL COMMENT '0-未删除，1-已删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(36) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`consortium_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联盟表';

-- ----------------------------
-- Table structure for node
-- ----------------------------
DROP TABLE IF EXISTS `node`;
CREATE TABLE `node` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `consortium_id` varchar(36) DEFAULT NULL COMMENT '联盟id',
  `blockchain_id` varchar(36) DEFAULT NULL COMMENT '区块链id',
  `node_ip` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '节点ip',
  `node_port` int(6) DEFAULT NULL COMMENT '节点端口',
  `organization_name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '机构名称',
  `status` int(2) DEFAULT NULL COMMENT '0-未启动，1-运行中，2-已停止',
  `delete_flag` int(2) DEFAULT NULL COMMENT '0-未删除，1-已删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(36) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点表';

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `permission_type` int(10) NOT NULL DEFAULT '1' COMMENT '权限类型，1:功能权限 2:菜单权限，3:uri权限',
  `permission_name` varchar(125) NOT NULL COMMENT '权限名称',
  `permission_code` varchar(125) NOT NULL DEFAULT '' COMMENT '权限码',
  `delete_flag` int(2) DEFAULT NULL COMMENT '0-未删除，1-已删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `consortium_id` varchar(36) DEFAULT NULL COMMENT '联盟id，系统管理员角色，联盟id为0',
  `role_type` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '角色类型,1-系统管理员，2-联盟管理员，3-普通用户',
  `role_name` varchar(64) NOT NULL COMMENT '角色名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `delete_flag` int(2) DEFAULT NULL COMMENT '0-未删除，1-已删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(36) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `idx_roleType` (`role_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` varchar(36) NOT NULL COMMENT '角色id',
  `permission_id` bigint(11) unsigned NOT NULL COMMENT '权限id',
  `consortium_id` varchar(36) DEFAULT NULL COMMENT '联盟id',
  `delete_flag` int(2) DEFAULT NULL COMMENT '0-未删除，1-已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(36) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8 COMMENT='角色权限信息';

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `account` varchar(127) DEFAULT '' COMMENT '账号',
  `email` varchar(126) DEFAULT '' COMMENT '邮箱',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(25) DEFAULT '' COMMENT '手机号',
  `password` varchar(256) DEFAULT '' COMMENT '加密后的登陆密码',
  `status` int(2) DEFAULT NULL COMMENT '1-启用，2-禁用',
  `delete_flag` int(11) DEFAULT '1' COMMENT '0-未删除，1-已删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(36) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_account` (`account`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for user_invite
-- ----------------------------
DROP TABLE IF EXISTS `user_invite`;
CREATE TABLE `user_invite` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `inviter` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '邀请者',
  `accepter` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '接受者',
  `token` varchar(625) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '邀请token',
  `expire` int(10) DEFAULT NULL COMMENT '过期时间，单位秒',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `invite_time` datetime DEFAULT NULL COMMENT '邀请时间',
  `accept_time` datetime DEFAULT NULL COMMENT '接受时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户邀请表';

-- ----------------------------
-- Table structure for user_login_token
-- ----------------------------
DROP TABLE IF EXISTS `user_login_token`;
CREATE TABLE `user_login_token` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(36) NOT NULL COMMENT '用户id',
  `token` varchar(625) NOT NULL COMMENT '登陆后token',
  `expire` int(10) NOT NULL DEFAULT '0' COMMENT '过期时间，单位秒',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employeeId` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户访问token信息';

-- ----------------------------
-- Table structure for user_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `user_oper_log`;
CREATE TABLE `user_oper_log` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户姓名',
  `user_ip` varchar(15) DEFAULT NULL COMMENT '用户ip',
  `user_device` varchar(50) DEFAULT NULL COMMENT '用户设备信息',
  `oper_type` int(2) DEFAULT '1' COMMENT '操作类型',
  `oper_code` varchar(25) DEFAULT NULL COMMENT '操作code',
  `oper_info` varchar(8000) DEFAULT '' COMMENT '操作信息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_employeeId` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1587 DEFAULT CHARSET=utf8 COMMENT='用户操作日志';

-- ----------------------------
-- Table structure for user_organization
-- ----------------------------
DROP TABLE IF EXISTS `user_organization`;
CREATE TABLE `user_organization` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户id',
  `consortium_id` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '联盟id',
  `organization_name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '机构名称',
  `delete_flag` int(2) DEFAULT NULL COMMENT '0-未删除，1-已删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户机构表';

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(36) NOT NULL COMMENT '用户id',
  `role_id` varchar(36) NOT NULL COMMENT '角色id',
  `consortium_id` varchar(36) DEFAULT NULL COMMENT '联盟id',
  `delete_flag` int(2) DEFAULT NULL COMMENT '0-未删除，1-已删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(36) DEFAULT NULL COMMENT '创建用户',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(36) DEFAULT NULL COMMENT '更新用户',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employeeId_roleId` (`user_id`,`role_id`) USING BTREE,
  KEY `idx_roleId` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='用户角色表';

-- ----------------------------
-- Table structure for user_role_invite
-- ----------------------------
DROP TABLE IF EXISTS `user_role_invite`;
CREATE TABLE `user_role_invite` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `invite_id` bigint(11) DEFAULT NULL COMMENT '邀请id',
  `user_id` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户id',
  `role_id` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '角色id',
  `consortium_id` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '联盟id',
  `organization_name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '组织名称',
  `status` int(2) DEFAULT NULL COMMENT '邀请状态，0-未授角色，1-已授角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色邀请表';

SET FOREIGN_KEY_CHECKS = 1;


