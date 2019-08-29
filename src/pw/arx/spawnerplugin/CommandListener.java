package pw.arx.spawnerplugin;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class CommandListener implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(Lang.NO_CONSOLE.getConfigValue(new String[] {}));
			return false;
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			player.sendMessage(Lang.COMMANDS_TITLE.getConfigValue(new String[] {}));
			player.sendMessage(Lang.COMMANDS_COMMANDS.getConfigValue(new String[] {}));
			return false;
		}
		
		if (args[0].equalsIgnoreCase("reload")) {
			if(!player.hasPermission("spawner.reload")) {
				player.sendMessage(Lang.NO_PERMISSION.getConfigValue(new String[] {}));
				return false;
			}
			
			SpawnerPlugin.getPlugin().getConfigFound().reloadConfig();
			SpawnerPlugin.getPlugin().getLangFound().reloadConfig();
			SpawnerPlugin.getPlugin().getSpawnersFound().reloadConfig();
			
			player.sendMessage("Reload True");
			return true;
			
		}
		
		if (args[0].equalsIgnoreCase("alter")) {
			
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
			player.sendMessage(Lang.INVALID_MOB_NAME.getConfigValue(new String[] {}));
			return false;
		}
	}
}
