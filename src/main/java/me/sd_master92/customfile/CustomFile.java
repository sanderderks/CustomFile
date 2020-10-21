package me.sd_master92.customfile;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomFile
{
    private final FileConfiguration config = new YamlConfiguration();
    private File loc = null;

    /**
     * Create a new CustomFile instance
     *
     * @param name   - name of the file
     * @param plugin - main plugin class
     */
    public CustomFile(String name, Plugin plugin)
    {
        File folder = plugin.getDataFolder();
        try
        {
            if (!folder.exists() && !folder.mkdirs())
            {
                throw new Exception("Could not generate folder");
            }

            loc = new File(folder, name);

            if (!loc.exists() && !loc.createNewFile())
            {
                plugin.saveResource(name, false);
            }
            getConfig().load(loc);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get the configuration from this CustomFile
     */
    public FileConfiguration getConfig()
    {
        return config;
    }

    /**
     * Save changes made to this CustomFile configuration
     */
    public boolean saveConfig()
    {
        try
        {
            getConfig().save(loc);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Reload the configuration from this CustomFile
     *
     * @return successful or not
     */
    public boolean reloadConfig()
    {
        try
        {
            getConfig().load(loc);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Delete a path from this CustomFile (does not require #saveConfig())
     *
     * @param path - path to delete
     * @return successful or not
     */
    public boolean delete(String path)
    {
        if (getConfig().contains(path))
        {
            getConfig().set(path.toLowerCase(), null);
            return saveConfig();
        }
        return false;
    }

    /**
     * Retrieve a Location from this CustomFile
     *
     * @param path - path to the Location
     * @return Location or null
     */
    public Location getLocation(String path)
    {
        ConfigurationSection section = getConfig().getConfigurationSection(path);
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

    /**
     * Save a Location to this CustomFile (does not require #saveConfig())
     *
     * @param path - path to this Location
     * @param loc  - Location to save
     * @return successful or not
     */
    public boolean setLocation(String path, Location loc)
    {
        getConfig().set(path + ".x", loc.getX());
        getConfig().set(path + ".y", loc.getY());
        getConfig().set(path + ".z", loc.getZ());
        getConfig().set(path + ".pit", loc.getPitch());
        getConfig().set(path + ".yaw", loc.getYaw());
        World w = loc.getWorld();
        if (w != null)
        {
            getConfig().set(path + ".world", loc.getWorld().getName());
        } else
        {
            getConfig().set(path + ".world", "world");
        }
        return saveConfig();
    }

    /**
     * Retrieve an ItemStack array from this CustomFile
     *
     * @param path - path to the ItemStack array
     * @return ItemStack[] or null
     */
    public ItemStack[] getItems(String path)
    {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        ConfigurationSection section = getConfig().getConfigurationSection(path.toLowerCase());
        if (section != null)
        {
            return new Gson().fromJson(section.getString("items"), ItemStack[].class);
        }
        return null;
    }

    /**
     * Save an ItemStack array (like inventory/chest content) to this CustomFile
     * (does not require #saveConfig())
     *
     * @param path  - path to this ItemStack array
     * @param items - ItemStack array to save
     * @return successful or not
     */
    public boolean setItems(String path, ItemStack[] items)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        for (ItemStack item : items)
        {
            if (item != null && item.getType() != Material.AIR)
            {
                list.add(item);
            }
        }
        getConfig().set(path.toLowerCase() + ".items", new Gson().toJson(list.toArray(new ItemStack[0])));
        return saveConfig();
    }
}
