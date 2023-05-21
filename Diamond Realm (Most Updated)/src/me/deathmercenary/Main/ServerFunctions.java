package me.deathmercenary.Main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ServerFunctions {

	private Main plugin = Main.getPlugin(Main.class);
	private HashMap<String, GameMode> gamemodes = new HashMap<>();
	
	public void saveFile(FileConfiguration fileconfig, File file) {
		try {
			fileconfig.save(file);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(
					plugin.format("&cCould not save the &e" + file.toPath().toString() + " | &cfile. &cError: &7" + e));
		}
	}

	public String removeFormat(String string) {
		string = string.replaceAll("§1", "");
		string = string.replaceAll("§2", "");
		string = string.replaceAll("§3", "");
		string = string.replaceAll("§4", "");
		string = string.replaceAll("§5", "");
		string = string.replaceAll("§6", "");
		string = string.replaceAll("§7", "");
		string = string.replaceAll("§8", "");
		string = string.replaceAll("§9", "");
		string = string.replaceAll("§0", "");
		string = string.replaceAll("§a", "");
		string = string.replaceAll("§b", "");
		string = string.replaceAll("§c", "");
		string = string.replaceAll("§d", "");
		string = string.replaceAll("§e", "");
		string = string.replaceAll("§f", "");
		string = string.replaceAll("§k", "");
		string = string.replaceAll("§l", "");
		string = string.replaceAll("§m", "");
		string = string.replaceAll("§n", "");
		return string;
	}
	
	public void gamemodesUpdate() {
		gamemodes.put("2", GameMode.ADVENTURE);
		gamemodes.put("a", GameMode.ADVENTURE);
		gamemodes.put("adventure", GameMode.ADVENTURE);
		gamemodes.put("gm2", GameMode.ADVENTURE);
		gamemodes.put("gma", GameMode.ADVENTURE);
		gamemodes.put("gmadventure", GameMode.ADVENTURE);

		gamemodes.put("1", GameMode.CREATIVE);
		gamemodes.put("c", GameMode.CREATIVE);
		gamemodes.put("creative", GameMode.CREATIVE);
		gamemodes.put("gm1", GameMode.CREATIVE);
		gamemodes.put("gmc", GameMode.CREATIVE);
		gamemodes.put("gmcreative", GameMode.CREATIVE);

		gamemodes.put("0", GameMode.SURVIVAL);
		gamemodes.put("s", GameMode.SURVIVAL);
		gamemodes.put("survival", GameMode.SURVIVAL);
		gamemodes.put("gm0", GameMode.SURVIVAL);
		gamemodes.put("gms", GameMode.SURVIVAL);
		gamemodes.put("gmsurvival", GameMode.SURVIVAL);

		gamemodes.put("3", GameMode.SPECTATOR);
		gamemodes.put("spec", GameMode.SPECTATOR);
		gamemodes.put("spectator", GameMode.SPECTATOR);
		gamemodes.put("gm3", GameMode.SPECTATOR);
		gamemodes.put("gmspec", GameMode.SPECTATOR);
		gamemodes.put("gmspectator", GameMode.SPECTATOR);
	}

	public void gamemodeUpdatePlayer(Player player, Player sender, String string, Boolean silentmode) {
		if(gamemodes.get(string) == null) {
			gamemodesUpdate();
		}
		if(!gamemodes.containsKey(string)) {
			sender.sendMessage(plugin.format(plugin.servererror + "Gamemode Options&f: &6survival &f|&6 creative &f|&6 spectator &f|&6 adventure"));
			return;
		}
		String gamemode = gamemodes.get(string).toString().toLowerCase();
		sender.sendMessage(plugin.format(plugin.servername + "Set gamemode &6" + gamemode + " &7for &b" + player.getName()));
		if(silentmode == false) {
			if(sender != player) {
				player.sendMessage(plugin.format(plugin.servername + "Set gamemode &6" + gamemode + " &7for &b" + player.getName()));
			}
		}
		player.setGameMode(gamemodes.get(string));

	}
}
