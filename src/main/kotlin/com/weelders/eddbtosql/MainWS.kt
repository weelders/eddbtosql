package com.weelders.eddbtosql

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.math.round

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

    @Autowired
    lateinit var stationsDaoI: StationsDaoI

    //Ping server
    @GetMapping("/test")
    fun testMethode(): String
    {
        traceServerRequest("/test")
        return "Server Status: OK"
    }

    //Get informations from EDDB and push them in DB, /!\TAKE ~20min/!\
    @GetMapping("/update")
    fun updateAll(): String
    {
        traceServerRequest("/update")
        runBlocking {
            traceUpdate("Update/Commodities", "Starting List Building...")
            val commoditieslist = updateCommodities()
            traceUpdate("Update/Commodities", "List Building Finish.")


            traceUpdate("Update/Commodities", "Drop Table...")
            commoditiesDaoI.deleteTable()
            traceUpdate("Update/Commodities", "Create Empty Table...")
            commoditiesDaoI.createTable()
            //Fill table Commoditites with https://eddb.io/archive/v6/commodities.json (~400 input,~120kb)
            traceUpdate("Update/Commodities", "Init Filling Table...")
            commoditieslist.forEach {
                GlobalScope.launch { commoditiesDaoI.save(it) }
            }
            traceUpdate("Update/Commodities", "Succefully Add Data to Table.")



            traceUpdate("Update/Factions", "Starting List Building...")
            val factionslist = updateFactions()
            traceUpdate("Update/Factions", "List Building Finish.")
            traceUpdate("Update/Factions", "Drop Table...")
            factionsDaoI.deleteTable()
            traceUpdate("Update/Factions", "Create Empty Table...")
            factionsDaoI.createTable()
            traceUpdate("Update/Factions", "Init Filling Table...")
            //Fill table Factions with /!\HUGE JSON/!\ https://eddb.io/archive/v6/factions.json (~70k input,~15_500kb)
            factionslist.forEach {
                GlobalScope.launch { factionsDaoI.save(it) }
            }

            traceUpdate("Update/Factions", "Succefully Add Data to Table.")




            traceUpdate("Update/SystemPops", "Starting List Building...")
            val systempoplist = updateSystemPops()
            traceUpdate("Update/SystemPops", "List Building Finish.")
            traceUpdate("Update/SystemPops", "Drop Table...")
            systemPopsDaoI.deleteTable()
            traceUpdate("Update/SystemPops", "Create Empty Table...")
            systemPopsDaoI.createTable()
            traceUpdate("Update/SystemPops", "Init Filling Table...")
            //Fill table SystemPops with /!\HUGE JSON/!\ https://eddb.io/archive/v6/systems_populated.json(~20k input,~33_500kb)
            systempoplist.forEach {
                GlobalScope.launch { systemPopsDaoI.save(it) }
            }

            traceUpdate("Update/SystemPops", "Succefully Add Data to Table.")



            traceUpdate("Update/Stations", "Starting List Building...")
            val stationslist = updateStations()
            traceUpdate("Update/Stations", "List Building Finish.")
            traceUpdate("Update/Stations", "Drop Table...")
            stationsDaoI.deleteTable()
            traceUpdate("Update/Stations", "Create Empty Table...")
            stationsDaoI.createTable()
            traceUpdate("Update/Stations", "Init Filling Table...")
            //Fill table Stations with /!\HUGE JSON/!\ https://eddb.io/archive/v6/stations.json(~54k input,~124_000kb)
            stationslist.forEach {
                GlobalScope.launch { stationsDaoI.save(it) }
            }
            traceUpdate("Update/Stations", "Succefully Add Data to Table.")
        }

        return "Dump Succefully Done"
    }

    //Return an List String of SystemsNames
    @GetMapping("/getSystemsNames")
    fun getSystemsNames(): List<String>
    {
        traceServerRequest("/getSystemsNames")
        return systemPopsDaoI.getListNames()
    }

    //Get 2 names and Return Distance (double) between 2 Systems
    @GetMapping("/getDistance")
    fun getDistanceByNames(@RequestParam name1: String, @RequestParam name2: String): Any
    {
        traceServerRequest("/getDistance?name1=$name1&name2=$name2")
        try
        {
            //Get System 1 by this name
            val systemPops1 = systemPopsDaoI.getSystemByName(userInputCheck(name1))
            //Get System 2 by this name
            val systemPops2 = systemPopsDaoI.getSystemByName(userInputCheck(name2))
            if (systemPops1 != null && systemPops2 != null)
            {
                //Return double distance in "xxxx,xx" format
                return round(distance3DCalculation(systemPops1.x, systemPops1.y, systemPops1.z, systemPops2.x, systemPops2.y, systemPops2.z) * 100) / 100
            }
            else return "Error with '$name1' or '$name2', please try again"
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            return "Error with '$name1' or '$name2', please try again"
        }
    }

    @GetMapping("/getShips")
    fun getShips(@RequestParam name: String, @RequestParam distance: Int, @RequestParam ship: String): Any
    {
        traceServerRequest("/getDistance?name=$name&distance=$distance&ship=$ship")
        val listSystem = systemPopsDaoI.getSystemsByDistance(name, distance)
        var listStations = mutableListOf<Stations>()
        var listComplexeStations = mutableListOf<ComplexeStations>()

        listSystem.forEach {
            stationsDaoI.getStationsBySystemId(it.systemPops.id).forEach {
                if (it.has_docking && it.selling_ships.isNotEmpty()) listStations.add(it)
            }
        }
        listStations = listStations.filter { it.selling_ships.any { it == ship } }.toMutableList()
        traceUpdate("/getShips", "Return ${listStations.size} result")
        return listStations
    }
}