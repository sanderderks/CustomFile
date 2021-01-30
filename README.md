# CustomFile
Custom configuration files for Spigot

A CustomFile is an extension to YamlConfiguration and makes creating configuration files and working with stored data very easy!


HOW IT WORKS

- Create an instance of CustomFile:

CustomFile config = new CustomFile("data.yml", plugin);

// Calling the constructor will load the existing file or the file provided in your .jar into this object.
// When these options are not available, a new file will be created and you can start editing it with the available methods.

- Store a player's inventory to this file:

config.setItems("some_path", player.getInventory.getContents());

// The extension methods provided by CustomFile do not require a call to #saveConfig.
// Similar functions are available for e.g. Locations, Messages (strings with colorcodes AND placeholders) and more!
// Methods that might fail, will not NEVER throw an EXCEPTION. Instead a Boolean value is returned, so you can decide how to handle failures.

- Retrieve a player's inventory:

config.getItems("some_path");

// Methods like these will NEVER return NULL. An empty list is returned instead when the items do not exist. This means less checks in your code!


If this wasn't enough, this repository also includes PlayerFile, which is an extension of CustomFile, to make per-player-files a lot easier!

- Create an instance of PlayerFile:

PlayerFile playerFile = new PlayerFile(player.getUniqueId(), plugin);

// A file will be created by the constructor of CustomFile, but within the folder "Players", and with the UUID as filename.
// Every time an instance is created, the player's name will be stored into this file, if it was changed. This comes in handy when a player is offline, but you need its name.
// The first time an instance of PlayerFile is created, a timestamp called "first_join" will be saved inside of it, in case you ever want to use it.
