package pw.arx.spawnerplugin;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import net.md_5.bungee.api.ChatColor;

public class PlaceListener implements Listener {

	FileConfiguration config = SpawnerPlugin.getPlugin().getConfig();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Block b = event.getBlock();
		Material bt = b.getType();
		
		if(bt == Material.SPAWNER) {
			
			if(!event.getPlayer().hasPermission("spawner.place")) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(Lang.NO_PERMISSION.getConfigValue(new String[] {}));
				return;
			}
			
			ItemStack is = event.getItemInHand();
			Player p = event.getPlayer();
			
			String dName = is.getItemMeta().getDisplayName();
			if(dName.equals(ChatColor.AQUA + "Mob Spawner")) {
				System.out.println("PLACING ");
				String mobType = ChatColor.stripColor(is.getItemMeta().getLore().get(0));

	            CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlockPlaced().getState();
	            try {
	                creatureSpawner.setCreatureTypeByName(event.getItemInHand().getItemMeta().getLore().get(0));
	            } catch (Exception e) {}
	            
	            creatureSpawner.update();
	            event.getBlockPlaced().setMetadata("ALREADY_FOUND", new FixedMetadataValue(SpawnerPlugin.getPlugin(), true));
	            
	            
	            String WORLD_NAME = event.getBlockPlaced().getWorld().getName();
	            Location BL = event.getBlockPlaced().getLocation();
	            String LOCSTRING = BL.getBlockX() + "," + BL.getBlockY() + "," + BL.getBlockZ();
	            FileConfiguration c = SpawnerPlugin.getPlugin().getSpawnersFound().getConfig();
	            
	            Boolean WORLD_EXISTS = c.isList(WORLD_NAME);
	            
	            if(WORLD_EXISTS == false) {
	            	List<String> FIRST_LIST_ITEM = Arrays.asList(LOCSTRING);
	            	c.set(WORLD_NAME, FIRST_LIST_ITEM);
	            	SpawnerPlugin.getPlugin().getSpawnersFound().saveConfig();
	            } else {
	            	List<String> LIST_ITEMS = c.getStringList(WORLD_NAME);
	            	LIST_ITEMS.add(LOCSTRING);
	            	c.set(WORLD_NAME, LIST_ITEMS);
	            	SpawnerPlugin.getPlugin().getSpawnersFound().saveConfig();
	            }
	            
	            
	            if(config.getBoolean("announce_spawner_place") == true) {
	            	p.getServer().broadcastMessage(Lang.SPAWNER_PLACE_TO_SERVER.getConfigValue(new String[] { p.getDisplayName(), mobType }));
	            } else {
	            	p.sendMessage(Lang.SPAWNER_PLACE_TO_PLAYER.getConfigValue(new String[] { mobType }));	
	            }
				
			}
			
			
		}
		
	}
	
}
