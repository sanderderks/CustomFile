package me.sd_master92.customfile;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CustomFile
{
    private FileConfiguration config = new YamlConfiguration();
    private File loc = null;

    /**
     * Create a new CustomFile instance
     *
     * @param folder - folder you want to save your file in
     * @param name   - name of the file
     * @param plugin - main plugin class
     */
    public CustomFile(File folder, String name, Plugin plugin)
    {
        try
        {
            if (!folder.exists()) // if the specified folder does not yet exist, create it
            {
                folder.mkdirs();
            }

            loc = new File(folder, name);

            if (!loc.exists()) // if the specified file does not yet exist, check if the plugin provides a
            // default configuration for this file (and load it to 'config') and else create
            // a new file
            {
                if (plugin.getResource(name) == null)
                {
                    loc.createNewFile();
                } else
                {
                    plugin.saveResource(name, false);
                    config.load(loc);
                }
            } else // if the specified file does exist, load its configuration to 'config'
            {
                config.load(loc);
            }
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
    public void saveConfig()
    {
        try
        {
            this.getConfig().save(loc);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Reload the configuration from this CustomFile
     */
    public void reloadConfig()
    {
        try
        {
            this.getConfig().load(loc);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Retreive a location from this CustomFile
     *
     * @param path - path to the location
     * @return Location
     */
    public Location getLocation(String path)
    {
        ConfigurationSection section = this.getConfig().getConfigurationSection(path);
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float pit = (float) section.getDouble("pit");
        float yaw = (float) section.getDouble("yaw");
        World world = Bukkit.getWorld(section.getString("world"));

        return new Location(world, x, y, z, yaw, pit);
    }

    /**
     * Save a location to this CustomFile (does not require #saveConfig())
     *
     * @param path - path to this location
     * @param loc  - location to save
     */
    public void setLocation(String path, Location loc)
    {
        this.getConfig().set(path + ".x", loc.getX());
        this.getConfig().set(path + ".y", loc.getY());
        this.getConfig().set(path + ".z", loc.getZ());
        this.getConfig().set(path + ".pit", loc.getPitch());
        this.getConfig().set(path + ".yaw", loc.getYaw());
        this.getConfig().set(path + ".world", loc.getWorld().getName());
        this.saveConfig();
    }

    /**
     * Retreive an itemstack array from this CustomFile
     *
     * @param path - path to the itemstack array
     * @return ItemStack[]
     */
    public ItemStack[] getItems(String path)
    {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        for (Map<?, ?> i : (path != null ? this.getConfig().getConfigurationSection(path.toLowerCase()).getMapList("items") : this.getConfig().getMapList("items")))
        {
            ItemStack item = ItemStack.deserialize((Map<String, Object>) i);
            list.add(item);
        }
        return list.toArray(new ItemStack[list.size()]);
    }

    /**
     * Save an itemstack array (like inventory/chest content) to this CustomFile
     * (does not require #saveConfig())
     *
     * @param path  - path to this itemstack array
     * @param items - itemstack array to save
     */
    public void setItems(String path, ItemStack[] items)
    {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ItemStack item : items)
        {
            if (item != null && item.getType() != Material.AIR)
            {
                list.add(item.serialize());
            }
        }
        this.getConfig().set(path.toLowerCase() + ".items", list);
        this.saveConfig();
    }
}
