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
        val comoditieslist = updateCommodities()
            //Use coroutine for large data insertion
            GlobalScope.launch {
                //Drop table commodities
                commoditiesDaoI.deleteTable()
                //Create table commodities empty
                commoditiesDaoI.createTable()
                //Fill table commoditites with https://eddb.io/archive/v6/commodities.json (~400 input)
                comoditieslist.forEach { commoditiesDaoI.save(it)}
            }
        println("Commodities Saved")
        return "Saved !"
    }
}