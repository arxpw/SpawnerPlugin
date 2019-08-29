package pw.arx.spawnerplugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pw.arx.spawnerplugin.ConfigWrapper;

public class SpawnerPlugin extends JavaPlugin {

	private static SpawnerPlugin plugin;
	
	private String CFG_YML = "config.yml";
	private String LANG_YML = "eng.yml";
	private String SPAWNERS_YML = "spawners-placed.yml";
	
	private ConfigWrapper configFile = new ConfigWrapper(this, "", CFG_YML);
    private ConfigWrapper messagesFile = new ConfigWrapper(this, "", LANG_YML);
    private ConfigWrapper spawnersFile = new ConfigWrapper(this, "", SPAWNERS_YML);
    
    public static SpawnerPlugin getPlugin() {
		return plugin;
	}
	
    @Override
    public void onEnable() {
		plugin = getPlugin(SpawnerPlugin.class);
		PluginManager pm = getServer().getPluginManager();

		// Configs
        configFile.createNewFile("Loading " + CFG_YML,"Messages file");
        configFile.getConfig().addDefault("announce_spawner_place", true);
        configFile.getConfig().addDefault("announce_spawner_find", true);
        configFile.getConfig().options().copyDefaults(true);
        configFile.reloadConfig(CFG_YML);
		
        messagesFile.createNewFile("Loading " + LANG_YML,"Messages file");
        loadMessages();
        
        spawnersFile.createNewFile("Loading " + SPAWNERS_YML, "Spawners placed file");
        spawnersFile.loadConfig(LANG_YML);
        
        // Listeners
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
    
    @Override
    public void onDisable() {

    }
}
