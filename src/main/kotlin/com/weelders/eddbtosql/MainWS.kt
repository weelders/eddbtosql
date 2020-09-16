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

    @Autowired
    lateinit var systemPopsDaoI: SystemPopsDaoI


    @GetMapping("/test")
    fun testMethode(): String
    {
        println("/test")
        return "Server Status: OK"
    }

    @GetMapping("/update")
    fun updateAll(): String
    {
        println("/update")
        val commoditieslist = updateCommodities()
        GlobalScope.launch {
            //Drop table Commodities
            commoditiesDaoI.deleteTable()
            //Create table Commodities empty
            commoditiesDaoI.createTable()
            //Fill table Commoditites with https://eddb.io/archive/v6/commodities.json (~400 input,~120kb)
            commoditieslist.forEach { this.launch { commoditiesDaoI.save(it) } }
        }

        val factionslist = updateFactions()
        GlobalScope.launch {
            //Drop table Factions
            factionsDaoI.deleteTable()
            //Create table Factions empty
            factionsDaoI.createTable()
            //Fill table Factions with /!\HUDGE JSON/!\ https://eddb.io/archive/v6/factions.json (~80k input,~15_500kb)
            factionslist.forEach { this.launch { factionsDaoI.save(it) } }
        }

        val systempoplist = updateSystemPops()
        GlobalScope.launch {
            //Drop table SystemPops
            systemPopsDaoI.deleteTable()
            //Create table SystemPops empty
            systemPopsDaoI.createTable()
            //Fill table SystemPops with /!\HUDGE JSON/!\ https://eddb.io/archive/v6/systems_populated.json(~20k input,~33_500kb)
            systempoplist.forEach { this.launch { systemPopsDaoI.save(it) } }
        }
        return "Dump Succefully Launched"
    }
}