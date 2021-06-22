package me.sd_master92.customfile;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomFile extends YamlConfiguration
{
    private File file;

    /**
     * create a new CustomFile
     *
     * @param folder folder of this CustomFile
     * @param name   name of this CustomFile
     * @param plugin main plugin class
     */
    public CustomFile(File folder, String name, Plugin plugin)
    {
        super();
        try
        {
            if (!folder.exists() && !folder.mkdirs())
            {
                throw new Exception("Could not generate folder");
            }

            file = new File(folder, name.toLowerCase());

            if (!file.exists())
            {
                try
                {
                    plugin.saveResource(name.toLowerCase(), false);
                } catch (Exception e)
                {
                    if (!file.createNewFile())
                    {
                        throw new Exception("Could not generate file '" + name.toLowerCase() + "'");
                    }
                }
            }
            load(file);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * create a new CustomFile
     *
     * @param name   name of this CustomFile
     * @param plugin main plugin class
     */
    public CustomFile(String name, Plugin plugin)
    {
        this(plugin.getDataFolder(), name, plugin);
    }

    /**
     * save config
     *
     * @return successful or not
     */
    public boolean saveConfig()
    {
        try
        {
            save(file);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * reload config
     *
     * @return successful or not
     */
    public boolean reloadConfig()
    {
        try
        {
            load(file);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * delete this file
     *
     * @return successful or not
     */
    public boolean delete()
    {
        return file.delete();
    }

    /**
     * delete a path from config
     *
     * @param path config path
     * @return successful or not
     */
    public boolean delete(String path)
    {
        set(path.toLowerCase(), null);
        return saveConfig();
    }

    /**
     * get a timestamp
     *
     * @param path config path
     * @return number
     */
    public long getTimeStamp(String path)
    {
        return getLong(path.toLowerCase());
    }

    /**
     * save a timestamp
     *
     * @param path config path
     * @return successful or not
     */
    public boolean setTimeStamp(String path)
    {
        set(path.toLowerCase(), System.currentTimeMillis());
        return saveConfig();
    }

    /**
     * get a number
     *
     * @param path config path
     * @return number
     */
    public int getNumber(String path)
    {
        return getInt(path.toLowerCase());
    }

    /**
     * save a number
     *
     * @param path   config path
     * @param number number to save
     * @return successful or not
     */
    public boolean setNumber(String path, int number)
    {
        set(path.toLowerCase(), number);
        return saveConfig();
    }

    /**
     * add x to an existing number
     *
     * @param path config path
     * @param add  number to add
     * @return successful or not
     */
    public boolean addNumber(String path, int add)
    {
        set(path.toLowerCase(), getInt(path.toLowerCase()) + add);
        return saveConfig();
    }

    /**
     * add 1 to an existing number
     *
     * @param path config path
     * @return successful or not
     */
    public boolean addNumber(String path)
    {
        return addNumber(path, 1);
    }

    /**
     * get a location
     *
     * @param path config path
     * @return location or null
     */
    public Location getLocation(String path)
    {
        return getObject("locations." + path.toLowerCase(), Location.class, null);
    }

    /**
     * save a location
     *
     * @param path config path
     * @param loc  location to save
     * @return successful or not
     */
    public boolean setLocation(String path, Location loc)
    {
        set("locations." + path.toLowerCase(), loc);
        return saveConfig();
    }

    /**
     * delete a location
     *
     * @param path config path
     * @return successful or not
     */
    public boolean deleteLocation(String path)
    {
        return delete("locations." + path.toLowerCase());
    }

    /**
     * get a list of locations (e.g. a player's homes)
     *
     * @param path config path
     * @return empty or filled map of name -> locations
     */
    public Map<String, Location> getLocations(String path)
    {
        HashMap<String, Location> locations = new HashMap<>();
        ConfigurationSection section = getConfigurationSection("locations." + path.toLowerCase());
        if (section != null)
        {
            for (String key : section.getKeys(false))
            {
                Location loc = getLocation(path.toLowerCase() + "." + key);
                if (loc != null)
                {
                    locations.put(key, loc);
                }
            }
        }
        return locations;
    }

    /**
     * get items
     *
     * @param path config path
     * @return empty or filled array of items
     */
    public ItemStack[] getItems(String path)
    {
        return getObject("items." + path.toLowerCase(), ItemStack[].class, new ItemStack[]{});
    }

    /**
     * save items
     *
     * @param path  config path
     * @param items items to save
     * @return successful or not
     */
    public boolean setItems(String path, ItemStack[] items)
    {
        set("items." + path.toLowerCase(), items);
        return saveConfig();
    }

    /**
     * delete items
     *
     * @param path config path
     * @return successful or not
     */
    public boolean deleteItems(String path)
    {
        return delete("items." + path.toLowerCase());
    }

    /**
     * get a message
     *
     * @param path         config path
     * @param placeholders placeholders to replace in the message
     * @return empty or filled string
     */
    public String getMessage(String path, Map<String, String> placeholders)
    {
        String message = getString(path.toLowerCase());
        if (message != null)
        {
            message = ChatColor.translateAlternateColorCodes('&', message);
            if (placeholders != null)
            {
                for (String placeholder : placeholders.keySet())
                {
                    message = message.replace(placeholder, placeholders.get(placeholder));
                }
            }
            return message;
        }
        return "";
    }

    /**
     * get a message
     *
     * @param path config path
     * @return empty or filled string
     */
    public String getMessage(String path)
    {
        return getMessage(path, null);
    }

    /**
     * get a list of messages
     *
     * @param path         config path
     * @param placeholders placeholders to replace in the messages
     * @param replaceFirst replace only the first occurrence of a placeholder
     * @return empty or filled list of messages
     */
    public List<String> getMessages(String path, Map<String, String> placeholders, boolean replaceFirst)
    {
        List<String> messages = getStringList(path.toLowerCase());
        for (int i = 0; i < messages.size(); i++)
        {
            String message = ChatColor.translateAlternateColorCodes('&', messages.get(i));
            if (placeholders != null)
            {
                for (String placeholder : placeholders.keySet())
                {
                    if (replaceFirst)
                    {
                        message = message.replaceFirst(placeholder, placeholders.get(placeholder));
                    } else
                    {
                        message = message.replace(placeholder, placeholders.get(placeholder));
                    }
                }
            }
            messages.set(i, message);
        }
        return messages;
    }

    /**
     * get a list of messages
     *
     * @param path         config path
     * @param placeholders placeholders to replace in the messages
     * @return empty or filled list of messages
     */
    public List<String> getMessages(String path, Map<String, String> placeholders)
    {
        return getMessages(path.toLowerCase(), placeholders, false);
    }

    /**
     * get a list of messages
     *
     * @param path config path
     * @return empty or filled list of messages
     */
    public List<String> getMessages(String path)
    {
        return getMessages(path.toLowerCase(), null, false);
    }
}
