package pw.arx.spawnerplugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pw.arx.spawnerplugin.ConfigWrapper;

public class SpawnerPlugin extends JavaPlugin {

	private static SpawnerPlugin plugin;
	
	
	private ConfigWrapper configFile = new ConfigWrapper(this, "", "config.yml");
    private ConfigWrapper messagesFile = new ConfigWrapper(this, "", "eng.yml");
    private ConfigWrapper spawnersFile = new ConfigWrapper(this, "", "spawners-placed.yml");
    
    public static SpawnerPlugin getPlugin() {
		return plugin;
	}
	
	
    @Override
    public void onEnable() {
		plugin = getPlugin(SpawnerPlugin.class);
		PluginManager pm = getServer().getPluginManager();

		// config
        configFile.createNewFile("Loading config.yml","Messages file");
        configFile.getConfig().addDefault("announce_spawner_place", true);
        configFile.getConfig().addDefault("announce_spawner_find", true);
        configFile.getConfig().options().copyDefaults(true);
        configFile.reloadConfig();
		
		// langs
        messagesFile.createNewFile("Loading eng.yml","Messages file");
        loadMessages();
        
        spawnersFile.createNewFile("Loading spawners-placed.yml", "Spawners placed file");
        loadSpawnersFound();
        
		// commands
		this.getCommand("spawners").setExecutor(new CommandListener());
    	
		pm.registerEvents(new TapListener(), this);
		pm.registerEvents(new PlaceListener(), this);
		pm.registerEvents(new BreakListener(), this);

    }
    
    public void loadMessages() {
        Lang.setFile(messagesFile.getConfig());
        for (final Lang value : Lang.values()) {
            messagesFile.getConfig().addDefault(value.getPath(), value.getDefault());
        }

        messagesFile.getConfig().options().copyDefaults(true);
        messagesFile.saveConfig();
    }
    
    public ConfigWrapper getConfigFound() {
    	return configFile;
    }
    
    public ConfigWrapper getLangFound() {
    	return messagesFile;
    }
    
    public ConfigWrapper getSpawnersFound() {
    	return spawnersFile;
    }
    
    public void loadSpawnersFound() {
    	spawnersFile.reloadConfig();
        spawnersFile.saveConfig();
    }
    

    
    // when I run 
    // Lang.NO_PERMISSION_MESSAGE.getConfigValue(new String[] { permNeeded }), 
    // that allows me to get the string from the lang.yml and add placeholders in it depending 
    // on the String list that I pass through getConfigValue(list) allowing you to use {0} to use 
    // the first variable passed through the list and so on.. 
    // If I were to add 
    // Lang.NO_PERMISSION_MESSAGE.getConfigValue(null), 
    // no {0} {1} variables will be able to be used in the lang.yml for that String
    public void sendNoPermsMsg(Player p, String permNeeded) {
    	p.sendMessage(Lang.NO_PERMISSION.getConfigValue(new String[] { permNeeded }));
    }
    
    @Override
    public void onDisable() {

    }
}
