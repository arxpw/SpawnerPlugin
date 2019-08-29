package pw.arx.spawnerplugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class BreakListener implements Listener {
	
	FileConfiguration config = SpawnerPlugin.getPlugin().getConfig();
	
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
    	Block broken = event.getBlock();
    	Player player = event.getPlayer();
    	
    	if(broken.getType() == Material.SPAWNER) {
    		if(player.getItemInHand() != null) {
    			if(player.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
    				
    				if(!player.hasPermission("spawner.break")) {
    					return;
    				}
    				
    		    	Location loc = broken.getLocation().add(0,0.5,0);
    		    	World world = event.getBlock().getWorld();
    		    	Boolean already_placed = false;
    		    	String LOCSTRING = broken.getLocation().getBlockX() + "," + broken.getLocation().getBlockY() + "," + broken.getLocation().getBlockZ();
    		    	
    		    	FileConfiguration c = SpawnerPlugin.getPlugin().getSpawnersFound().getConfig();
    		    	
    		    	if(c.getStringList(world.getName()).contains(LOCSTRING)) {
    		    		already_placed = true;
    		    		event.setExpToDrop(0);
    		    		List<String> LIST_REMOVED = c.getStringList(world.getName());
    		    		LIST_REMOVED.remove(LOCSTRING);
    		    		c.set(world.getName(), LIST_REMOVED);
    		    		SpawnerPlugin.getPlugin().getSpawnersFound().saveConfig();
    		    	}

    		    	CreatureSpawner spawner = ((CreatureSpawner) broken.getState());
    		    	
    		    	ItemStack is = new ItemStack(Material.SPAWNER, 1);    		    	
    		    	ItemMeta im = is.getItemMeta();
    		    	im.setDisplayName(ChatColor.AQUA + "Mob Spawner");
    		    	
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(spawner.getCreatureTypeName());
                    
                    im.setLore(lore);
    		    	is.setItemMeta(im);
    		    	
    	    		world.dropItem(loc, is);
    	    		if(config.getBoolean("announce_spawner_find") == true) {
    	    			// natrual spawner? yes boy!
    	    			if(already_placed == false) {
    	    				player.sendMessage(Lang.SPAWNER_BREAK_TO_SERVER.getConfigValue(new String[] { player.getDisplayName(), spawner.getCreatureTypeName() }));
    	    			}
    	    		}
    			}
    		}

    	}
    }
    
}