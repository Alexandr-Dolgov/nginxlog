update nginxlog.logs2 l
  left join nginxlog.url_with_id u on
                                     l.id = u.id
set
  l.url = SUBSTRING_INDEX(u.url, '/',3),
  l.url_id = SUBSTRING_INDEX(u.url, '/',-1)
where l.id = u.id;