package com.weelders.eddbtosql

data class Commodities (
        val id : Long,
        val name : String,
        val category_id : Int,
        val average_price : Int,
        val is_rare : Int,
        val max_buy_price : Int,
        val max_sell_price : Int,
        val min_buy_price : Int,
        val min_sell_price : Int,
        val buy_price_lower_average : Int,
        val sell_price_upper_average : Int,
        val is_non_marketable : Int,
        val ed_id : Int,
        val category : Category
)

data class Category (
        val id : Int,
        val name : String
)

data class Factions (

        val id : Long,
        val name : String,
        val updated_at : Int,
        val government_id : Int,
        val government : String,
        val allegiance_id : Int,
        val allegiance : String,
        val home_system_id : Int,
        val is_player_faction : Boolean
)