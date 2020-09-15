package com.weelders.eddbtosql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

interface CommoditiesDaoI {
    fun save(comodities: Comodities): Int
}

@Repository
open class CommoditiesDao : CommoditiesDaoI
{
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    override fun save(comodities: Comodities) = jdbcTemplate.update("insert into commodities(id,name,category_id,average_price,is_rare,max_buy_price,max_sell_price,min_buy_price,min_sell_price,buy_price_lower_average,sell_price_upper_average,is_non_marketable,ed_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?)",
            comodities.id,comodities.name,comodities.category_id,comodities.average_price,comodities.is_rare,comodities.max_buy_price,comodities.max_sell_price,comodities.min_buy_price,comodities.min_sell_price,comodities.buy_price_lower_average,comodities.sell_price_upper_average,comodities.is_non_marketable,comodities.ed_id)

    
}