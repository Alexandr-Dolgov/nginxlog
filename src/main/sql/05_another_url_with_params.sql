# распарсить оставшиеся url с params у http_method POST, HEAD, ...

USE nginxlog;


CREATE TABLE `tmp_url_with_params` (
  `id`  BIGINT(20) NOT NULL,
  `url` VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE utf8_unicode_ci;


INSERT INTO nginxlog.tmp_url_with_params (id, url)
  SELECT
    l.id,
    l.url
  FROM nginxlog.logs2 AS l
  WHERE l.url LIKE '%?%';


UPDATE nginxlog.logs2 l
  LEFT JOIN nginxlog.tmp_url_with_params u ON l.id = u.id
SET
  l.url    = SUBSTRING_INDEX(u.url, '?', 1),
  l.params = SUBSTRING_INDEX(u.url, '?', -1)
WHERE l.id = u.id;

DROP TABLE IF EXISTS `tmp_url_with_params`;