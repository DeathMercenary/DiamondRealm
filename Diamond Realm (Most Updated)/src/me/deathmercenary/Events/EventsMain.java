package me.deathmercenary.Events;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.deathmercenary.Main.Main;

public class EventsMain implements Listener {

	private Main main = Main.getPlugin(Main.class);
	
	
	public ItemStack SpookyItem(Integer amount) {
		return new ItemStack(Material.ACACIA_BOAT);
	}
}
