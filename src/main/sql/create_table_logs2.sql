CREATE TABLE `logs2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user` varchar(100) DEFAULT NULL,
  `is_admin` bit(1) NOT NULL,
  `dt` datetime DEFAULT NULL,
  `http_method` varchar(100) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `http_protocol_version` varchar(100) DEFAULT NULL,
  `answer` bigint(20) DEFAULT NULL,
  `size_bytes` bigint(20) DEFAULT NULL,
  `t1_ms` bigint(20) DEFAULT NULL,
  `t2_ms` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE utf8_unicode_ci