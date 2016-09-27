# популярность http методов

SET @all_count = (select count(*) from nginxlog.logs2);

SELECT l.http_method, COUNT(*) as `count`, (COUNT(*) / @all_count) * 100  as `percent`
FROM nginxlog.logs2 AS l
GROUP BY l.http_method
ORDER BY `count` DESC;