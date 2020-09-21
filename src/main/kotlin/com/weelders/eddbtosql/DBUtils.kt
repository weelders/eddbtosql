package com.weelders.eddbtosql

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import kotlin.math.round

interface CommoditiesDaoI
{
    fun createTable(): Int
    fun save(commodities: Commodities): Int
    fun deleteTable(): Int
}

interface FactionsDaoI
{
    fun createTable(): Int
    fun save(factions: Factions): Int
    fun deleteTable(): Int
}

interface SystemPopsDaoI
{
    fun createTable(): Int
    fun save(systemPops: SystemPops): Int
    fun deleteTable(): Int
    fun getListNames(): List<String>
    fun getSystemByName(name: String): SystemPops?
    fun getSystemsByDistance(name: String, distance: Int): List<SystemPopsDistance>
}

interface StationsDaoI
{
    fun createTable(): Int
    fun save(stations: Stations): Int
    fun deleteTable(): Int
    fun getStationsBySystemId(systemId: Int): List<Stations>
}

@Repository
open class CommoditiesDao : CommoditiesDaoI
{
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    //Override methode createTable and inject SQL create table
    override fun createTable(): Int = jdbcTemplate.update("create table commodities(" +
            "   id                       INTEGER  NOT NULL PRIMARY KEY " +
            "  ,name                     VARCHAR(50) NOT NULL" +
            "  ,category_id              INTEGER  NOT NULL" +
            "  ,average_price            INTEGER  NOT NULL" +
            "  ,is_rare                  BIT  NOT NULL" +
            "  ,max_buy_price            INTEGER  NOT NULL" +
            "  ,max_sell_price           INTEGER  NOT NULL" +
            "  ,min_buy_price            INTEGER  NOT NULL" +
            "  ,min_sell_price           INTEGER  NOT NULL" +
            "  ,buy_price_lower_average  INTEGER  NOT NULL" +
            "  ,sell_price_upper_average INTEGER  NOT NULL" +
            "  ,is_non_marketable        BIT  NOT NULL" +
            "  ,ed_id                    INTEGER  NOT NULL" +
            ")")

    //Override methode save and inject SQL insert into with data from EDDB
    override fun save(commodities: Commodities) = jdbcTemplate.update("insert into commodities(id,name,category_id,average_price,is_rare,max_buy_price,max_sell_price,min_buy_price,min_sell_price,buy_price_lower_average,sell_price_upper_average,is_non_marketable,ed_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?)",
            commodities.id, commodities.name, commodities.category_id, commodities.average_price, commodities.is_rare, commodities.max_buy_price, commodities.max_sell_price, commodities.min_buy_price, commodities.min_sell_price, commodities.buy_price_lower_average, commodities.sell_price_upper_average, commodities.is_non_marketable, commodities.ed_id)

    //Override methode deleteTable and SQL drop table, needed for update with raw table
    override fun deleteTable() = jdbcTemplate.update("drop table commodities")
}

@Repository
open class FactionsDao : FactionsDaoI
{
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    //Override methode createTable and inject SQL create table
    override fun createTable(): Int = jdbcTemplate.update("create table factions(" +
            "   id                INTEGER  NOT NULL PRIMARY KEY " +
            "  ,name              VARCHAR(100)" +
            "  ,updated_at        INTEGER " +
            "  ,government_id     INTEGER " +
            "  ,government        VARCHAR(100)" +
            "  ,allegiance_id     INTEGER " +
            "  ,allegiance        VARCHAR(100)" +
            "  ,home_system_id    INTEGER " +
            "  ,is_player_faction BOOLEAN" +
            ")")

    //Override methode save and inject SQL insert into with data from EDDB
    override fun save(factions: Factions) = jdbcTemplate.update("insert into factions(id,name,updated_at,government_id,government,allegiance_id,allegiance,home_system_id,is_player_faction) values (?,?,?,?,?,?,?,?,?);",
            factions.id, factions.name, factions.updated_at, factions.government_id, factions.government, factions.allegiance_id, factions.allegiance, factions.home_system_id, factions.is_player_faction)

