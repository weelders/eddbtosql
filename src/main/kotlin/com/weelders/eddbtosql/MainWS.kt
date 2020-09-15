package com.weelders.eddbtosql

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainWS
{
    @Autowired
    lateinit var commoditiesDaoI: CommoditiesDaoI


    @GetMapping("/test")
    fun testMethode(): String
    {
        println("/test")
        return "I'm ok !"
    }

    @GetMapping("/update")
    fun pdateAll(): String
    {
        println("/update")
        val comoditieslist = updateCommodities()
        var count = 0

            GlobalScope.launch {
                comoditieslist.forEach { commoditiesDaoI.save(it);println(count);count++ }
            }

        println("Commodities Saved")
        return "Saved !"
    }
}