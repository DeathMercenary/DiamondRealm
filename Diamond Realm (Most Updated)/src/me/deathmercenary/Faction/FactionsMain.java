package me.deathmercenary.Faction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;

import me.deathmercenary.Main.Main;

public class FactionsMain implements Listener {

	private Main plugin = Main.getPlugin(Main.class);
	private HashMap<File, FileConfiguration> filesConfigs = plugin.files;
	private HashMap<UUID, File> playerFactionFile = new HashMap<>();
	private HashMap<UUID, FileConfiguration> playerFactionFileConfig = new HashMap<>();
	private HashMap<String, File> idFactionFile = new HashMap<>();
	private HashMap<String, FileConfiguration> idFactionFileConfig = new HashMap<>();

	private File factionsFile = new File(plugin.getDataFolder(), "/factions/factions.yml");
	private File factionsPlayers = new File(plugin.getDataFolder(), "/factions/playerfactions.yml");
	private File factionsConfigFile = new File(plugin.getDataFolder(), "/factions/config.yml");

	private File factionsBase = new File(plugin.getDataFolder(), "/schems/FactionBase.schematic");

//	public HashMap<Player, File> playerSkillsFile = new HashMap<>();

// For creating new factions
	public HashMap<String, File> faction; // Factions Files and their factionID
	public HashMap<UUID, String> playerToFac = new HashMap<>();
	public HashMap<String, UUID> onlineFaction = new HashMap<>(); // Online Faction ID and faction Leader
	public HashMap<String, String> allFactions = new HashMap<>(); // Faction name, Faction ID <Key, Value>
	public HashMap<HashMap<String, File>, Player> factionLeader = new HashMap<>(); // Faction file/ID hashmap, leader of
																					// faction
	public Integer totalFactions = 0;
	public Location factionBase = new Location(Bukkit.getWorld("factions"), 0, 0, 0);

	private Double factionBaseX = 0.0;
	private Double factionBaseZ = 0.0;
	private Double factionBasedX = 0.0;
	private Double factionBasedZ = -1.0;

//
// Player Faction yaml file, faction they are in, last time used /f gift, homes set in the faction
//
	public void loadFactions() {
		loadAllFactionNamesId();
		loadPlayerFactions();
		loadFactionConfig();
	}

	//
	// Player/Faction YAML File Load/Save
	//

