package me.sd_master92.customfile;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomFile
{
    private final FileConfiguration config = new YamlConfiguration();
    private File file;

    /**
     * Create a new CustomFile instance
     *
     * @param folder folder of the file
     * @param name   name of the file
     * @param plugin main plugin class
     */
    public CustomFile(File folder, String name, Plugin plugin)
    {
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
            getConfig().load(file);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Create a new CustomFile instance
     *
     * @param name   name of the file
     * @param plugin main plugin class
     */
    public CustomFile(String name, Plugin plugin)
    {
        this(plugin.getDataFolder(), name, plugin);
    }

    public FileConfiguration getConfig()
    {
        return config;
    }

    public boolean saveConfig()
    {
        try
        {
            getConfig().save(file);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public boolean reloadConfig()
    {
        try
        {
            getConfig().load(file);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public boolean delete()
    {
        return file.delete();
    }

    public boolean delete(String path)
    {
        if (getConfig().contains(path))
        {
            getConfig().set(path.toLowerCase(), null);
            return saveConfig();
        }
        return false;
    }

    public long getTimeStamp(String path)
    {
        return getConfig().getLong(path.toLowerCase());
    }

    public boolean setTimeStamp(String path)
    {
        getConfig().set(path.toLowerCase(), System.currentTimeMillis());
        return saveConfig();
    }

    public int getNumber(String path)
    {
        return getConfig().getInt(path.toLowerCase());
    }

    public boolean setNumber(String path, int number)
    {
        getConfig().set(path.toLowerCase(), number);
        return saveConfig();
    }

    public boolean addNumber(String path, int add)
    {
        if (add != 0)
        {
            Object found = getConfig().get(path.toLowerCase());
            if (found instanceof Integer)
            {
                int number = (int) found;

                number += add;

                getConfig().set(path.toLowerCase(), number);
                return saveConfig();
            }
        }
        return false;
    }

    public Map<String, Location> getLocations(String path)
    {
        HashMap<String, Location> locations = new HashMap<>();
        ConfigurationSection section = getConfig().getConfigurationSection("locations." + path.toLowerCase());
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

    public Location getLocation(String path)
    {
        ConfigurationSection section = getConfig().getConfigurationSection("locations." + path.toLowerCase());
        if (section != null)
        {
            double x = section.getDouble("x");
            double y = section.getDouble("y");
            double z = section.getDouble("z");
            float pit = (float) section.getDouble("pit");
            float yaw = (float) section.getDouble("yaw");
            String w = section.getString("world");
            if (w != null)
            {
                World world = Bukkit.getWorld(w);
                return new Location(world, x, y, z, yaw, pit);
            }
        }
        return null;
    }

    public boolean setLocation(String path, Location loc)
    {
        String section = "locations." + path.toLowerCase();
        getConfig().set(section + ".x", loc.getX());
        getConfig().set(section + ".y", loc.getY());
        getConfig().set(section + ".z", loc.getZ());
        getConfig().set(section + ".pit", loc.getPitch());
        getConfig().set(section + ".yaw", loc.getYaw());
        World w = loc.getWorld();
        if (w != null)
        {
            getConfig().set(section + ".world", loc.getWorld().getName());
        } else
        {
            getConfig().set(section + ".world", "world");
        }
        return saveConfig();
    }

    public ItemStack[] getItems(String path)
    {
        ConfigurationSection section = getConfig().getConfigurationSection("items");
        ArrayList<ItemStack> items = new ArrayList<>();
        if (section != null)
        {
            Object object = section.get(path.toLowerCase());
            if (object instanceof ArrayList)
            {
                for (Object item : (ArrayList<?>) object)
                {
                    if (item instanceof ItemStack)
                    {
                        items.add((ItemStack) item);
                    }
                }
            }
        }
        return items.toArray(new ItemStack[0]);
    }

    public boolean setItems(String path, ItemStack[] items)
    {
        ArrayList<ItemStack> list = new ArrayList<>();
        for (ItemStack item : items)
        {
            if (item != null && item.getType() != Material.AIR)
            {
                list.add(item);
            }
        }
        getConfig().set("items." + path.toLowerCase(), list);
        return saveConfig();
    }

    public String getMessage(String path, Map<String, String> placeholders)
    {
        String message = getConfig().getString(path.toLowerCase());
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

    public List<String> getMessages(String path, Map<String, String> placeholders, boolean replaceFirst)
    {
        List<String> messages = getConfig().getStringList(path.toLowerCase());
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

    public List<String> getMessages(String path, Map<String, String> placeholders)
    {
        return getMessages(path.toLowerCase(), placeholders, false);
    }
}
