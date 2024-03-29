package me.sd_master92.customfile

import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.io.File

/**
 * create a new CustomFile
 *
 * @param folder folder of this CustomFile
 * @param name   name of this CustomFile
 * @param plugin main plugin class
 */
open class CustomFile(folder: File, name: String, plugin: Plugin) : YamlConfiguration()
{
    private var file: File? = null

    /**
     * create a new CustomFile
     *
     * @param name   name of this CustomFile
     * @param plugin main plugin class
     */
    constructor(name: String, plugin: Plugin) : this(plugin.dataFolder, name, plugin)
    {
    }

    /**
     * save config
     *
     * @return successful or not
     */
    fun saveConfig(): Boolean
    {
        return try
        {
            save(file!!)
            true
        } catch (e: Exception)
        {
            false
        }
    }

    /**
     * reload config
     *
     * @return successful or not
     */
    fun reloadConfig(): Boolean
    {
        return try
        {
            load(file!!)
            true
        } catch (e: Exception)
        {
            false
        }
    }

    /**
     * delete this file
     *
     * @return successful or not
     */
    fun delete(): Boolean
    {
        return file!!.delete()
    }

    /**
     * delete a path from config
     *
     * @param path config path
     * @return successful or not
     */
    fun delete(path: String): Boolean
    {
        set(path.lowercase(), null)
        return saveConfig()
    }

    /**
     * get a timestamp
     *
     * @param path config path
     * @return number
     */
    fun getTimeStamp(path: String): Long
    {
        return getLong(path.lowercase())
    }

    /**
     * save a timestamp
     *
     * @param path config path
     * @return successful or not
     */
    fun setTimeStamp(path: String): Boolean
    {
        set(path.lowercase(), System.currentTimeMillis())
        return saveConfig()
    }

    /**
     * get a number
     *
     * @param path config path
     * @return number
     */
    fun getNumber(path: String): Int
    {
        return getInt(path.lowercase())
    }

    /**
     * save a number
     *
     * @param path   config path
     * @param number number to save
     * @return successful or not
     */
    fun setNumber(path: String, number: Int): Boolean
    {
        set(path.lowercase(), number)
        return saveConfig()
    }

    @JvmOverloads
    fun addNumber(path: String, add: Int = 1): Boolean
    {
        set(path.lowercase(), getInt(path.lowercase()) + add)
        return saveConfig()
    }

    /**
     * get a location
     *
     * @param path config path
     * @return location or null
     */
    override fun getLocation(path: String): Location?
    {
        return getObject("locations." + path.lowercase(), Location::class.java, null)
    }

    /**
     * save a location
     *
     * @param path config path
     * @param loc  location to save
     * @return successful or not
     */
    fun setLocation(path: String, loc: Location): Boolean
    {
        set("locations." + path.lowercase(), loc)
        return saveConfig()
    }

    /**
     * delete a location
     *
     * @param path config path
     * @return successful or not
     */
    fun deleteLocation(path: String): Boolean
    {
        return delete("locations." + path.lowercase())
    }

    /**
     * get a list of locations (e.g. a player's homes)
     *
     * @param path config path
     * @return empty or filled map of name -> locations
     */
    fun getLocations(path: String): Map<String, Location>
    {
        val locations = HashMap<String, Location>()
        val section = getConfigurationSection("locations." + path.lowercase())
        if (section != null)
        {
            for (key in section.getKeys(false))
            {
                val loc = getLocation(path.lowercase() + "." + key)
                if (loc != null)
                {
                    locations[key] = loc
                }
            }
        }
        return locations
    }

    /**
     * get items
     *
     * @param path config path
     * @return empty or filled array of items
     */
    fun getItems(path: String): Array<ItemStack>
    {
        val list = getList("items." + path.lowercase())
        if (list != null && list.isNotEmpty())
        {
            if (list[0] is ItemStack)
            {
                val items = list as List<ItemStack>
                return items.toTypedArray()
            }
        }
        return arrayOf()
    }

    /**
     * save items
     *
     * @param path  config path
     * @param items items to save
     * @return successful or not
     */
    fun setItems(path: String, items: Array<ItemStack?>): Boolean
    {
        set("items." + path.lowercase(), items.filterNotNull())
        return saveConfig()
    }

    /**
     * save items with air instead of null
     *
     * @param path  config path
     * @param items items to save
     * @return successful or not
     */
    fun setItemsWithNull(path: String, items: Array<ItemStack?>): Boolean
    {
        return setItems(path, items.map { item: ItemStack? -> item ?: ItemStack(Material.AIR) }.toTypedArray())
    }

    /**
     * delete items
     *
     * @param path config path
     * @return successful or not
     */
    fun deleteItems(path: String): Boolean
    {
        return delete("items." + path.lowercase())
    }

    /**
     * get a message
     *
     * @param path         config path
     * @param placeholders placeholders to replace in the message
     * @return empty or filled string
     */
    @JvmOverloads
    fun getMessage(path: String, placeholders: Map<String, String> = HashMap()): String
    {
        var message = getString(path.lowercase())
        if (message != null)
        {
            message = ChatColor.translateAlternateColorCodes('&', message)
            for (placeholder in placeholders.keys)
            {
                message = message!!.replace(placeholder, placeholders[placeholder]!!)
            }
            return message!!
        }
        return ""
    }

    /**
     * get a list of messages
     *
     * @param path         config path
     * @param placeholders placeholders to replace in the messages
     * @param replaceFirst replace only the first occurrence of a placeholder
     * @return empty or filled list of messages
     */
    @JvmOverloads
    fun getMessages(
        path: String,
        placeholders: Map<String, String> = HashMap(),
        replaceFirst: Boolean = false
    ): List<String>
    {
        val messages = getStringList(path.lowercase())
        for (i in messages.indices)
        {
            var message = ChatColor.translateAlternateColorCodes('&', messages[i])
            for (placeholder in placeholders.keys)
            {
                message = if (replaceFirst)
                {
                    message.replaceFirst(placeholder.toRegex(), placeholders[placeholder]!!)
                } else
                {
                    message.replace(placeholder, placeholders[placeholder]!!)
                }
            }
            messages[i] = message
        }
        return messages
    }

    init
    {
        try
        {
            if (!folder.exists() && !folder.mkdirs())
            {
                throw Exception("Could not generate folder")
            }
            file = File(folder, name.lowercase())
            if (!file!!.exists())
            {
                try
                {
                    plugin.saveResource(name.lowercase(), false)
                } catch (e: Exception)
                {
                    if (!file!!.createNewFile())
                    {
                        throw Exception("Could not generate file '" + name.lowercase() + "'")
                    }
                }
            }
            load(file!!)
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}