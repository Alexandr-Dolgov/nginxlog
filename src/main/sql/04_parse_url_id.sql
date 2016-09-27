# распарсить url на url и url_id

USE nginxlog;

CREATE TABLE `url_with_id` (
  `id`  BIGINT(20) NOT NULL,
  `url` VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE utf8_unicode_ci;

ALTER TABLE nginxlog.logs2
  ADD url_id VARCHAR(100)
COLLATE utf8_unicode_ci DEFAULT NULL;

INSERT INTO nginxlog.url_with_id (id, url)
  SELECT
    l.id,
    l.url
  FROM nginxlog.logs2 AS l
  WHERE l.url REGEXP '/[[:digit:]]+\$';

UPDATE nginxlog.logs2 l
  LEFT JOIN nginxlog.url_with_id u ON l.id = u.id
SET
  l.url    = SUBSTRING_INDEX(u.url, '/', 3),
  l.url_id = SUBSTRING_INDEX(u.url, '/', -1)
WHERE l.id = u.id;

DROP TABLE IF EXISTS `url_with_id`;