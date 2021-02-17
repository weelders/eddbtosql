package com.weelders.eddbtosql

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.IncorrectResultSizeDataAccessException
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
        var debut = System.currentTimeMillis()
        runBlocking {
            traceUpdate("Update/Commodities", "Drop Table...")
            commoditiesDaoI.deleteTable()
            traceUpdate("Update/Commodities", "Create Empty Table...")
            commoditiesDaoI.createTable()
            //Fill table Commoditites with https://eddb.io/archive/v6/commodities.json (~400 input,~120kb)
            traceUpdate("Update/Commodities", "Init Filling Table...")
            launch {
                traceUpdate("Update/Commodities", "Starting List Building...")
                val commoditieslist = updateCommodities()
                traceUpdate("Update/Commodities", "List Building Finish.")
                //Ouvrir la transaction
                runBlocking {
                    commoditieslist.forEach {
                        launch { commoditiesDaoI.save(it) }
                    }
                }
                //Fermer la transaction
                traceUpdate("Update/Commodities", "Succefully Add Data to Table.")
            }





            traceUpdate("Update/Factions", "Drop Table...")
            factionsDaoI.deleteTable()
            traceUpdate("Update/Factions", "Create Empty Table...")
            factionsDaoI.createTable()
            traceUpdate("Update/Factions", "Init Filling Table...")
            //Fill table Factions with /!\HUGE JSON/!\ https://eddb.io/archive/v6/factions.json (~70k input,~15_500kb)
            launch {
                traceUpdate("Update/Factions", "Starting List Building...")
                val factionslist = updateFactions()
                traceUpdate("Update/Factions", "List Building Finish.")
                runBlocking {
                    factionslist.forEach {
                        launch { factionsDaoI.save(it) }
                    }
                }
                traceUpdate("Update/Factions", "Succefully Add Data to Table.")
            }







            traceUpdate("Update/SystemPops", "Drop Table...")
            systemPopsDaoI.deleteTable()
            traceUpdate("Update/SystemPops", "Create Empty Table...")
            systemPopsDaoI.createTable()
            traceUpdate("Update/SystemPops", "Init Filling Table...")
            //Fill table SystemPops with /!\HUGE JSON/!\ https://eddb.io/archive/v6/systems_populated.json(~20k input,~33_500kb)
            launch {
                traceUpdate("Update/SystemPops", "Starting List Building...")
                val systempoplist = updateSystemPops()
                traceUpdate("Update/SystemPops", "List Building Finish.")
                runBlocking {
                    systempoplist.forEach {
                        launch { systemPopsDaoI.save(it) }
                    }
                }
                traceUpdate("Update/SystemPops", "Succefully Add Data to Table.")
            }




            traceUpdate("Update/Stations", "Drop Table...")
            stationsDaoI.deleteTable()
            traceUpdate("Update/Stations", "Create Empty Table...")
            stationsDaoI.createTable()
            traceUpdate("Update/Stations", "Init Filling Table...")
            //Fill table Stations with /!\HUGE JSON/!\ https://eddb.io/archive/v6/stations.json(~54k input,~124_000kb)
            launch {
                traceUpdate("Update/Stations", "Starting List Building...")
                val stationslist = updateStations()
                traceUpdate("Update/Stations", "List Building Finish.")
                runBlocking {
                    stationslist.forEach {
                        launch { stationsDaoI.save(it) }
                    }
                }
                traceUpdate("Update/Stations", "Succefully Add Data to Table.")
            }
        }
        println("temps: ${(System.currentTimeMillis() - debut) / 1000}")
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
    fun getDistanceByNames(@RequestParam name1: String, @RequestParam name2: String): Any?
    {
        traceServerRequest("/getDistance?name1=$name1&name2=$name2")
        try
        {
            //Get System 1 by this name
            val systemPops1 = systemPopsDaoI.getSystemByName(userInputCheck(name1))
            //Get System 2 by this name
            val systemPops2 = systemPopsDaoI.getSystemByName(userInputCheck(name2))
            if (systemPops1.isNotEmpty() && systemPops2.isNotEmpty())
            {
                //Return double distance in "xxxx,xx" format
                val distance = round(distance3DCalculation(systemPops1[0].x, systemPops1[0].y, systemPops1[0].z, systemPops2[0].x, systemPops2[0].y, systemPops2[0].z) * 100) / 100
                traceUpdate("/getDistance", "Return $distance AL between $name1 and $name2")
                return distance
            } else
            {
                traceUpdate("/getDistance", "Name '$name1' or name '$name2' is incorrect or data doesnt exist.")
                return "Name '$name1' or name '$name2' is incorrect or data doesnt exist."
            }
        } catch (e: IncorrectResultSizeDataAccessException)
        {
            traceUpdate("/getDistance", "Name '$name1' or name '$name2' is incorrect or data doesnt exist.")
            return "Name '$name1' or name '$name2' is incorrect or data doesnt exist."
        } catch (e: Exception)
        {
            e.printStackTrace()
            return e.message
        }
    }

    //need optimisation, too slow when distance > 20
