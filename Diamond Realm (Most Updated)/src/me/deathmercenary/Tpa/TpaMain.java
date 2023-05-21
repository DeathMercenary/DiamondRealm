package me.deathmercenary.Tpa;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.deathmercenary.Main.Main;

public class TpaMain implements Listener {

	private Main plugin = Main.getPlugin(Main.class);
	private HashMap<String, Player> tpa = new HashMap<String, Player>();
	private HashMap<UUID, Player> recentRequest = new HashMap<UUID, Player>();
	private HashMap<UUID, Player> recentRequesting = new HashMap<UUID, Player>();
	
	public void tpaRequest(Player sender, Player target) {
		if(tpa.containsKey(sender)) {
			
		}
	}

	public void tpahereRequest(Player sender, Player target) {

	}
	
	public void tpaAccept(Player player) {
		if(player.getGameMode() != GameMode.SPECTATOR) {
			
		} else player.sendMessage(format(""));
	}
	
	private void teleport(Player teleporter, Player location) {
			
	}
	
	private String format(String string) {
		return plugin.format(string);
	}
}
 