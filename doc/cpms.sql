/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.26 : Database - cpms
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cpms` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `cpms`;

/*Table structure for table `cpms_role` */

DROP TABLE IF EXISTS `cpms_role`;

CREATE TABLE `cpms_role` (
  `role_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(50) NOT NULL DEFAULT '' COMMENT '角色名',
  `role_descript` varchar(500) NOT NULL DEFAULT '' COMMENT '角色描述',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

/*Data for the table `cpms_role` */

insert  into `cpms_role`(`role_id`,`role_name`,`role_descript`) values (1,'超级管理员','拥有所有权限'),(2,'管理员','拥有部分权限'),(3,'编辑','拥有编辑权限'),(4,'产品经理','查看权限');

/*Table structure for table `cpms_role_permission` */

DROP TABLE IF EXISTS `cpms_role_permission`;

CREATE TABLE `cpms_role_permission` (
  `role_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色表role_id',
  `page_permission` text NOT NULL COMMENT '页面权限URL',
  `handle_permission` text NOT NULL COMMENT '操作权限URL即增删改查接口URL',
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `cpms_role_permission` */

insert  into `cpms_role_permission`(`role_id`,`page_permission`,`handle_permission`) values (2,'/userRole,/userRole/user,/userRole/role','/user/allUser/**'),(3,'/userRole,/userRole/user','/user/allUser/**,/role/allRole/**,/user/add/**'),(4,'/userRole,/userRole/user,/userRole/role','/user/allUser/**');

/*Table structure for table `cpms_user` */

DROP TABLE IF EXISTS `cpms_user`;

CREATE TABLE `cpms_user` (
  `user_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `user_password` varchar(35) NOT NULL DEFAULT '' COMMENT '用户密码',
  `create_time` datetime NOT NULL COMMENT '用户创建时间',
  `last_login_time` datetime NOT NULL COMMENT '最后登陆时间',
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0:正常 -1：删除',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;

/*Data for the table `cpms_user` */

insert  into `cpms_user`(`user_id`,`user_name`,`user_password`,`create_time`,`last_login_time`,`status`) values (1,'admin','7ba08e055b00626ac575e2dbee3cabb1','2019-09-01 14:24:40','2020-01-09 17:55:25',0),(13,'test','7ba08e055b00626ac575e2dbee3cabb1','2019-10-08 14:11:52','2019-10-08 14:11:52',0),(15,'test2','87f138ccad328633312592271e0fb5a2','2020-01-08 19:33:15','2020-01-08 19:33:15',0),(16,'test3','87f138ccad328633312592271e0fb5a2','2020-01-09 18:12:50','2020-01-09 18:12:50',0);

/*Table structure for table `cpms_user_role` */

DROP TABLE IF EXISTS `cpms_user_role`;

CREATE TABLE `cpms_user_role` (
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户表user_id',
  `role_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色表role_id',
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `cpms_user_role` */

insert  into `cpms_user_role`(`user_id`,`role_id`) values (1,1),(15,4),(15,2),(16,3),(13,3),(13,2);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
