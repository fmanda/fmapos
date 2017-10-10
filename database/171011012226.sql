/*
MySQL Backup
Source Server Version: 5.5.5
Source Database: kumo
Date: 10/11/2017 01:22:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
--  Table structure for `company`
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(36) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(250) DEFAULT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=408 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `customer`
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(36) NOT NULL,
  `company_id` int(11) NOT NULL,
  `unit_id` int(11) NOT NULL,
  `code` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `category` varchar(100) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `date_modified` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `units`
-- ----------------------------
DROP TABLE IF EXISTS `units`;
CREATE TABLE `units` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(36) NOT NULL,
  `company_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(36) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(30) DEFAULT NULL,
  `email_address` varchar(100) DEFAULT NULL,
  `company_id` int(11) DEFAULT NULL,
  `unit_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records 
-- ----------------------------
INSERT INTO `company` VALUES ('405','4C53D9AB-0040-2361-CC86-CBBA21C65E7E','Pizza Hut','Solo Square Lt 2 Surakarta','Mr Pizza Hut','0271 777 777','contact@pizzahut.com'), ('406','8051653B-C1A7-BA6C-DEC1-D5F907021C59','KFC','Jl Slamet Riyadi No 88 Surakarta','Mr KFC','0271 888 888','contact@kfc.com'), ('407','C55CF322-A6B4-62DE-0CB8-1B5A8878929F','Queen Pizza','Gentant, Sukoharjo','Sigit Purnomo','0271 666 666','contact@queenpizza.com');
INSERT INTO `customer` VALUES ('2','39945E80-8055-7ACE-F5D2-27BC3E6CB62A','405','2','test 2','aa','a','aa','a','','2017-10-10 22:19:21');
INSERT INTO `units` VALUES ('1','DD83287E-9DB3-5112-1EE9-D2DBC87CA5CC','405','PH Jogja','Jogja',''), ('2','32ED427A-529A-5E26-18F9-F9EB9BEC32F6','405','PH Solo','Solo',''), ('3','5BA90A2B-1AE4-B825-0948-EB5F04F087A3','407','Queen Solo','Solo',''), ('4','CA3A74D3-50B2-3747-34D7-D398F2854DF5','407','Queen Jogja','Jogja','');
