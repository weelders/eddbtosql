package com.weelders.eddbtosql

import com.google.gson.Gson

//Get commoditites list from https://eddb.io/archive/v6/commodities.json
fun updateCommodities(): List<Commodities>
{
        val json = sendGetOkHttpRequest(URL_COMMODITIES)
        val result = Gson().fromJson(json,Array<Commodities>::class.java).toList()
        return result

}

fun updateFactions():List<Factions>
{
        val json = sendGetOkHttpRequest(URL_FACTIONS)
        val result = Gson().fromJson(json,Array<Factions>::class.java).toList()
        return result
}

fun updateSystemPops():List<SystemPops>
{
        val json = sendGetOkHttpRequest(URL_SYSTEMSPOP)
        val result = Gson().fromJson(json,Array<SystemPops>::class.java).toList()
        return result
}