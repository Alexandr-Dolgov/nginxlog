# количество урлов к которым было указанное количество запросов
select count(*) from
  (
    select l.url, count(*) as `count`
    from nginxlog.logs2 l
    group by l.url
    order by `count` desc
  ) as t
where t.`count` >= 0;