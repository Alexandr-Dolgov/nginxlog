delete
from nginxlog.logs2
where
  http_method != 'GET' and
  http_method != 'POST' and
  http_method != 'HEAD' and
  http_method != 'OPTIONS' and
  http_method != 'CONNECT' and
  http_method != 'PROPFIND' and
  http_method != 'PUT';