/*
Navicat MySQL Data Transfer

Source Server         : ddz
Source Server Version : 50616
Source Host           : localhost:3306
Source Database       : ddz

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2016-09-22 15:35:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `sex` int(1) DEFAULT '1',
  `score` bigint(20) DEFAULT '0',
  `salt` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'w', '1', '1', '0', null);
INSERT INTO `user` VALUES ('2', 'w1', '68e2d42a567c473f479407740bbb7621', '1', '0', 'DXJ93Y1H');
INSERT INTO `user` VALUES ('3', 'wwww', '3232', '0', '100', null);
INSERT INTO `user` VALUES ('5', 'w11', 'c78fa51cd4e5b5c8914a218169afd559', '0', '100', 'AEIBW4WW');
INSERT INTO `user` VALUES ('6', 'w1321', 'ac9e526fa27c74d415b27844adb63e6a', '0', '100', 'NIG93YIV');
