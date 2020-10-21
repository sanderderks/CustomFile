package me.sd_master92.customfile;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class PlayerFile extends CustomFile
{
    private final String uuid;

    /**
     * Create a new PlayerFile instance
     *
     * @param uuid   uuid of this player
     * @param plugin main plugin class
     */
    public PlayerFile(String uuid, Plugin plugin)
    {
        super(new File(plugin.getDataFolder() + File.separator + "players"), uuid + ".yml", plugin);
        this.uuid = uuid.toLowerCase();
    }

    public String getUuid()
    {
        return uuid;
    }

    public String getName()
    {
        return getConfig().getString("name");
    }

    public boolean setName(String name)
    {
        getConfig().set("name", name);
        return saveConfig();
    }

    public Location getHome(String name)
    {
        return getLocation(name);
    }

    public Location getHome()
    {
        return getHome("home");
    }

    public boolean setHome(String name, Location loc)
    {
        return setLocation(name, loc);
    }

    public boolean setHome(Location loc)
    {
        return setHome("home", loc);
    }
}
