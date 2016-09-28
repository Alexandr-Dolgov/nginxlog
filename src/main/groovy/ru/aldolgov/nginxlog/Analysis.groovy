package ru.aldolgov.nginxlog

import groovy.sql.GroovyRowResult
import groovy.sql.Sql

class Analysis {

    static void main(String[] args) {
        def url = 'jdbc:mysql://localhost:3306/nginxlog'
        def user = 'root'
        def password = 'root'
        def driver = 'com.mysql.jdbc.Driver'
        def sql = Sql.newInstance(url, user, password, driver)

        //analysis(sql)
        analysis2(sql)

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

    static void analysis2(Sql sql) {
        int allCount = sql.firstRow("SELECT COUNT(*) AS `count` FROM nginxlog.logs2").count
        List<GroovyRowResult> res = sql.rows("""
            SELECT l.url, count(*) AS `count`, (count(*) / ?) * 100  AS `percent`
            FROM nginxlog.logs2 l
            GROUP BY l.url
            ORDER BY `count` DESC
            """, [allCount]
        )

        List ms = [100, 200, 500, 1000, 2000, 5000, 10_000, 20_000, 50_000].reverse()
        List s = ['0.1', '0.2', '0.5', '1', '2', '5', '10', '20', '50'].reverse()
        String resultStr = "url\tcount\t%\tmax_s\tavg_s\t" +
                "${-> s.collect{">=${it}s\t>=${it}s%"}.join('\t')}\t" +
                "url\t<0.1s\t<0.1s%\t" +
                "${-> [1, 2, 3, 4, 5].collect {"${it}xx\t${it}xx%"}.join('\t')}\n"

        int index = 0
        res = res.getAt(0..499)
        for(GroovyRowResult r : res) {
            println(r)

            int count = r.count
            String url = r.url
            def time = sql.firstRow("""
                SELECT
                  max(l.t1_ms) / 1000 as max,
                  avg(l.t1_ms) / 1000 as avg
                FROM nginxlog.logs2 l
                WHERE l.url = ?
                """, [url]
            )

            String rowStr = "${url}\t${count}"
            rowStr += "\t${r.percent}\t${time.max}\t${time.avg}".replace('.', ',')

            ms.each { Integer n ->
                def t = sql.firstRow("""
                    SELECT count(*) as `count`, (count(*) / ?) * 100  as `percent`
                    FROM nginxlog.logs2 l
                    WHERE l.url = ? and l.t1_ms >= ?
                    """, [count, url, n]
                )
                rowStr += "\t${t.count}\t${t.percent}".replace('.', ',')
            }
            rowStr += "\t${url}"
            def t = sql.firstRow("""
                    SELECT count(*) as `count`, (count(*) / ?) * 100  as `percent`
                    FROM nginxlog.logs2 l
                    WHERE l.url = ? and l.t1_ms < ?
                    """, [count, url, ms.last()]
            )
            rowStr += "\t${t.count}\t${t.percent}".replace('.', ',')

            [100, 200, 300, 400, 500].each { Integer n ->
                def states = sql.firstRow("""
                    SELECT count(*) as `count`, (count(*) / ?) * 100  as `percent`
                    FROM nginxlog.logs2 l
                    WHERE l.url = ? and l.answer >= ? and l.answer < (?+100)
                    """, [count, url, n, n]
                )
                rowStr += "\t${states.count}\t${states.percent}".replace('.', ',')
            }
            rowStr += "\n"

            resultStr +=rowStr
            print(rowStr)
            println("${index} ${new Date()}")
            index++
        }

        println(resultStr)
    }

}
