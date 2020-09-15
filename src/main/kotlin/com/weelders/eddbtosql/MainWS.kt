package com.weelders.eddbtosql

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainWS
{
    //Use interface for DAO
    @Autowired
    lateinit var commoditiesDaoI: CommoditiesDaoI
    @Autowired
    lateinit var factionsDaoI: FactionsDaoI


    @GetMapping("/test")
    fun testMethode(): String
    {
        println("/test")
        return "I'm ok !"
    }

    @GetMapping("/update")
    fun updateAll(): String
    {
        println("/update")
        val commoditieslist = updateCommodities()
            //Use coroutine for large data insertion
            GlobalScope.launch {
                //Drop table commodities
                commoditiesDaoI.deleteTable()
                //Create table commodities empty
                commoditiesDaoI.createTable()
                //Fill table commoditites with https://eddb.io/archive/v6/commodities.json (~400 input)
                commoditieslist.forEach { commoditiesDaoI.save(it)}
                println("Commodities Saved")
            }

       /* val factionslist = updateFactions()
        GlobalScope.launch {
            factionsDaoI.deleteTable()
            factionsDaoI.createTable()
            factionslist.forEach { factionsDaoI.save(it) }
            println("Factions saved")
        }*/
        return "Saved !"
    }
}