    //Override methode deleteTable and SQL drop table, needed for update with raw table
    override fun deleteTable() = jdbcTemplate.update("drop table factions")
}

@Repository
open class SystemPopsDao : SystemPopsDaoI
{
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    val systemPopsRowMapper = RowMapper { it: ResultSet, rowNum: Int ->
        SystemPops(
                it.getInt("id"),
                it.getInt("edsm_id"),
                it.getString("name"),
                it.getDouble("x"),
                it.getDouble("y"),
                it.getDouble("z"),
                it.getLong("population"),
                it.getBoolean("is_populated"),
                it.getInt("government_id"),
                it.getString("government"),
                it.getInt("allegiance_id"),
                it.getString("allegiance"),
                it.getInt("security_id"),
                it.getString("security"),
                it.getInt("primary_economy_id"),
                it.getString("primary_economy"),
                it.getString("power"),
                it.getString("power_state"),
                it.getInt("power_state_id"),
                it.getBoolean("needs_permit"),
                it.getLong("updated_at"),
                it.getString("simbad_ref"),
                it.getInt("controlling_minor_faction_id"),
                it.getString("controlling_minor_faction"),
                it.getInt("reserve_type_id"),
                it.getString("reserve_type"),
                it.getLong("ed_system_address")
        )
    }

    //Override methode createTable and inject SQL create table
    override fun createTable(): Int = jdbcTemplate.update("CREATE TABLE SystemPops(" +
            "   id                              INTEGER NOT NULL PRIMARY KEY " +
            "  ,edsm_id                         INTEGER" +
            "  ,name                            VARCHAR(100) " +
            "  ,x                               DECIMAL(9,3) " +
            "  ,y                               DECIMAL(9,3) " +
            "  ,z                               DECIMAL(9,3) " +
            "  ,population                      BIGINT " +
            "  ,is_populated                    BOOLEAN " +
            "  ,government_id                   INTEGER " +
            "  ,government                      VARCHAR(100) " +
            "  ,allegiance_id                   INTEGER " +
            "  ,allegiance                      VARCHAR(100) " +
            "  ,security_id                     INTEGER " +
            "  ,security                        VARCHAR(50) " +
            "  ,primary_economy_id              INTEGER " +
            "  ,primary_economy                 VARCHAR(50) " +
            "  ,power                           VARCHAR(100) " +
            "  ,power_state                     VARCHAR(50) " +
            "  ,power_state_id                  INTEGER " +
            "  ,needs_permit                    BOOLEAN " +
            "  ,updated_at                      BIGINT " +
            "  ,simbad_ref                      VARCHAR(100) " +
            "  ,controlling_minor_faction_id    INTEGER " +
            "  ,controlling_minor_faction       VARCHAR(100) " +
            "  ,reserve_type_id                 INTEGER " +
            "  ,reserve_type                    VARCHAR(50) " +
            "  ,ed_system_address               BIGINT " +
            ")")

    //Override methode save and inject SQL insert into with data from EDDB
    override fun save(systemPops: SystemPops) = jdbcTemplate.update("INSERT INTO systempops (id, edsm_id, name, x, y, z, population, is_populated, government_id, government, allegiance_id, allegiance, security_id, security, primary_economy_id, primary_economy, power, power_state, power_state_id, needs_permit, updated_at, simbad_ref, controlling_minor_faction_id, controlling_minor_faction, reserve_type_id, reserve_type, ed_system_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
            systemPops.id, systemPops.edsm_id, systemPops.name, systemPops.x, systemPops.y, systemPops.z, systemPops.population, systemPops.is_populated, systemPops.government_id, systemPops.government, systemPops.allegiance_id, systemPops.allegiance, systemPops.security_id, systemPops.security, systemPops.primary_economy_id, systemPops.primary_economy, systemPops.power, systemPops.power_state, systemPops.power_state_id, systemPops.needs_permit, systemPops.updated_at, systemPops.simbad_ref, systemPops.controlling_minor_faction_id, systemPops.controlling_minor_faction, systemPops.reserve_type_id, systemPops.reserve_type, systemPops.ed_system_address)


