alter table tb_wallet change `index` `wallet_index` int(11) DEFAULT '0' COMMENT 'Wallet Index';
alter table tb_wallet add `is_private` int(5) DEFAULT '0' COMMENT '是否私匙(0,助记词; 1,私匙)' after `type`;
alter table tb_wallet add `password` varchar(100) DEFAULT NULL COMMENT '密码' after `is_private`;
alter table tb_transfer add `message_id` varchar(100) DEFAULT NULL COMMENT '消息ID' after `amount`;
alter table tb_transfer add `operation_user_id` bigint(11) DEFAULT '0' COMMENT '操作人' after `message_id`;

CREATE TABLE `tb_wallet_bind` (
`id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
`user_id` bigint(11) DEFAULT NULL COMMENT '用户ID',
`wallet_id` bigint(11) DEFAULT NULL COMMENT '钱包ID',
`gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
`gmt_modity` datetime DEFAULT NULL COMMENT '修改时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户钱包绑定表';


INSERT INTO `one_wallet`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1103', '账户信息', '1104', '0', 'info', 'system/wallet/index', '1', '0', 'C', '0', '0', 'system:wallet:index', 'form', 'admin', '2022-09-19 14:25:35', 'admin', '2022-09-20 09:59:48', '');
INSERT INTO `one_wallet`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1104', 'wallet', '0', '0', 'wallet', NULL, '1', '0', 'M', '0', '0', NULL, 'money', 'admin', '2022-09-20 09:59:33', '', NULL, '');

CREATE TABLE `tb_wallet_msig_proposal` (
`id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
`user_id` bigint(11) DEFAULT '0' COMMENT '用户ID',
`wallet_id` bigint(11) DEFAULT '0' COMMENT '钱包ID',
`wallet_msig_id` bigint(11) DEFAULT '0' COMMENT '多签钱包ID',
`task_id` varchar(10) DEFAULT NULL COMMENT '任务ID',
`method` varchar(50) DEFAULT NULL COMMENT '方法',
`status` int(5) DEFAULT '0' COMMENT '任务状态(0, 未完成; 1, 已完成)',
`gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
`gmt_modity` datetime DEFAULT NULL COMMENT '修改时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='多签提案处理表';

alter table tb_wallet_msig_proposal add `msg_id` varchar(100) DEFAULT NULL COMMENT '信息ID' after `method`;
alter table tb_wallet_msig_proposal add `msg_url` varchar(200) DEFAULT NULL COMMENT '信息地址' after `msg_id`;
alter table tb_wallet_msig_proposal add `is_owner` tinyint(1) DEFAULT '0' COMMENT '所属人，0,不是;1,是;' after `msg_url`;
alter table tb_wallet_msig_proposal add `err_msg` varchar(100) DEFAULT NULL COMMENT '错误信息' after `is_owner`;









