package me.deathmercenary.Fly;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.deathmercenary.Main.Main;

public class FlyMain implements Listener {

	private Main main = Main.getPlugin(Main.class);

	public void flyToggle(Player player) {
		if (player.getAllowFlight() == true) {
			flyOff(player);
		} else {
			flyOn(player);
		}
	}

	public void flyOff(Player player) {
		player.setAllowFlight(false);
	}

	public void flyOn(Player player) {
		player.setAllowFlight(true);
	}

	@EventHandler
	public void worldChange(PlayerChangedWorldEvent event) {
		flyOff(event.getPlayer());

	}

	@EventHandler
	public void joinEvent(PlayerJoinEvent event) {
		event.getPlayer().setAllowFlight(true);
	}
}
