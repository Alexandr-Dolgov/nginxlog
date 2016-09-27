# количество урлов к которым было указанное количество запросов
SET @all_count = (select count(*) from nginxlog.logs2);
SET @n = 1000;

select count(*) as quantity, sum(`count`) as `sum`, (sum(`count`) / @all_count) * 100  as `percent` from
  (
    select l.url, count(*) as `count`
    from nginxlog.logs2 l
    group by l.url
    order by `count` desc
  ) as t
where t.`count` >= @n;