    //Override methode deleteTable and SQL drop table, needed for update with raw table
    override fun deleteTable() = jdbcTemplate.update("drop table SystemPops")

    //Todo remove this and make a full version & return a list String only
    override fun getListNames(): List<String>
    {
        val list = jdbcTemplate.query("SELECT * FROM systempops", systemPopsRowMapper)
        val listReturned = mutableListOf<String>()
        list.forEach { listReturned.add(it.name) }
        return listReturned
    }

    override fun getSystemByName(name: String): SystemPops?
    {
        return jdbcTemplate.queryForObject("SELECT * FROM systempops WHERE name = '$name'", systemPopsRowMapper)
    }

    override fun getSystemsByDistance(name: String, distance: Int): List<SystemPopsDistance>
    {
        val focusSystem = getSystemByName(name) ?: throw Exception("System Not exist")
        val listSystem = jdbcTemplate.query("SELECT * FROM systempops", systemPopsRowMapper)
        var list = mutableListOf<SystemPopsDistance>()
        listSystem.forEach { if ((round(distance3DCalculation(focusSystem.x, focusSystem.y, focusSystem.z, it.x, it.y, it.z) * 100) / 100) <= distance.toDouble()) list.add(SystemPopsDistance(it, (round(distance3DCalculation(focusSystem.x, focusSystem.y, focusSystem.z, it.x, it.y, it.z) * 100) / 100))) }
        return list.sortedBy { it.distance }
    }

}

@Repository
open class StationsDao : StationsDaoI
{
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    val stationRowMapper = RowMapper { it: ResultSet, rowNum: Int ->
        Stations(
                it.getInt("id"),
                it.getString("name"),
                it.getInt("system_id"),
                it.getLong("updated_at"),
                it.getString("max_landing_pad_size"),
                it.getInt("distance_to_star"),
                it.getInt("government_id"),
                it.getString("government"),
                it.getInt("allegiance_id"),
                it.getString("allegiance_id"),
                Gson().fromJson(it.getString("states"), Array<States>::class.java).toList(),
                it.getInt("type_id"),
                it.getString("type"),
                it.getBoolean("has_blackmarket"),
                it.getBoolean("has_market"),
                it.getBoolean("has_refuel"),
                it.getBoolean("has_repair"),
                it.getBoolean("has_rearm"),
                it.getBoolean("has_outfitting"),
                it.getBoolean("has_shipyard"),
                it.getBoolean("has_docking"),
                it.getBoolean("has_commodities"),
                Gson().fromJson(it.getString("import_commodities"), Array<String>::class.java).toList(),
                Gson().fromJson(it.getString("export_commodities"), Array<String>::class.java).toList(),
                Gson().fromJson(it.getString("prohibited_commodities"), Array<String>::class.java).toList(),
                Gson().fromJson(it.getString("economies"), Array<String>::class.java).toList(),
                it.getLong("shipyard_updated_at"),
                it.getLong("outfitting_updated_at"),
                it.getLong("market_updated_at"),
                it.getBoolean("is_planetary"),
                Gson().fromJson(it.getString("selling_ships"), Array<String>::class.java).toList(),
                Gson().fromJson(it.getString("selling_modules"), Array<Int>::class.java).toList(),
                it.getString("settlement_size_id"),
                it.getString("settlement_size"),
                it.getString("settlement_security_id"),
                it.getString("settlement_security"),
                it.getInt("body_id"),
                it.getInt("controlling_minor_faction_id"),
                it.getLong("ed_market_id")
        )

    }

