# индексы для ускорения выборки данных

CREATE INDEX PIndexUrl
ON nginxlog.logs2 (url);

CREATE INDEX PIndexT1ms
ON nginxlog.logs2 (t1_ms);

CREATE INDEX PIndexAnswer
ON nginxlog.logs2 (answer);