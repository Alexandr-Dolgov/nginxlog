# top url

SET @all_count = (select count(*) from nginxlog.logs2);

select l.url, count(*) as `count`, (COUNT(*) / @all_count) * 100  as `percent`
from nginxlog.logs2 l
group by l.url
order by `count` desc;