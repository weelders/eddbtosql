package com.weelders.eddbtosql

data class Commodities(
        val id: Long,
        val name: String,
        val category_id: Int,
        val average_price: Int,
        val is_rare: Int,
        val max_buy_price: Int,
        val max_sell_price: Int,
        val min_buy_price: Int,
        val min_sell_price: Int,
        val buy_price_lower_average: Int,
        val sell_price_upper_average: Int,
        val is_non_marketable: Int,
        val ed_id: Int,
        val category: Category
)

data class Category(
        val id: Int,
        val name: String
)

///////////////////////////////////////////////////////

data class Factions(

        val id: Long,
        val name: String,
        val updated_at: Int,
        val government_id: Int,
        val government: String,
        val allegiance_id: Int,
        val allegiance: String,
        val home_system_id: Int,
        val is_player_faction: Boolean
)

///////////////////////////////////////////////////////


data class States(

        val id: Int,
        val name: String
)

data class Minor_faction_presences(

        val happiness_id: Int,
        val minor_faction_id: Int,
        val influence: Double,
        val active_states: List<Active_states>,
        //???? Any for not crash ???? TODO
        val pending_states: List<Any>,
        val recovering_states: List<Any>
)

data class Active_states(

        val id: Int,
        val name: String
)

//Temporary desactivated Lists TODO
data class SystemPops(

        val id: Int,
        val edsm_id: Int,
        val name: String,
        val x: Double,
        val y: Double,
        val z: Double,
        val population: Long,
        val is_populated: Boolean,
        val government_id: Int,
        val government: String,
        val allegiance_id: Int,
        val allegiance: String,
        //val states : List<States>,
        val security_id: Int,
        val security: String,
        val primary_economy_id: Int,
        val primary_economy: String,
        val power: String,
        val power_state: String,
        val power_state_id: Int,
        val needs_permit: Boolean,
        val updated_at: Int,
        val simbad_ref: String,
        val controlling_minor_faction_id: Int,
        val controlling_minor_faction: String,
        val reserve_type_id: Int,
        val reserve_type: String,
        // val minor_faction_presences : List<Minor_faction_presences>,
        val ed_system_address: Long
)