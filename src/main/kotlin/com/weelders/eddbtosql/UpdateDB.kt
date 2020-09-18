package com.weelders.eddbtosql

import com.google.gson.Gson

//Get commoditites list from https://eddb.io/archive/v6/commodities.json
fun updateCommodities(): List<Commodities>
{
    val json = sendGetOkHttpRequest(URL_COMMODITIES)
    val result = Gson().fromJson(json, Array<Commodities>::class.java).toList()
    return result
}

//Get Factions list from https://eddb.io/archive/v6/factions.json /!\HUGE JSON/!\
fun updateFactions(): List<Factions>
{
    val json = sendGetOkHttpRequest(URL_FACTIONS)
    val result = Gson().fromJson(json, Array<Factions>::class.java).toList()
    return result
}

//Get SystemPops List from https://eddb.io/archive/v6/systems_populated.json /!\HUGE JSON/!\
fun updateSystemPops(): List<SystemPops>
{
    val json = sendGetOkHttpRequest(URL_SYSTEMSPOP)
    val result = Gson().fromJson(json, Array<SystemPops>::class.java).toList()
    return result
}

//Get Stations list from https://eddb.io/archive/v6/stations.json /!\HUGE JSON/!\
fun updateStations(): List<Stations>
{
    val json = sendGetOkHttpRequest(URL_STATIONS)
    val result = Gson().fromJson(json, Array<Stations>::class.java).toList()
    return result
}