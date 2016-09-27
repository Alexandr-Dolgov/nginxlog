package ru.aldolgov.nginxlog

import groovy.sql.Sql

class Analysis {

    static void main(String[] args) {
        def url = 'jdbc:mysql://localhost:3306/nginxlog'
        def user = 'root'
        def password = 'root'
        def driver = 'com.mysql.jdbc.Driver'
        def sql = Sql.newInstance(url, user, password, driver)

        analysis(sql)

        sql.close()
    }

    static void analysis(Sql sql) {
        def results

        //сделать группировку по http_methods и посмотреть на количества
        int allCount = sql.firstRow("SELECT COUNT(*) AS `count` FROM nginxlog.logs2").count
        results = sql.rows("""
            SELECT l.http_method, COUNT(*) as `count`, (COUNT(*) / ?) * 100  as `percent`
            FROM nginxlog.logs2 AS l
            GROUP BY l.http_method
            ORDER BY `count` DESC
            """, [allCount])

        //сохранить количество строк с нестандартными методами
        results = sql.rows("""
            select count(*)
            from nginxlog.logs2 as l
            where l.http_method NOT IN ('GET', 'POST', 'HEAD', 'OPTIONS', 'CONNECT', 'PROPFIND', 'PUT')
            """)
        //удалить строки у которых http_method не стандартный

        //создать отдельную таблицу url_with_id
        //достать все строки у которых зашит в урле id и положить в отдельную таблицу
        results = sql.rows("""
# выбрать все url с id
select l.id, l.url
from nginxlog.logs2 as l
where l.url regexp '/[[:digit:]]+\$'
            """)
        //в таблицу logs2 добавить столбец url_id
        //в таблице logs2 разбить url на url и url_id

        //todo
        //посмотреть максимальное время запроса
        //посмотреть минимальное время запроса
        //доставать для всех времен запроса с интервалом в 100мс количества запросов

        //сделать группировку по урлам (увидеть все контроллеры) и посмотреть на количества

        //для каждого урла сделать
            //посмотреть максимальное время запроса
            //посмотреть минимальное время запроса
            //доставать для всех времен запроса с интервалом в 100мс количества запросов

        //построить графики для каждого урла. По оси Х миллисекунды, по оси Y количества запросов

        String s = 'finish'
    }

}
