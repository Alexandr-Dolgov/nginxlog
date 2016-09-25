package ru.aldolgov.nginxlog

import groovy.sql.GroovyRowResult
import groovy.sql.Sql

class Main {

    static void main(String[] args) {
        def url = 'jdbc:mysql://localhost:3306/nginxlog'
        def user = 'root'
        def password = 'abcdef1'
        def driver = 'com.mysql.jdbc.Driver'
        def sql = Sql.newInstance(url, user, password, driver)

        parse(sql)

        sql.close()
    }

    static void parse(Sql sql) {
        //todo привести таблицу logs к виду, с которым удобно работать, а именно:
        //1. столбец user : переименовать в username; прочерки или 'null' заменить на NULL
        //2. столбец dt : переименовать в datetime; изменить тип на datetime
        //3. столбец url : разбить на три столбца (string http_method, string url, string protocol_version)
        //4. столбец answer : изменить тип на long
        //5. столбец size : изменить тип на long, переименовать в size_bytes
        //6. столбец t1 : изменить тип на long, переименовать в t1_ms
        //7. столбец t2 : изменить тип на long, переименовать в t2_ms

        int offset = 1
        int maxRows = 5000
        List<GroovyRowResult> results = sql.rows("SELECT * FROM logs", offset, maxRows)
        while (results != null && results.size() > 0) {
            for (GroovyRowResult result : results) {

                try {
                    String user = (result.user in ['-', 'null', null]) ? null : result.user

                    Boolean isAdmin = false
                    if (user != null) {
                        char char0 = user.charAt(0)
                        char char1 = user.charAt(1)
                        isAdmin = !((char0 == (char)'a') && (char1 >= (char)'0') && (char1 <= (char)'9'))
                    }

                    Date dt = new Date().parse("dd/MMM/yyyy:hh:mm:ss Z", (String)result.dt)

                    String urlOriginal = result.url
                    List urls = urlOriginal.split(' ')
                    String httpMethod = urls[0]
                    String url = urls[1]
                    String httpProtocolVersion = urls[2]

                    Long answer = Long.parseLong((String)result.answer)

                    Long sizeBytes = Long.parseLong((String)result.size)

                    String t1Str = result.t1
                    Double t1Ms = (t1Str == '-') ? null : (long)(Double.parseDouble(t1Str) * 1000)

                    String t2Str = result.t2
                    Double t2Ms = (t2Str == '-') ? null : (long)(Double.parseDouble(t2Str) * 1000)

                    sql.executeInsert("""
                          INSERT INTO logs2 (
                            user, is_admin, dt,
                            http_method, url, http_protocol_version,
                            answer, size_bytes, t1_ms, t2_ms)
                          VALUES (
                            ?, ?, ?,
                            ?, ?, ?,
                            ?, ?, ?, ?)
                            """,
                            [user, isAdmin, dt,
                             httpMethod, url, httpProtocolVersion,
                             answer, sizeBytes, t1Ms, t2Ms]
                    )
                } catch (Exception e) {
                    println "exception occurred when proceed rows with id=${result.id}"
                    e.printStackTrace()
                }
            }
            offset += maxRows
            results = sql.rows("SELECT * FROM logs", offset, maxRows)

            println "${new Date()} offset=$offset"
        }

        println 'finish'
    }

}
