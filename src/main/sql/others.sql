# посмотреть максимальное и минимальное время запроса
SELECT min(l.t2_ms), max(l.t2_ms)
FROM nginxlog.logs2 as l;

# сделать группировку по урлам (увидеть все контроллеры) и посмотреть на количества
SELECT l.url, count(*) as `count`
FROM nginxlog.logs2 as l
GROUP BY l.url
ORDER BY `count` DESC;