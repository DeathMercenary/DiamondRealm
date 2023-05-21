package me.deathmercenary.Main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Files {
	private Main plugin = Main.getPlugin(Main.class);

	private HashMap<File, FileConfiguration> filesConfigs = plugin.files;

	public void loadFile(File file) {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
			Bukkit.getServer().getConsoleSender()
					.sendMessage(format("&aCreated &e" + plugin.getDataFolder().toString() + " &aDirectory"));
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
				Bukkit.getServer().getConsoleSender().sendMessage(format("&aCreated &e" + file.getName() + " &aFile"));
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(
						format("&cCould not create file &e" + file.toPath().toString() + " | &cError: &7" + e));
			}
		}
		FileConfiguration prestigeFileConfig = YamlConfiguration.loadConfiguration(file);
		Bukkit.getServer().getConsoleSender().sendMessage(plugin.format("&aReloaded " + file.getName() + " File"));
		filesConfigs.put(file, prestigeFileConfig);
		saveFile(file);
	}

	public void saveFile(File file) {
		if (filesConfigs.get(file) == null) {
			loadFile(file);
		}
		try {
			filesConfigs.get(file).save(file);
			Bukkit.getServer().getConsoleSender().sendMessage(format("&aSaved " + file.getName() + " File"));
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(
					format("&cCould not save the &e" + file.toPath().toString() + " | &cfile. &cError: &7" + e));
		}
	}
	
	public FileConfiguration getFileConfig(File file) {
		if (!filesConfigs.containsKey(file)) {
			reloadFile(file);
		}
		return filesConfigs.get(file);
	}
	
	public void reloadFile(File file) {
		//plugin.loadDir();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(
						format("&cCould not create file &e" + file.toPath().toString() + " | &cError: &7" + e));
			}
		}
		if (!filesConfigs.containsKey(file)) {
			FileConfiguration skillsConfigManager = YamlConfiguration.loadConfiguration(file);
			filesConfigs.put(file, skillsConfigManager);
		}
		loadFile(file);

	}
	
	private String format(String string) {
		return plugin.format(string);
	}
}
