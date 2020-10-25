package me.sd_master92.customfile;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        if (getName() == null)
        {
            setName("unknown");
        }
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

    public static List<PlayerFile> getAll(Plugin plugin)
    {
        List<PlayerFile> playerFiles = new ArrayList<>();
        File folder = new File(plugin.getDataFolder() + File.separator + "players");
        if (folder.exists())
        {
            File[] files = folder.listFiles();
            if (files != null)
            {
                for (File file : files)
                {
                    String uuid = file.getName().replace(".yml", "");
                    if (!uuid.isEmpty())
                    {
                        playerFiles.add(new PlayerFile(uuid, plugin));
                    }
                }
            }
        }
        return playerFiles;
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
