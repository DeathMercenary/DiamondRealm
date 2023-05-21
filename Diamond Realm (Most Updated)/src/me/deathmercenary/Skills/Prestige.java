package me.deathmercenary.Skills;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.deathmercenary.Main.Main;

public class Prestige implements Listener {
	private Main plugin = Main.getPlugin(Main.class);

	private File prestigeFile = new File(plugin.getDataFolder(), "/permanent/prestige.yml");

	private HashMap<File, FileConfiguration> filesConfigs = plugin.files;
	public HashMap<Player, Integer> prestige = new HashMap<>();

	public void prestigeSet(Player sender, Player arg, Integer amount) {
		if (sender.hasPermission("prismforge.prestigeset")) {
			playerPrestigeSet(arg, amount);
			plugin.skills.skillspointsFix(arg);
		} else {
			sender.sendMessage(plugin.format(plugin.servererror
					+ "&7You do not have permissions for this command&f. &7If you belive this to be an error, please contact an &6admin &7for assitance&f."));
		}
	}

	public void prestigeCommand(Player player) {
		if (plugin.skills.level.get(player) >= 66) {
			playerPrestigeSet(player, playerGrabPrestige(player) + 1);
			plugin.skills.skillsReset(player);
		} else {
			player.sendMessage(
					plugin.format(plugin.servererror + "&7You have to be level &666 &7to use this command&f."));
		}
	}

	public void loadPrestigeFile() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
			Bukkit.getServer().getConsoleSender()
					.sendMessage(plugin.format("&aCreated &e" + plugin.getDataFolder().toString() + " &aDirectory"));
		}
		if (!prestigeFile.exists()) {
			try {
				prestigeFile.createNewFile();
				Bukkit.getServer().getConsoleSender()
						.sendMessage(plugin.format("&aCreated &e" + prestigeFile.getName() + " &aFile"));
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(plugin.format(
						"&cCould not create file &e" + prestigeFile.toPath().toString() + " | &cError: &7" + e));
			}
		}
		FileConfiguration prestigeFileConfig = YamlConfiguration.loadConfiguration(prestigeFile);
		Bukkit.getServer().getConsoleSender()
				.sendMessage(plugin.format("&aReloaded " + prestigeFile.getName() + " File"));
		filesConfigs.put(prestigeFile, prestigeFileConfig);
		savePrestigeFile();
	}

	public void savePrestigeFile() {
		if (filesConfigs.get(prestigeFile) == null) {
			loadPrestigeFile();
		}
		try {
			filesConfigs.get(prestigeFile).save(prestigeFile);
			Bukkit.getServer().getConsoleSender().sendMessage(plugin.format("&aSaved Prestige File"));
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(plugin.format(
					"&cCould not save the &e" + prestigeFile.toPath().toString() + " | &cfile. &cError: &7" + e));
		}
	}

	public void playerPrestigeSync(Player player) {
		if (filesConfigs.get(prestigeFile) == null) {
			loadPrestigeFile();
		}
		if (prestige.get(player) == null) {
			prestige.put(player, filesConfigs.get(prestigeFile).getInt(player.getUniqueId().toString()));
		}

		filesConfigs.get(prestigeFile).set(player.getUniqueId().toString(), prestige.get(player));
		savePrestigeFile();
	}

	public void playerPrestigeSet(Player player, Integer p) {
		if (filesConfigs.get(prestigeFile) == null) {
			loadPrestigeFile();
		}
		prestige.put(player, p);
		playerPrestigeSync(player);
	}

	public Integer playerGrabPrestige(Player player) {
		if (filesConfigs.get(prestigeFile) == null) {
			loadPrestigeFile();
		}
		if (!filesConfigs.get(prestigeFile).contains(player.getUniqueId().toString())) {
			playerPrestigeSet(player, 0);
		}
		if (prestige.get(player) == null) {
			playerPrestigeSync(player);
		} else if (prestige.get(player) != filesConfigs.get(prestigeFile).getInt(player.getUniqueId().toString())) {
			playerPrestigeSync(player);
		}

		return prestige.get(player);
	}

	public void serverPrestigeSync() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			playerPrestigeSync(player);
		}
	}
}
