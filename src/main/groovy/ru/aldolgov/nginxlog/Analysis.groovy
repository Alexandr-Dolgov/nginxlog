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

        //сделать группировку по http_methods и посмотреть на количества
        //сохранить количество строк с нестандартными методами
        //удалить строки у которых http_method не стандартный

        //посмотреть максимальное время запроса
        //посмотреть минимальное время запроса
        //доставать для всех времен запроса с интервалом в 100мс количества запросов

        //сделать группировку по урлам (увидеть все контроллеры) и посмотреть на количества

        //для каждого урла сделать
            //посмотреть максимальное время запроса
            //посмотреть минимальное время запроса
            //доставать для всех времен запроса с интервалом в 100мс количества запросов

        //построить графики для каждого урла. По оси Х миллисекунды, по оси Y количества запросов
    }

}
