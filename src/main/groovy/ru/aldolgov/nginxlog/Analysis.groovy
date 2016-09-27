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

        String table = "#\tquantity_urls\t>=\tsum_requests\tpercent\n"
        int allCount = sql.firstRow("SELECT COUNT(*) AS `count` FROM nginxlog.logs2").count
        int i = 1
        [1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000].each { Integer n ->
            def r = sql.firstRow("""
            select count(*) as quantity, sum(`count`) as `sum`, (sum(`count`) / ?) * 100  as `percent` from
              (
                select l.url, count(*) as `count`
                from nginxlog.logs2 l
                group by l.url
                order by `count` desc
              ) as t
            where t.`count` >= ?
            """, [allCount, n])
            String rowStr = "$i\t${r.quantity}\t>=${n}\t${r.sum}\t${r.percent}\n"
            println(rowStr)
            table += rowStr
            i++
        }
        println(table)

        String s = 'finish'
    }

}