	private void createPlayerYaml(Player player) {
		if (playerFactionFileConfig.get(player.getUniqueId()) == null) {
			if (!plugin.getDataFolder().exists()) {
				plugin.getDataFolder().mkdir();
			}
			File file = new File(plugin.getDataFolder(), "/factions/players/" + player.getUniqueId() + ".yml");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					Bukkit.getServer().getConsoleSender().sendMessage(plugin
							.format("&cCould not create file &e" + file.toPath().toString() + " | &cError: &7" + e));
				}
			}
			FileConfiguration playerfile = YamlConfiguration.loadConfiguration(file);
			try {
				playerfile.save(file);
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(plugin
						.format("&cCould not save the &e" + file.toPath().toString() + " | &cfile. &cError: &7" + e));
			}
			playerFactionFile.put(player.getUniqueId(), file);
			playerFactionFileConfig.put(player.getUniqueId(), playerfile);
		}
	}

	private void savePlayerYaml(Player player) {
		createPlayerYaml(player);
		File file = playerFactionFile.get(player.getUniqueId());
		FileConfiguration playerfile = playerFactionFileConfig.get(player.getUniqueId());

		if (playerfile.get("previousfactions") == null) {

		}
		try {
			playerfile.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createFactionYaml(String facID) {
		if (idFactionFileConfig.get(facID) == null) {
			if (!plugin.getDataFolder().exists()) {
				plugin.getDataFolder().mkdir();
			}
			File file = new File(plugin.getDataFolder(), "/factions/factions/" + facID + ".yml");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					Bukkit.getServer().getConsoleSender().sendMessage(plugin
							.format("&cCould not create file &e" + file.toPath().toString() + " | &cError: &7" + e));
				}
			}
			FileConfiguration facFile = YamlConfiguration.loadConfiguration(file);
			try {
				facFile.save(file);
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(plugin
						.format("&cCould not save the &e" + file.toPath().toString() + " | &cfile. &cError: &7" + e));
			}
			idFactionFile.put(facID, file);
			idFactionFileConfig.put(facID, facFile);
		}
	}

	private void saveFactionYaml(String facID) {
		createFactionYaml(facID);
		File file = idFactionFile.get(facID);
		FileConfiguration facFile = idFactionFileConfig.get(facID);

		if (facFile.get("leader") == null) {

		}
		try {
			facFile.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadFile(File file) {
		plugin.filesFunctions.loadFile(file);
	}

	private void saveFile(File file) {
		plugin.filesFunctions.saveFile(file);
	}

	private FileConfiguration grabFileConfig(File file) {
		return plugin.filesFunctions.getFileConfig(file);
	}

	//
	// Config/PlayerFactions Files Load
	//
	private void loadPlayerFactions() {
		loadFile(factionsPlayers);
		FileConfiguration config = grabFileConfig(factionsPlayers);
		Set<String> uuidNodes = config.getKeys(true);
		if (!uuidNodes.isEmpty()) {
			for (String uuid : uuidNodes) {
				playerToFac.put(UUID.fromString(uuid), config.getString(uuid));
			}
		}
	}

	private void loadFactionConfig() {
		loadFile(factionsConfigFile);
		totalFactions = grabFileConfig(factionsConfigFile).getInt("totalfactions");
		saveFile(factionsConfigFile);
	}

	//
	// Player Join/Quit Event
	//
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		createPlayerYaml(event.getPlayer());
		if (factionCheck(event.getPlayer()) == true) {
			String faction = grabFileConfig(factionsFile).getString(event.getPlayer().getUniqueId().toString());
			if (!onlineFaction.containsKey(faction)) {
				// onlineFaction.put(faction, getFName());
			}
		}
	}

	//
	// Player Join/Quit Event
	//
	//
	// Faction ID/NAME Updates
	//
	private void loadAllFactionNamesId() {
		if (grabFileConfig(factionsFile) == null) {
			loadFile(factionsFile);
		}
		FileConfiguration config = grabFileConfig(factionsFile);
		Set<String> nodes = config.getKeys(true);
		if (!nodes.isEmpty()) {
			for (String node : nodes) {
				Bukkit.broadcastMessage("node: " + node + " string:" + config.getString(node));
				allFactions.put(node, config.getString(node));
			}
		}
		Bukkit.broadcastMessage(allFactions.toString());
	}

	private void updateFactionName(String facName, String facID) {
		if (grabFileConfig(factionsFile) == null) {
			loadAllFactionNamesId();
		}
		saveFile(factionsFile);
		FileConfiguration config = grabFileConfig(factionsFile);
		if (allFactions.containsKey(facID)) {
			saveFactionNameId(facName, facID);
		} else if (allFactions.containsValue(facName)) {
			return;
		} else {
			saveFactionNameId(facName, facID);
		}

	}

	private void saveAllFactionNamesId() {

	}

	private void saveFactionNameId(String facName, String facID) {
		saveFile(factionsFile);
		FileConfiguration config = grabFileConfig(factionsFile);
		config.set(facID, facName);
		allFactions.put(facID, facName);
		saveFile(factionsFile);
	}

//
// Faction ID/NAME Updates
//

	private String getFactionID(Player player) {
		if (factionCheck(player) == true) {
			return playerFactionFileConfig.get(player.getUniqueId()).getString("faction");
		}
		return null;
	}

//
// (FUNCTIONS) Deleting a faction
//
	private void removeFactionNameId(String facID) {
		if (allFactions.containsKey(facID)) {
			allFactions.remove(facID);
		}
		if (grabFileConfig(factionsFile).contains(facID)) {
			grabFileConfig(factionsFile).set(facID, null);
		}
		FileConfiguration fac = idFactionFileConfig.get(facID);
		saveFactionYaml(facID);
		idFactionFile.get(facID).delete();
	}

//
// (FUNCTIONS) Deleting a Faction 
//

	public void factionCommand(Player p, String[] args) {
		savePlayerYaml(p);
		FileConfiguration pf = playerFactionFileConfig.get(p.getUniqueId());
		Boolean inFaction = factionCheck(p);
		String facID = null;
		if (inFaction == true) {
			facID = pf.getString("faction");
		}
		if (args.length != 0) {
			if (args[0].toLowerCase().contains("help")) {
				factionHelpCommand(p);
				return;
			} else if (args[0].toLowerCase().contains("create")) {
				if (args.length == 2) {
					p.sendMessage("test" + inFaction.toString());
					if (inFaction == true) {
						p.sendMessage(format("Your factoin is" + getFactionID(p)));
					} else {
						createFactionCommand(args[1], p);
					}
				} else {
					p.sendMessage(format("&7Command is /f create <facName>"));
				}
			} else if (args[0].toLowerCase().contains("invite")) {
				if (facID == null) {
					p.sendMessage(format("You need to be in a faction to execute this command."));
				} else {
					if (args.length == 2) {
						FileConfiguration config = idFactionFileConfig.get(facID);
						if (getPlayerFromString(args[0]) != null) {
							Player playerArg = getPlayerFromString(args[1]);
							if (config.get("invites." + playerArg.getUniqueId().toString()) == null) {
								invitePlayer(facID, playerArg);
							} else {
								p.sendMessage(format("player has already been invited"));
							}
						}
					} else {
						p.sendMessage(format("&7Command is /f invite (player)"));
					}
				}
			}
		} else {
			if (inFaction == true) {
				if (!onlineFaction.containsKey(facID)) {
					bringFacOnline(facID, p);
				}

			} else {
				factionHelpCommand(p);
				Date now = new Date();
				SimpleDateFormat formats = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				Bukkit.broadcastMessage("[" + formats.format(now) + "] ");

				now.setTime(now.getTime() + TimeUnit.HOURS.toMillis(4));
				Bukkit.broadcastMessage("[" + formats.format(now) + "] ");
			}
		}
	}

	private void factionGUI(String facID, Player p) {
		if (!onlineFaction.containsKey(facID)) {
			bringFacOnline(facID, p);
		}
	}

	private void bringFacOnline(String facID, Player player) {
		if (!onlineFaction.containsKey(facID)) {
			createFactionYaml(facID);
			FileConfiguration pf = playerFactionFileConfig.get(player.getUniqueId());
			onlineFaction.put(facID, UUID.fromString(pf.getString("leader")));
		}
	}

	private void factionHelpCommand(Player player) {
		player.sendMessage(format("&7--------------------[&bFactions&7]---------------------\n"
				+ "&b/f help &7Prints this message\n" + "&b/f create [name] &7Create a faction\n"
				+ "&b/f rename [name] &7Rename your faction\n"
				+ "&b/f invite/uninvite [player] &7Invite/Uninvite someone to your faction\n"
				+ "&b/f promote/demote [player] &7Promote/Demote a player in your faction\n"
				+ "&b/f home &7Teleport to your faction's home\n" + "&b/f outside &7Teleport to your faction's plot\n"
				+ "&b/f sethome &7Chance your faction's home location\n" + "&b/f perm &7Change permissions per rank\n"
				+ "&b/f f [faction] &7Check a faction's stats\n" + "&b/f who [name] &7See in what faction a player is\n"
				+ "&b/f list [faction] &7See who's all in the specified faction\n"
				+ "&b/f raid &7Scout and raid other factions\n" + "&b/f war &7Fight other factions in PvP for elo\n"
				+ "&7-------------------------------------------------"));
	}

	private Boolean factionCheck(Player player) {
		savePlayerYaml(player);
		if (playerFactionFileConfig.get(player.getUniqueId()).getString("faction") != null) {
			return true;
		} else {
			return false;
		}
	}

	private String getFName(String string) {
		return string;
	}

	private void newFaction(String facName, String facID, Player p) {
		createFactionYaml(facID);
		FileConfiguration config = idFactionFileConfig.get(facID);
		String[] factionNames = { facName };
		config.set("faction.name", facName);
		config.set("faction.namehistory." + facName, returnTime());
		config.set("raid.energy", 0);
		config.set("faction.balance", 0);
		config.set("online", 0);
		config.set("war.losses", 0);
		config.set("war.wins", 0);
		config.set("war.elo", 1000);
		config.set("faction.created", returnTime());
		config.set("faction.home", "");
		// config.set("raid.logs", "");
		config.set("raid.active", false);
		// config.set("raid.raiders", 0);
		config.set("settings.demote.officer", true);
		config.set("settings.promote.officer", true);
		config.set("settings.raidgui.officer", true);
		config.set("settings.invite.officer", true);
		config.set("settings.uninvite.officer", true);
		config.set("settings.kick.officer", true);
		config.set("settings.ally.officer", true);
		config.set("settings.unally.officer", true);
		config.set("settings.inventory.officer", true);
		config.set("settings.inventory.member", true);
		config.set("settings.inventory.recruit", true);
		config.set("settings.joinraid.officer", true);
		config.set("settings.joinraid.member", true);
		config.set("settings.joinraid.recruit", true);
		config.set("settings.interact.officer", true);
		config.set("settings.interact.member", true);
		config.set("settings.build.officer", true);
		config.set("settings.build.member", true);
		config.set("settings.withdraw.officer", true);
		config.set("players." + p.getUniqueId().toString(), p.getName());
		updateLeader(facID, p);

		addPlayer(facID, p);

		updateFactionName(facName, facID);
		memberCount(facID);
	}

	private void memberCount(String id) {
		FileConfiguration config = idFactionFileConfig.get(id);
		int number = 0;
		Set<String> set = config.getConfigurationSection("players").getKeys(true);
		for (String node : set) {
			if (getPlayerFromString(config.getString("players." + node)) != null) {
				number++;
			}
		}
		config.set("online", number);
		saveFactionYaml(id);
	}

	private void pastePlot() {
		Clipboard clipboard;
		ClipboardFormat format = ClipboardFormats.findByFile(factionsBase);
		try (ClipboardReader reader = format.getReader(new FileInputStream(factionsBase))) {
			clipboard = reader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (EditSession editSession = WorldEdit.getInstance().newEditSession(world, -1)) {
		    Operation operation = new ClipboardHolder(clipboard)
		            .createPaste(editSession)
		            .to(BlockVector3.at(0, 0, 0))
		            .ignoreAirBlocks(true)
		            .build();
		    Operations.complete(operation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// WorldEdit
	}

	private void updatePlots() {

	}

	private void resetPlots() {

	}

	private Location newFacLoc(Double x, Double y, Double z) {
		return new Location(Bukkit.getWorld("factions"), x, y, z);
	}

	private void disbandFactionCommand(Player player) {
		// playerFactionFileConfig.get(player)
		playerFactionFileConfig.get(player.getUniqueId()).set("faction", null);
		// remove
		// removeFactionNameId
	}

	private void createFactionCommand(String facName, Player player) {
		if (facName.matches("^[a-zA-Z0-9]{4,16}+")) {
			for (String key : allFactions.values()) {
				Bukkit.broadcastMessage(key);
				if (key.equalsIgnoreCase(facName)) {
					player.sendMessage(format("&b&lHey! &7The faction named &b" + facName
							+ " &7has already been created&f. &7Please try another name&f."));
					return;
				}
			}

			String id = generateFacId();

			newFaction(facName, id, player);

			playerFactionFileConfig.get(player.getUniqueId()).set("faction", id);
			savePlayerYaml(player);
		} else {
			player.sendMessage(format(
					"&b&lHey! &7Your faction name has to be &44-16&7 charcaters and &4not include any symbols&f."));
		}
	}

	private void createFaction(String id, String name) {

	}

	private String generateFacId() {
		totalFactions++;
		grabFileConfig(factionsConfigFile).set("totalfactions", totalFactions);
		saveFile(factionsConfigFile);
		return totalFactions + "-" + randomChar() + randomChar() + randomChar() + randomChar() + randomChar();
	}

	private void loadFaction(String string) {

	}

	private void updateLeader(String id, Player player) {
		createFactionYaml(id);
		FileConfiguration config = idFactionFileConfig.get(id);
		config.set("leader." + player.getUniqueId().toString(), player.getName().toString());
		saveFactionYaml(id);
	}

	private void addPlayer(String id, Player player) {
		FileConfiguration config = idFactionFileConfig.get(id);
		config.set("players." + player.getUniqueId().toString(), player.getName().toString());
		saveFactionYaml(id);
		memberCount(id);
	}

	private void invitePlayer(String id, Player player) {
		FileConfiguration config = idFactionFileConfig.get(id);
		if (config.getString("invites." + player.getUniqueId().toString()) == null) {
			config.set("invites." + player.getUniqueId().toString(), player.getName().toString());
			saveFactionYaml(id);
			new BukkitRunnable() {

				@Override
				public void run() {
					if (config.getString("invites." + player.getUniqueId().toString()) != null) {
						config.set("invites." + player.getUniqueId().toString(), null);
						saveFactionYaml(id);
						player.sendMessage(format("Your invite expired"));
					}

				}
			}.runTaskLater(plugin, 1200);
		}
	}

	private void uninvitePlayer(String id, Player player) {

	}

	private void kickPlayer(String id, Player player) {

	}

	private void updateBanner(String id, ItemStack items) {

	}

	private String returnTime() {
		Date now = new Date();
		SimpleDateFormat formats = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return "[" + formats.format(now) + "]";
	}

	private Player getPlayerFromString(String string) {
		return Bukkit.getPlayer(string);
	}

	private char randomChar() {
		Random rnd = new Random();
		return (char) ('a' + rnd.nextInt(26));
	}

	private String format(String string) {
		return plugin.format(string);
	}

}
