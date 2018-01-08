/*
MySQL Backup
Source Server Version: 5.5.5
Source Database: kumo
Date: 11/10/2017 01:38:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
--  Table structure for `company`
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
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
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(36) NOT NULL,
  `company_id` int(11) NOT NULL,
  `unit_id` int(11) NOT NULL,
  `code` varchar(30) NOT NULL,
  `name` varchar(100) NOT NULL,
  `category` varchar(100) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `date_modified` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=621 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `modifier`
-- ----------------------------
DROP TABLE IF EXISTS `modifier`;
CREATE TABLE `modifier` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(36) NOT NULL,
  `company_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `price` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `ordercategory`
-- ----------------------------
DROP TABLE IF EXISTS `ordercategory`;
CREATE TABLE `ordercategory` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(36) NOT NULL,
  `company_id` int(11) NOT NULL,
  `unit_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `customfield_1` varchar(100) DEFAULT NULL,
  `customfield_2` varchar(100) DEFAULT NULL,
  `customfield_3` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `orderitem`
-- ----------------------------
DROP TABLE IF EXISTS `orderitem`;
CREATE TABLE `orderitem` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(36) NOT NULL,
  `company_id` int(11) DEFAULT NULL,
  `unit_id` int(11) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `qty` float DEFAULT NULL,
  `price` float DEFAULT NULL,
  `discount` float DEFAULT NULL,
  `tax` float DEFAULT NULL,
  `total` float DEFAULT NULL,
  `notes` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `orders`
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(36) NOT NULL,
  `company_id` int(11) DEFAULT NULL,
  `unit_id` int(11) DEFAULT NULL,
  `orderno` varchar(50) DEFAULT NULL,
  `orderdate` date DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `tax` float DEFAULT NULL,
  `payment` float DEFAULT NULL,
  `cashpayment` float DEFAULT NULL,
  `cardpyament` float DEFAULT NULL,
  `changeamount` float DEFAULT NULL,
  `uploaded` smallint(6) DEFAULT NULL,
  `customer_id` bigint(11) DEFAULT NULL,
  `customfield_1` varchar(100) DEFAULT NULL,
  `customfield_2` varchar(100) DEFAULT NULL,
  `customfield_3` varchar(100) DEFAULT NULL,
  `user_create` varchar(100) DEFAULT NULL,
  `device` varchar(100) DEFAULT NULL,
  `notes` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `product`
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(36) NOT NULL,
  `company_id` int(11) NOT NULL,
  `sku` varchar(30) NOT NULL,
  `name` varchar(100) NOT NULL,
  `uom` varchar(30) NOT NULL,
  `price` float NOT NULL,
  `tax` float NOT NULL,
  `category` varchar(100) NOT NULL,
  `barcode` varchar(50) DEFAULT NULL,
  `img` varchar(50) DEFAULT NULL,
  `date_modified` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `unitproduct`
-- ----------------------------
DROP TABLE IF EXISTS `unitproduct`;
CREATE TABLE `unitproduct` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(36) NOT NULL,
  `company_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `unit_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `units`
-- ----------------------------
DROP TABLE IF EXISTS `units`;
CREATE TABLE `units` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
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
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(36) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(30) DEFAULT NULL,
  `email_address` varchar(100) DEFAULT NULL,
  `company_id` int(11) DEFAULT NULL,
  `unit_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records 
-- ----------------------------
