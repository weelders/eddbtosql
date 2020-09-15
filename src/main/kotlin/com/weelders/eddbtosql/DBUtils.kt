package com.weelders.eddbtosql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

interface CommoditiesDaoI {
    fun createTable():Int
    fun save(comodities: Comodities): Int
    fun deleteTable():Int
}

@Repository
open class CommoditiesDao : CommoditiesDaoI
{
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    //Override methode createTable and inject SQL create table
    override fun createTable(): Int = jdbcTemplate.update("create table commodities(" +
            "   id                       INTEGER  NOT NULL PRIMARY KEY " +
            "  ,name                     VARCHAR(50) NOT NULL" +
            "  ,category_id              INTEGER  NOT NULL" +
            "  ,average_price            INTEGER  NOT NULL" +
            "  ,is_rare                  BIT  NOT NULL" +
            "  ,max_buy_price            INTEGER  NOT NULL" +
            "  ,max_sell_price           INTEGER  NOT NULL" +
            "  ,min_buy_price            INTEGER  NOT NULL" +
            "  ,min_sell_price           INTEGER  NOT NULL" +
            "  ,buy_price_lower_average  INTEGER  NOT NULL" +
            "  ,sell_price_upper_average INTEGER  NOT NULL" +
            "  ,is_non_marketable        BIT  NOT NULL" +
            "  ,ed_id                    INTEGER  NOT NULL" +
            ")")

    //Override methode save and inject SQL insert into with data from EDDB
    override fun save(comodities: Comodities) = jdbcTemplate.update("insert into commodities(id,name,category_id,average_price,is_rare,max_buy_price,max_sell_price,min_buy_price,min_sell_price,buy_price_lower_average,sell_price_upper_average,is_non_marketable,ed_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?)",
            comodities.id,comodities.name,comodities.category_id,comodities.average_price,comodities.is_rare,comodities.max_buy_price,comodities.max_sell_price,comodities.min_buy_price,comodities.min_sell_price,comodities.buy_price_lower_average,comodities.sell_price_upper_average,comodities.is_non_marketable,comodities.ed_id)

    //Override methode deleteTable and SQL drop table, needed for update with raw table
    override fun deleteTable() = jdbcTemplate.update("drop table commodities")
}