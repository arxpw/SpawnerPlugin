package pw.arx.spawnerplugin;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandListener implements CommandExecutor {

	private String CFG_YML = "config.yml";
	private String LANG_YML = "eng.yml";
	private String SPAWNERS_YML = "spawners-placed.yml";
	
	public static String c(String i) {
		return ChatColor.translateAlternateColorCodes('&',i);
	}
	
	public static String changed(String before, String after) {
		if(before.equals(after)) {
			return "";
		}
		return c(" &4&l<-&r&4 Changed ");
	}
	
	public static String configBit(String find, FileConfiguration before, FileConfiguration after) {
		return after.getString(find) + changed(before.getString(find), after.getString(find));
	}
	
	public static Boolean senderIsPlayer(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Lang.NO_CONSOLE.getConfigValue(new String[] {}));
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {

		
		

		if (args.length == 0) {
			sender.sendMessage(Lang.COMMANDS_TITLE.getConfigValue(new String[] {}));
			sender.sendMessage(Lang.COMMANDS_COMMANDS.getConfigValue(new String[] {}));
			return false;
		}
		
		if (args[0].equalsIgnoreCase("reload")) {
			if(!sender.hasPermission("spawner.reload")) {
				sender.sendMessage(Lang.NO_PERMISSION.getConfigValue(new String[] {}));
				return false;
			}
			
			FileConfiguration ConfigBefore = SpawnerPlugin.getPlugin().getConfigFound().getConfig();
			SpawnerPlugin.getPlugin().getConfigFound().reloadConfig("config.yml");
			FileConfiguration ConfigAfter = SpawnerPlugin.getPlugin().getConfigFound().getConfig();
			
			sender.sendMessage(c("&f---- " + CFG_YML));
			for(String key : ConfigAfter.getKeys(false)){
				sender.sendMessage(c("&e" + key + ": &f") + configBit(key, ConfigBefore, ConfigAfter));
			}
			
			FileConfiguration LangConfigBefore = SpawnerPlugin.getPlugin().getLangFound().getConfig();
			SpawnerPlugin.getPlugin().getLangFound().reloadConfig("eng.yml");
			FileConfiguration LangConfigAfter = SpawnerPlugin.getPlugin().getLangFound().getConfig();
			
			sender.sendMessage(c("&f---- " + LANG_YML));
			for(String key : LangConfigAfter.getKeys(false)){
				sender.sendMessage(c("&e" + key + ": &f") + configBit(key, LangConfigBefore, LangConfigAfter));
			}
			
			SpawnerPlugin.getPlugin().getSpawnersFound().reloadConfig("spawners-placed.yml");
			sender.sendMessage(c("&f---- " + SPAWNERS_YML));

			return true;
			
		}
		
		if (args[0].equalsIgnoreCase("alter")) {
			
			if(senderIsPlayer(sender) == false)
				return false;
			
			// otherwise.. we know it's a player!
			Player player = (Player) sender;
			
			if(!player.hasPermission("spawner.alter")) {
				player.sendMessage(Lang.NO_PERMISSION.getConfigValue(new String[] {}));
				return false;
			}
			
			if(args.length == 1) {
				player.sendMessage(Lang.INVALID_MOB_NAME.getConfigValue(new String[] {}));
				return false;
			}
				
			EntityType ENTITY_NAME_FOUND = EntityType.fromName(args[1]);
			if(ENTITY_NAME_FOUND == null) {
				player.sendMessage(Lang.INVALID_MOB_NAME.getConfigValue(new String[] {}));
				return false;
			}
			
			Material IN_HAND_MAT = player.getInventory().getItemInMainHand().getType();
			if(IN_HAND_MAT != null && IN_HAND_MAT.equals(Material.SPAWNER)) {
				ItemStack spawner = player.getItemInHand();
				ItemMeta spawnerMeta = spawner.getItemMeta();
				
				if(!spawnerMeta.hasLore()) {
					player.sendMessage(Lang.INVALID_SPAWNER_TYPE.getConfigValue(new String[] {}));
					return false;
				}
				
	            ArrayList<String> lore = new ArrayList<String>();
	            lore.add(ENTITY_NAME_FOUND.name().toLowerCase());
	            
	            spawnerMeta.setLore(lore);
		    	spawner.setItemMeta(spawnerMeta);
		    	
		    	String SPAWNER_TYPE = ENTITY_NAME_FOUND.name().toLowerCase(); 
		    	player.sendMessage(Lang.SPAWNER_ALTER_SUCCESS.getConfigValue(new String[] { SPAWNER_TYPE }));
			} else {
				player.sendMessage(Lang.SPAWNER_ALTER_FAIL.getConfigValue(new String[] {}));
			}
			return true;
		} else {
			sender.sendMessage(Lang.INVALID_MOB_NAME.getConfigValue(new String[] {}));
			return false;
		}
	}
}