//    @GetMapping("/getShips")
//    fun getShips(@RequestParam name: String, @RequestParam distance: Int, @RequestParam ship: String): Any
//    {
//        traceServerRequest("/getShips?name=$name&distance=$distance&ship=$ship")
//        //Get all systems where distance < $distance around $name
//        val listSystem = systemPopsDaoI.getSystemsByDistance(name, distance)
//        var listComplexeStations = mutableListOf<ComplexeStations>()
//
//        //For each system in systemList, add him into listComplexeStation if has_Docking && sell ships
//        runBlocking {
//            listSystem.forEach {
//                GlobalScope.launch {
//                    var listStations = mutableListOf<Stations>()
//                    stationsDaoI.getStationsBySystemId(it.systemPops.id).forEach {
//                        if (it.has_docking && it.selling_ships.isNotEmpty()) listStations.add(it)
//                    }
//                    listStations = listStations.filter { it.selling_ships.any { it == ship } }.toMutableList()
//                    if (listStations.size != 0)
//                    {
//                        listComplexeStations.add(ComplexeStations(it, listStations))
//                    }
//                }.join()
//            }
//        }
//
//        traceUpdate("/getShips", "Return ${listComplexeStations.size} result")
//        //Return a list of systems who contain a list of stations
//        return listComplexeStations
//    }

    @GetMapping("/getShipStation")
    fun getShipStation(@RequestParam name: String, @RequestParam distance: Int, @RequestParam ship: String): List<ShipSystemDistance>
    {
        traceServerRequest("/getShipStation?name=$name&distance=$distance&ship=$ship")
        val focusSystem = systemPopsDaoI.getSystemByName(name)[0]
        val listSystem = systemPopsDaoI.getSystemShip(ship)
        var list = mutableListOf<ShipSystemDistance>()
        listSystem.forEach {
            if ((round(distance3DCalculation(focusSystem.x, focusSystem.y, focusSystem.z, it.x, it.y, it.z) * 100) / 100) <= distance.toDouble()
            ) list.add(ShipSystemDistance(it, (round(distance3DCalculation(focusSystem.x, focusSystem.y, focusSystem.z, it.x, it.y, it.z) * 100) / 100)))
        }
        traceUpdate("/getShipStation", "Return ${list.size} result")
        return list.sortedBy { it.distance }
    }

    //TODO Add factions
    @GetMapping("/getSystem")
    fun getSystem(@RequestParam name: String): Any?
    {
        traceServerRequest("/getSystem?name=$name")
        try
        {
            //Check UserInput Before search
            val verifiedName = userInputCheck(name)
            //Get systems by they names
            val system = systemPopsDaoI.getSystemByName(verifiedName)
            val listComplexeStations = mutableListOf<ComplexeStations>()
            system.forEach {
                //Get all station by system_id get before
                val stations = stationsDaoI.getStationsBySystemId(it.id)
                listComplexeStations.add(ComplexeStations(SystemPopsDistance(it, 0.0), stations))
            }
            traceUpdate("/getSystem?name=$name", "Return ${listComplexeStations.size} result")
            return listComplexeStations
        } catch (e: IncorrectResultSizeDataAccessException)
        {
            traceUpdate("/getSystem", "Name '$name' is incorrect or data doesnt exist.")
            return "Name '$name' is incorrect or data doesnt exist."
        } catch (e: Exception)
        {
            e.printStackTrace()
            return e.message
        }
    }
}