    //Override methode createTable and inject SQL create table
    override fun createTable(): Int = jdbcTemplate.update("CREATE TABLE Stations (" +
            "id                                     INTEGER NOT NULL PRIMARY KEY," +
            "name                                   VARCHAR(100)," +
            "system_id                              INTEGER," +
            "updated_at                             BIGINT," +
            "max_landing_pad_size                   VARCHAR(10)," +
            "distance_to_star                       INTEGER," +
            "government_id                          INTEGER," +
            "government                             VARCHAR(100)," +
            "allegiance_id                          INTEGER," +
            "allegiance                             VARCHAR(100)," +
            "states                                 LONGTEXT," +
            "type_id                                INTEGER," +
            "type                                   VARCHAR(50)," +
            "has_blackmarket                        BOOLEAN," +
            "has_market                             BOOLEAN," +
            "has_refuel                             BOOLEAN," +
            "has_repair                             BOOLEAN," +
            "has_rearm                              BOOLEAN," +
            "has_outfitting                         BOOLEAN," +
            "has_shipyard                           BOOLEAN," +
            "has_docking                            BOOLEAN," +
            "has_commodities                        BOOLEAN," +
            "import_commodities                     LONGTEXT," +
            "export_commodities                     LONGTEXT," +
            "prohibited_commodities                 LONGTEXT," +
            "economies                              LONGTEXT," +
            "shipyard_updated_at                    BIGINT," +
            "outfitting_updated_at                  BIGINT," +
            "market_updated_at                      BIGINT," +
            "is_planetary                           BOOLEAN," +
            "selling_ships                          LONGTEXT," +
            "selling_modules                        LONGTEXT," +
            "settlement_size_id                     INTEGER," +
            "settlement_size                        VARCHAR(50)," +
            "settlement_security_id                 INTEGER," +
            "settlement_security                    VARCHAR(50)," +
            "body_id                                INTEGER," +
            "controlling_minor_faction_id           INTEGER," +
            "ed_market_id                           BIGINT" +
            ")")

    //Override methode save and inject SQL insert into with data from EDDB
    override fun save(stations: Stations) = jdbcTemplate.update("INSERT INTO stations (id, name, system_id, updated_at, max_landing_pad_size, distance_to_star, government_id, government, allegiance_id, allegiance, states, type_id, type, has_blackmarket, has_market, has_refuel, has_repair, has_rearm, has_outfitting, has_shipyard, has_docking, has_commodities, import_commodities, export_commodities, prohibited_commodities, economies, shipyard_updated_at, outfitting_updated_at, market_updated_at, is_planetary, selling_ships, selling_modules, settlement_size_id, settlement_size, settlement_security_id, settlement_security, body_id, controlling_minor_faction_id, ed_market_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
            stations.id, stations.name, stations.system_id, stations.updated_at, stations.max_landing_pad_size, stations.distance_to_star, stations.government_id, stations.government, stations.allegiance_id, stations.allegiance, Gson().toJson(stations.states), stations.type_id, stations.type, stations.has_blackmarket, stations.has_market, stations.has_refuel, stations.has_repair, stations.has_rearm, stations.has_outfitting, stations.has_shipyard, stations.has_docking, stations.has_commodities, Gson().toJson(stations.import_commodities), Gson().toJson(stations.export_commodities), Gson().toJson(stations.prohibited_commodities), Gson().toJson(stations.economies), stations.shipyard_updated_at, stations.outfitting_updated_at, stations.market_updated_at, stations.is_planetary, Gson().toJson(stations.selling_ships), Gson().toJson(stations.selling_modules), stations.settlement_size_id, stations.settlement_size, stations.settlement_security_id, stations.settlement_security, stations.body_id, stations.controlling_minor_faction_id, stations.ed_market_id)


    //Override methode deleteTable and SQL drop table, needed for update with raw table
    override fun deleteTable() = jdbcTemplate.update("drop table stations")

    override fun getStationsBySystemId(systemId: Int): List<Stations>
    {
        return jdbcTemplate.query("SELECT * FROM stations WHERE system_id = '$systemId'", stationRowMapper).toList()
    }
}