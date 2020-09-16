package com.weelders.eddbtosql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

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

    //Override methode createTable and inject SQL create table
    override fun createTable(): Int = jdbcTemplate.update("CREATE TABLE SystemPops(" +
            "   id                              INTEGER NOT NULL PRIMARY KEY " +
            "  ,edsm_id                         INTEGER" +
            "  ,name                            VARCHAR(100) " +
            "  ,x                               DECIMAL " +
            "  ,y                               DECIMAL" +
            "  ,z                               DECIMAL " +
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
}