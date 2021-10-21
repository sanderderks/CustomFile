package me.sd_master92.customfile

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.io.File
import java.util.*
import java.util.stream.Collectors

/**
 * create a new PlayerFile instance
 *
 * @param uuid   uuid of this player
 * @param plugin main plugin class
 */
open class PlayerFile(var uuid: String, plugin: Plugin) :
    CustomFile(File(plugin.dataFolder.toString() + File.separator + "players"), "$uuid.yml", plugin)
{
    /**
     * create a new PlayerFile instance
     * (this will automatically save all possible player properties)
     *
     * @param player the player
     * @param plugin main plugin class
     */
    constructor(player: Player, plugin: Plugin) : this(player.uniqueId.toString(), plugin)
    {
        name = player.name
    }

    /**
     * save first join timestamp
     */
    fun setFirstJoinTime()
    {
        if (getTimeStamp("first_join") == 0L)
        {
            setTimeStamp("first_join")
        }
    }

    /**
     * get first join timestamp
     */
    val firstJoinTime: Long
        get() = getTimeStamp("first_join")

    /**
     * get the name of the player
     *
     * @return name or "unknown"
     */
    override fun getName(): String
    {
        val name = getString("name")
        return name ?: "unknown"
    }

    /**
     * save the name of the player
     *
     * @param name player name
     * @return successful or not
     */
    fun setName(name: String): Boolean
    {
        set("name", name)
        return saveConfig()
    }

    companion object
    {
        /**
         * get a PlayerFile by player name
         *
         * @param name   player name
         * @param plugin main plugin class
         * @return PlayerFile or null
         */
        fun getByName(name: String, plugin: Plugin): PlayerFile?
        {
            return getAll(plugin).stream()
                .filter { playerFile: PlayerFile -> playerFile.name.equals(name, ignoreCase = true) }
                .findFirst().orElse(null)
        }

        /**
         * get all PlayerFiles
         *
         * @param plugin main plugin class
         * @return empty or filled list of PlayerFiles
         */
        fun getAll(plugin: Plugin): List<PlayerFile>
        {
            val files = File(plugin.dataFolder.toString() + File.separator + "players").listFiles()
            return if (files != null)
            {
                Arrays.stream(files).map { file: File -> PlayerFile(file.name.replace(".yml", ""), plugin) }
                    .collect(Collectors.toList())
            } else ArrayList()
        }
    }

    init
    {
        setFirstJoinTime()
    }
}