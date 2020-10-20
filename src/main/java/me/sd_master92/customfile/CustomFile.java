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
            config.load(loc);
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
     * Retrieve a location from this CustomFile
     *
     * @param path - path to the location
     * @return Location or null
     */
    public Location getLocation(String path)
    {
        ConfigurationSection section = this.getConfig().getConfigurationSection(path);
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
        World w = loc.getWorld();
        if (w != null)
        {
            this.getConfig().set(path + ".world", loc.getWorld().getName());
        } else
        {
            this.getConfig().set(path + ".world", "world");
        }
        this.saveConfig();
    }

    /**
     * Retrieve an itemstack array from this CustomFile
     *
     * @param path - path to the itemstack array
     * @return ItemStack[] or null
     */
    public ItemStack[] getItems(String path)
    {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        ConfigurationSection section = this.getConfig().getConfigurationSection(path.toLowerCase());
        if (section != null)
        {
            return new Gson().fromJson(section.getString("items"), ItemStack[].class);
        }
        return null;
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
        List<ItemStack> list = new ArrayList<ItemStack>();
        for (ItemStack item : items)
        {
            if (item != null && item.getType() != Material.AIR)
            {
                list.add(item);
            }
        }
        this.getConfig().set(path.toLowerCase() + ".items", new Gson().toJson(list.toArray(new ItemStack[0])));
        this.saveConfig();
    }
}
