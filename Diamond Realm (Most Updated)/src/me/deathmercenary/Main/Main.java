package me.deathmercenary.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.deathmercenary.Chat.ChatMain;
import me.deathmercenary.CustomItems.CustomItemsMain;
import me.deathmercenary.CustomItems.CustomItemsUpgrading;
import me.deathmercenary.DeathPoint.DeathPointMain;
import me.deathmercenary.Developer.DeveloperMain;
import me.deathmercenary.Events.EventsMain;
import me.deathmercenary.Faction.FactionsMain;
import me.deathmercenary.Fly.FlyMain;
import me.deathmercenary.Functions.YamlFunctions;
import me.deathmercenary.Novis.NovisMain;
import me.deathmercenary.Skills.MainSkills;
import me.deathmercenary.Skills.Prestige;
import me.deathmercenary.Tpa.TpaMain;
import me.deathmercenary.Warzone.WarzoneMain;
import me.deathmercvenary.Spawn.SpawnMain;

public class Main extends JavaPlugin implements Listener {
	private Commands commands;// = new Commands();
	private ArrayList<String> lore;
	public HashMap<File, FileConfiguration> files = new HashMap<>();
	public YamlFunctions functionsFiles = new YamlFunctions(); // skills = new MainSkills(this, prestige);
	public Files filesFunctions;
	public CustomItemsMain customitems;
	public Prestige prestige;
	public MainSkills skills;
	public NovisMain novis;
	public EventsMain events;
	public Items items;
	public WarzoneMain warzone;
	public ServerFunctions functions;
	public DeveloperMain devClass;
	public DeathPointMain dp;
	public SpawnMain spawn;
	public FlyMain fly;
	public TpaMain tpa;
	public ChatMain chat;
	public FactionsMain fac;
	public CustomItemsUpgrading upgrade;
	public Logs logs;
	public String servername = "&b&lPrism&f&lForge &e&l» &7";
	public String servererror = "&c&lERROR &4&l» &7";
	// public HashMap<File, FileConfiguration> files = new HashMap<>();
	// public HashMap<Player, Hologram> holograms = new HashMap<>();
	// public HashMap<Player, Boolean> hologramCheck = new HashMap<>();

	public void onEnable() {
		initialClass();
		loadConfigurations();
		getServer().getPluginManager().registerEvents(new Events(), this);
		getServer().getPluginManager().registerEvents(skills, this);
		getServer().getPluginManager().registerEvents(customitems, this);
		getServer().getPluginManager().registerEvents(novis, this);
		getServer().getPluginManager().registerEvents(warzone, this);
		getServer().getPluginManager().registerEvents(dp, this);
		getServer().getPluginManager().registerEvents(fly, this);
		getServer().getPluginManager().registerEvents(spawn, this);
		getServer().getPluginManager().registerEvents(chat, this);
		getServer().getPluginManager().registerEvents(upgrade, this);
		getServer().getPluginManager().registerEvents(fac, this);

		for (String name : commands.commands) {
			getCommand(name).setExecutor(commands);
		}
		skills.setUpSkills();
		novis.lootSetup();
		warzone.setupChestLoot();
		for (Player player : Bukkit.getOnlinePlayers()) {
			// igneous_boots
			// player.sendMessage(customitems.getEnchants("weapon.common.dummy_sword.enchants",
			// 1) + " Test");
			// player.getInventory().addItem(customitems.createItem("dummy_sword", "common",
			// "weapon", 1, 1));
			player.getInventory().addItem(customitems.createCustomItem("igneous_boots", "rare", "armor", 4, 5));
			player.getInventory().addItem(customitems.createCrystal("common"), customitems.createCrystal("epic"),
					customitems.createCrystal("rare"), customitems.createCrystal("event"),
					customitems.createCrystal("legendary"), customitems.createCrystal("mythic"));
			skills.loadSkills(player);
			chat.updatePlayerName(player);
		}
	}

	public void onDisable() {
		prestige.savePrestigeFile();
		for (Player player : Bukkit.getOnlinePlayers()) {
			skills.saveSkills(player);
		}
	}

	public void loadDir() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
	}

	public void reloadPlugin() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			skills.saveSkills(player);
			prestige.playerPrestigeSync(player);
			new BukkitRunnable() {
				public void run() {
					player.kickPlayer("Server is reloading.");
				}
			}.runTaskLater(this, 40);
		}
		new BukkitRunnable() {
			public void run() {
				Bukkit.reload();
			}
		}.runTaskLater(this, 100);
	}

	public void loadConfigurations() {
		prestige.loadPrestigeFile();
		customitems.loadFiles();
		fac.loadFactions();
		saveDefaultConfig();
	}

	public Commands getCommands() {
		return commands;
	}

	public Items grabItems() {
		return items;
	}

	public ServerFunctions grabFunctions() {
		return functions;
	}

	public MainSkills grabSkills() {
		return skills;
	}

	public Logs grabLogs() {
		return logs;
	}

	public void initialClass() {
		filesFunctions = new Files();
		customitems = new CustomItemsMain();
		prestige = new Prestige();
		events = new EventsMain();
		skills = new MainSkills(this, prestige);
		warzone = new WarzoneMain();
		novis = new NovisMain();
		items = new Items();
		devClass = new DeveloperMain();
		commands = new Commands();
		spawn = new SpawnMain();
		dp = new DeathPointMain();
		fly = new FlyMain();
		fac = new FactionsMain();
		tpa = new TpaMain();
		chat = new ChatMain();
		upgrade = new CustomItemsUpgrading();
		functions = new ServerFunctions();
		logs = new Logs();
	}

	// YAML

	// Functions

	public String format(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public void playerConfigUpdate(Player player) {

	}

	// getItem(Material.FEATHER, 1, "", lore)
	public ItemStack getItem(Material material, Integer amount, String displayname, ArrayList<String> lores) {
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();
		lore = new ArrayList<String>();
		meta.setDisplayName(format(displayname));
		for (String lores2 : lores) {
			lore.add(format(lores2));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return removeNBT(item);
	}

	public ItemStack removeNBT(ItemStack item) {
		final ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
		meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
		meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
		meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_PLACED_ON });
		meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
		meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });

		item.setItemMeta(meta);
		return item;
	}

}
