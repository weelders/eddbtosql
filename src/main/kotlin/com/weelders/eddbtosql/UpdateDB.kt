package com.weelders.eddbtosql

import com.google.gson.Gson

fun updateCommodities(): List<Comodities>
{
        val json = sendGetOkHttpRequest(URL_COMMODITIES)
        val result = Gson().fromJson(json,Array<Comodities>::class.java).toList()
        return result

}