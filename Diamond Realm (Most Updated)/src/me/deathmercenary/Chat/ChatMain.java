package me.deathmercenary.Chat;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.deathmercenary.Main.Main;


public class ChatMain implements Listener {

	private Main main = Main.getPlugin(Main.class);
	
	private HashMap<Player, Integer> level = main.skills.level;
	
	@EventHandler
	public void joinEvent(PlayerJoinEvent event) {
		updatePlayerName(event.getPlayer());
	}
	
	private String getPrefixFormat(Player player) {
		return textThemeUpdate("BETA", "beta");
	}
	private String getNameFormat(Player player) {
		return textThemeUpdate(player.getName(), "beta");
	}
	
	private String textThemeUpdate(String string, String theme) {
		String[] split = string.split("");
		String n = "";
		if(theme.toLowerCase().equals("beta")) {
			int i = 0;
			for(String s : split) {
				if(i == 0) {
					n = "&9&l" + s;
				} else if (i % 2 == 0) { //0 = even number
					n = n + "&9&l" + s;
				} else {
					n = n + "&b&l" + s;
				}
				i++;
			}
		}
		return n;
	}
	private String getLevelFormat(Player player) {
		String levelformat;
		if(level.get(player) <= 19) {
			levelformat = "&a[" + level.get(player) + "]";
		} else if(level.get(player) <= 39) {
			levelformat = "&e[" + level.get(player) + "]";
		}else if(level.get(player) <= 59) {
			levelformat = "&c[" + level.get(player) + "]";
		}else if(level.get(player) <= 65) {
			levelformat = "&b[" + level.get(player) + "]";
		} else {
			levelformat = "&d[" + level.get(player) + "]";
		}
		return levelformat;
	}
	
	public void updatePlayerName(Player player) {
		player.setDisplayName(format(getLevelFormat(player) + " &7" + getNameFormat(player) + " " + getPrefixFormat(player)));
	}
	
	private String format(String string) {
		return main.format(string);
	}
}
