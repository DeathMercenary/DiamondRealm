package me.deathmercenary.Developer;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.deathmercenary.Main.Main;

public class DeveloperMain implements Listener {
	private Main plugin = Main.getPlugin(Main.class);
	private HashMap<Player, Boolean> devToggle = new HashMap<>();


	public void developerToggle(Player player) {
		devToggler(player, devToggleCheck(player));
	}

	public void devMessage(Player player, String msg) {
		if (devToggleCheck(player) == true)
			player.sendMessage(format("&4[&c&lDEV&4] &e&l» &c" + msg));
	}

	public Boolean devToggleCheck(Player player) {
		if (devToggle.containsKey(player))
			return devToggle.get(player);
		else
			return false;
	}

	private void devToggler(Player player, Boolean boo) {
		boo = !boo;
		devToggle.put(player, boo);
		player.sendMessage(
				format(plugin.servername + "&cDeveloper Messages &7have been set to &b" + boo.toString() + "&f."));
	}

	private String format(String string) {
		return plugin.format(string);
	}
}
