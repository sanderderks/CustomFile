package me.sd_master92.customfile;

import org.bukkit.entity.Player;
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

    /**
     * Create a new PlayerFile instance
     * (This will automatically save all possible player properties)
     *
     * @param player the player
     * @param plugin main plugin class
     */
    public PlayerFile(Player player, Plugin plugin)
    {
        this(player.getUniqueId().toString(), plugin);
        setName(player.getName());
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
}
