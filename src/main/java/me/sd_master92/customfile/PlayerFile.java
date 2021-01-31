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
     * create a new PlayerFile instance
     *
     * @param uuid   uuid of this player
     * @param plugin main plugin class
     */
    public PlayerFile(String uuid, Plugin plugin)
    {
        super(new File(plugin.getDataFolder() + File.separator + "players"), uuid + ".yml", plugin);
        this.uuid = uuid.toLowerCase();
        setFirstJoinTime();
    }

    /**
     * create a new PlayerFile instance
     * (this will automatically save all possible player properties)
     *
     * @param player the player
     * @param plugin main plugin class
     */
    public PlayerFile(Player player, Plugin plugin)
    {
        this(player.getUniqueId().toString(), plugin);
        setName(player.getName());
    }

    /**
     * get a PlayerFile by player name
     *
     * @param name   player name
     * @param plugin main plugin class
     * @return PlayerFile or null
     */
    public static PlayerFile getByName(String name, Plugin plugin)
    {
        for (PlayerFile playerFile : getAll(plugin))
        {
            if (playerFile.getName().equalsIgnoreCase(name))
            {
                return playerFile;
            }
        }
        return null;
    }

    /**
     * get all PlayerFiles
     *
     * @param plugin main plugin class
     * @return empty or filled list of PlayerFiles
     */
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

    /**
     * save first join timestamp
     */
    private void setFirstJoinTime()
    {
        if (getTimeStamp("first_join") == 0)
        {
            setTimeStamp("first_join");
        }
    }

    /**
     * get first join timestamp
     */
    private long getFirstJoinTime()
    {
        return getTimeStamp("first_join");
    }

    /**
     * get the unique id of this PlayerFile
     *
     * @return uuid or null
     */
    public String getUuid()
    {
        return uuid;
    }

    /**
     * get the name of the player
     *
     * @return name or "unknown"
     */
    @Override
    public String getName()
    {
        String name = getString("name");
        if (name != null)
        {
            return name;
        }
        return "unknown";
    }

    /**
     * save the name of the player
     *
     * @param name player name
     * @return successful or not
     */
    public boolean setName(String name)
    {
        set("name", name);
        return saveConfig();
    }
}
