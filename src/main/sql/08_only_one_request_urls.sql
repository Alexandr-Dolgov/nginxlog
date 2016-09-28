# соотношение ответов для урлов к которым был только один запрос
select answer, count(*) as c
from
  (
    select l.answer, l.url, count(*) as `count`, (COUNT(*) / @all_count) * 100  as `percent`
    from nginxlog.logs2 l
    group by l.url
    order by `count` desc
  ) x
where x.`count`=1
group by answer
order by c desc