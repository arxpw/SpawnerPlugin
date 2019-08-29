package pw.arx.spawnerplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class TapListener implements Listener {
	
	FileConfiguration config = SpawnerPlugin.getPlugin().getConfig();
	
    @EventHandler
    public void onBlockTap(PlayerInteractEvent event)
    {
    	
    	EquipmentSlot e = event.getHand();
    	if(e.equals(EquipmentSlot.HAND) != true)
    		return;
    	
    	if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
    		return;
    	
    	if(event.getClickedBlock().getType() != Material.SPAWNER)
    		return;
    
    	if(event.getPlayer().hasPermission("spawner.inspect")) {
    		Location BL = event.getClickedBlock().getLocation();
    		String BLSTRING = BL.getBlockX() + "," + BL.getBlockY() + "," + BL.getBlockZ();
    		
    		FileConfiguration C = SpawnerPlugin.getPlugin().getSpawnersFound().getConfig();
    		ConfigurationSection WorldCFG = C.getConfigurationSection(BL.getWorld().getName());
    		Integer C_AMOUNT = WorldCFG.getKeys(false).size();
    		
    		if(C_AMOUNT > 0) {
    			for(Integer i = 1; i < (C_AMOUNT+1); i++) {
    				String CONFIGCOORDS = (String) WorldCFG.get(i.toString());
    				if(CONFIGCOORDS.equals(BLSTRING)) {
    		    		event.getPlayer().sendMessage("This spawner has been placed by a player.");
    		    		event.getPlayer().playSound(event.getClickedBlock().getLocation(), Sound.BLOCK_ANVIL_LAND, 2, 2);
    		    		break;
    				}
    			}
    		}
    	}
    }
    
}