select count(*)
from nginxlog.logs2 as l
where l.http_method NOT IN ('GET', 'POST', 'HEAD', 'OPTIONS', 'CONNECT', 'PROPFIND', 'PUT');