INSERT INTO nginxlog.url_with_id (id, url)
  SELECT l.id, l.url
  FROM nginxlog.logs2 as l
  WHERE l.url regexp '/[[:digit:]]+\$';