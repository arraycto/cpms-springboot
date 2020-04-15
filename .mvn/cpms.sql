/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.5.53 : Database - cpms
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

/*Table structure for table `cpms_user` */

DROP TABLE IF EXISTS `cpms_user`;

CREATE TABLE `cpms_user` (
  `user_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `user_password` varchar(35) NOT NULL DEFAULT '' COMMENT '用户密码',
  `user_create_time` int(13) NOT NULL DEFAULT '0' COMMENT '用户创建时间',
  `user_last_login_time` int(13) NOT NULL DEFAULT '0' COMMENT '最后登陆时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

/*Data for the table `cpms_user` */

insert  into `cpms_user`(`user_id`,`user_name`,`user_password`,`user_create_time`,`user_last_login_time`) values (1,'admin','7ba08e055b00626ac575e2dbee3cabb1',0,0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
