package me.deathmercenary.Skills;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftTippedArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import io.netty.util.internal.ThreadLocalRandom;
import me.deathmercenary.Main.Main;

public class MainSkills2 implements Listener {

	private final Main plugins;
	private final Prestige prest;

	public MainSkills2(Main plugins, Prestige prest) {
		this.plugins = plugins;
		this.prest = prest;
	}

	// private File skillsConfig = new File(plugin.getDataFolder(),
	// "/Skills/config.yml");
	// private HashMap<File, FileConfiguration> filesConfigs = this.filesConfigs;

	private Main plugin = Main.getPlugin(Main.class);

	private File skillsConfig = new File(plugin.getDataFolder(), "/skills/config.yml");
	private HashMap<File, FileConfiguration> filesConfigs = plugin.files;

	public HashMap<Player, File> playerSkillsFile = new HashMap<>();
	public HashMap<Player, FileConfiguration> playerSkillsFileConfig = new HashMap<>();
	public HashMap<Player, Integer> level = new HashMap<>();
	public HashMap<Player, Integer> exp = new HashMap<>();
	public HashMap<Player, Integer> totalexp = new HashMap<>();
	public HashMap<Player, Integer> strength = new HashMap<>();
	public HashMap<Player, Integer> attackspeed = new HashMap<>();
	public HashMap<Player, Integer> critchance = new HashMap<>();
	public HashMap<Player, Integer> magic = new HashMap<>();
	public HashMap<Player, Integer> archery = new HashMap<>();
	public HashMap<Player, Integer> health = new HashMap<>();
	public HashMap<Player, Integer> armor = new HashMap<>();
	public HashMap<Player, Integer> skillspoints = new HashMap<>();
	public HashMap<Player, Integer> trader = new HashMap<>();
	public HashMap<Player, Integer> alchemy = new HashMap<>();
	public HashMap<Player, Integer> miner = new HashMap<>();
	public HashMap<Player, Integer> traderexp = new HashMap<>();
	public HashMap<Player, Integer> alchemyexp = new HashMap<>();
	public HashMap<Player, Integer> minerexp = new HashMap<>();
	public HashMap<Player, Integer> prestiges = new HashMap<>();

	public String[] skills = { "level", "exp", "strength", "attackspeed", "critchance", "magic", "archery", "health",
			"armor", "skillspoints", "trader", "alchemy", "miner", "traderexp", "alchemyexp", "minerexp" };
	public HashMap[] skillsList = { level, exp, strength, attackspeed, critchance, magic, archery, health, armor,
			skillspoints, trader, alchemy, miner, traderexp, alchemyexp, minerexp };
	public HashMap<String, HashMap<Player, Integer>> skillToString = new HashMap<>();

	private ArrayList<String> lore;




	public void expBarUpdate(Player player) {
		if (player.getLevel() != level.get(player))
			player.setLevel(level.get(player));
		if (player.getLevel() != 66) {
			Double D = Double.valueOf((double) exp.get(player) / (double) getExpFromLevel(level.get(player)));
			if (player.getExp() != D.floatValue())
				player.setExp(D.floatValue());
		} else if (player.getLevel() == 66) {
			player.setExp(0f);
		}

	}

	public void levelUpdate(Player player) {
		int currentExp = exp.get(player);// Grab Players Experience from Hashmap
		int currentLevel = level.get(player);// Grab Players Level from Hashmap
		int nextLevel = currentLevel + 1;//
		int levelUpExp = getExpFromLevel(currentLevel);
		if (nextLevel >= 67) {
			player.sendMessage(format("You have reached max level!"));
			return;
		}
		if (currentExp > levelUpExp) {
			level.replace(player, nextLevel);
			exp.replace(player, currentExp - levelUpExp);
			skillspoints.replace(player, skillspoints.get(player) + 2);
			player.sendMessage("levelup! You are now level " + nextLevel + " and you need ");
			levelUpdate(player);
			plugin.chat.updatePlayerName(player);
		}
		// int currentTotalExp = exp.get(player);
		// level = Math.cbrt(exp/3) - 3
		// int level; // level = (square root of exp / 3) - 3
		// int levels = (int) (Math.cbrt(exp.get(player) / 3) - 3);

		expBarUpdate(player);
	}

	public void totalExpBackup(Player player) {
		int lvl = level.get(player);
		int xp = exp.get(player);
		int totalxp = totalexp.get(player);
		int newxp = 0;
		if (lvl != 0) {
			for (int i = 0; i != lvl; i++) {
				newxp = newxp + getExpFromLevel(i);
			}
		}
		newxp = newxp + xp;
		if (totalxp != newxp) {
			if (newxp > totalxp) {
				totalexp.replace(player, newxp);
				levelUpdate(player);
				saveSkills(player);
			} else if (totalxp > newxp) {
				player.sendMessage(format("Your exp seems bugged some how, please report this to devs."));
				Bukkit.broadcast(player.getName().toString() + " totalexp is greater than their level + exp.",
						"prismforge.developer");
			}
		}
	}



	public void addExp(Integer integer, Player player) {
		exp.replace(player, exp.get(player) + integer);
		player.sendMessage(format("&a+&6" + integer + " &eExp"));
		Random random = new Random();
		int x = random.nextInt(2);
		double y = random.nextInt(2) + 0.5;
		int z = random.nextInt(2);
		if (yesOrNo() == true)
			z = 0 - z;
		if (yesOrNo() == true)
			x = 0 - x;
		final Hologram hologram = HologramsAPI.createHologram(plugin, player.getLocation().add(x, y, z));
		hologram.appendTextLine(format("&a+&6" + integer + " &eExp"));
		new BukkitRunnable() {
			public void run() {
				hologram.delete();
			}
		}.runTaskLater(plugin, 30);
		levelUpdate(player);
	}


	// SKILL GUI
	//
	//
	@EventHandler
	public void skillsInteract(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getCurrentItem() == null) {
			return;
		}
		// Inventory inv = event.getInventory();
		// Inventory inv2 = event.getClickedInventory();
		// ItemStack cursorItem = event.getCursor();
		Player player = (Player) event.getWhoClicked();
		if (event.getCurrentItem() != null) {
			if (event.getCurrentItem() != null) {
				if (event.getView().getTitle().contains("Skills")) {
					String clickedItemName = event.getCurrentItem().getItemMeta().getDisplayName();
					event.setCancelled(true);
					boolean x = false;
					if (event.getCurrentItem().equals(guiItem(player, "strength"))) {
						upgradeSkill(player, strength, "strength", event.getSlot());
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1, 1);
					} else if (event.getCurrentItem().equals(guiItem(player, "attackspeed"))) {
						upgradeSkill(player, attackspeed, "attackspeed", event.getSlot());
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
					} else if (event.getCurrentItem().equals(guiItem(player, "critchance"))) {
						upgradeSkill(player, critchance, "critchance", event.getSlot());
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1, 1);
					} else if (event.getCurrentItem().equals(guiItem(player, "magic"))) {
						upgradeSkill(player, magic, "magic", event.getSlot());
						player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1, 1);
					} else if (event.getCurrentItem().equals(guiItem(player, "archery"))) {
						upgradeSkill(player, archery, "archery", event.getSlot());
						player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1);
					} else if (event.getCurrentItem().equals(guiItem(player, "health"))) {
						upgradeSkill(player, health, "health", event.getSlot());
						player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
					} else if (event.getCurrentItem().equals(guiItem(player, "armor"))) {
						upgradeSkill(player, armor, "armor", event.getSlot());
						player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1, 1);
					} else if (event.getCurrentItem().equals(skillResetItem(player))) {
						if (level.get(player) <= 15) {
							resetSkills(player);
							skillsGui(player);
						} else {
							x = true;
						}
					}
					setInventory(player, event.getSlot());
					if (x == true)
						cantClick(player, event.getSlot());
				}
			}
		}
	}

	public void cantClick(Player player, Integer slot) {
		if (player.getOpenInventory().getTitle().contains("Skills")) {
			ItemStack item = player.getOpenInventory().getItem(slot);
			player.getOpenInventory().setItem(slot, barrierItem());
			new BukkitRunnable() {
				public void run() {
					if (player.getOpenInventory().getTitle().contains("Skills")) {
						player.getOpenInventory().setItem(slot, item);
					}
				}
			}.runTaskLater(plugin, 30);
		}
	}

	public void upgradeSkill(Player player, HashMap<Player, Integer> hash, String text, Integer slot) {
		if (skillspoints.get(player) != 0) {
			if (hash.get(player) < grabConfigInt("settings.skills." + text + ".maximum")) {
				hash.replace(player, hash.get(player) + 1);
				skillspoints.replace(player, skillspoints.get(player) - 1);
			} else if (hash.get(player) >= grabConfigInt("settings.skills." + text + ".maximum")) {
				player.sendMessage(format("You have the &a&nmaximum &7possible &6skills &7for this &6skill &7type&f."));
				cantClick(player, slot);
			}
		} else {
			player.sendMessage(format(plugin.servername
					+ "You do &c&nnot&7 have enough &6skill points &7for this action&f. &bLevel up &7to earn more &6skill points!"));
			cantClick(player, slot);
		}
		if (hash == health) {
			healthUpdate(player);
		} else if (hash == attackspeed) {
			atkSpd(player);
		} else if (hash == armor) {
			armorUpdate(player);
		}
	}


	private void updateSkillGui(Player player, String string) {
		Inventory playerinv = player.getOpenInventory().getTopInventory();
		playerinv.setItem(getSlot(string), guiItem(player, string));
	}

	@EventHandler
	public void inventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		new BukkitRunnable() {
			public void run() {
				atkSpd(player);
				armorUpdate(player);
			}
		}.runTaskLater(plugin, 1);
	}

	@EventHandler
	public void armorEquip(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		new BukkitRunnable() {
			public void run() {
				armorUpdate(player);
			}
		}.runTaskLater(plugin, 1);
	}

	@EventHandler
	public void armorEquip(PlayerItemBreakEvent e) {
		Player player = e.getPlayer();
		new BukkitRunnable() {
			public void run() {
				armorUpdate(player);
			}
		}.runTaskLater(plugin, 1);
	}

	@EventHandler
	public void playerRespawn(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		new BukkitRunnable() {
			public void run() {
				armorUpdate(player);
				expBarUpdate(player);

			}
		}.runTaskLater(plugin, 1);
	}

	@EventHandler
	public void inventoryDrag(InventoryDragEvent event) {
		Player player = event.getView().getPlayer().getKiller();
		new BukkitRunnable() {
			public void run() {
				atkSpd(player);
				armorUpdate(player);
			}
		}.runTaskLater(plugin, 1);
	}

	@EventHandler
	public void inventoryMove(InventoryMoveItemEvent event) {
		Player player = (Player) event.getInitiator().getViewers();
		new BukkitRunnable() {
			public void run() {
				atkSpd(player);
				armorUpdate(player);
			}
		}.runTaskLater(plugin, 1);
	}

	@EventHandler
	public void playerItemSwitchEvent(PlayerSwapHandItemsEvent event) {
		Player player = (Player) event.getPlayer();
		new BukkitRunnable() {
			public void run() {
				atkSpd(player);
				armorUpdate(player);
			}
		}.runTaskLater(plugin, 1);
	}

	@EventHandler
	public void playerItemHeldEvent(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		new BukkitRunnable() {
			public void run() {
				atkSpd(player);
				armorUpdate(player);
			}
		}.runTaskLater(plugin, 1);
	}

	public void atkSpd(Player player) {
		devMessage(player,
				"Previous AttackSpeed: " + player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue());
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		if (mainHand.getItemMeta() == null) {
			player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
		} else if (!mainHand.getItemMeta().getDisplayName().contains("Lvl")) {
			player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
		} else {
			lore = (ArrayList<String>) mainHand.getItemMeta().getLore();
			String name = mainHand.getItemMeta().getDisplayName();
			name = removeFormat(name);
			for (String string : lore) {
				if (string.contains("Attack Speed")) {
					double atspdsk = (double) attackspeed.get(player);
					String newstring = removeFormat(string).replaceAll("attack speed: ", "");
					Double ints = Double.parseDouble(newstring);
					Double speed = ((atspdsk / 100) * 2 * ints) + ints;
					player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(speed);

				}
			}
		}
		if (attackspeed.get(player) >= 25) {
			player.setWalkSpeed(0.24f); // 0.2 = 1 (x*5 = y) x = 0.2
		} else {
			player.setWalkSpeed(0.2f);
		}
		devMessage(player,
				"Current AttackSpeed: " + player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue());
	}

	private String removeFormat(String string) {
		return plugin.functions.removeFormat(string.toLowerCase());
	}

	public void armorUpdate(Player player) {
		devMessage(player, "Current Armor: " + player.getAttribute(Attribute.GENERIC_ARMOR).getBaseValue());
		double armors = (double) armor.get(player);
		double armorValue = 0;
		double armorAmount = 0;
		player.getInventory().getArmorContents();
		// ItemStack offHands = player.getInventory().getHelmet();
		if (player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() != Material.AIR) {
			if (player.getInventory().getBoots().getItemMeta().hasLore() == true)
				if (removeFormat(player.getInventory().getBoots().getItemMeta().getLore().get(0)).contains("rarity")) {
					ItemStack offHand = player.getInventory().getBoots();
					armorValue = newArmorValue(armorValue, offHand);
					armorAmount++;
				}
		}
		if (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType() != Material.AIR) {
			if (player.getInventory().getHelmet().getItemMeta().hasLore() == true)
				if (removeFormat(player.getInventory().getHelmet().getItemMeta().getLore().get(0)).contains("rarity")) {
					ItemStack offHand = player.getInventory().getHelmet();
					armorValue = newArmorValue(armorValue, offHand);
					armorAmount++;
				}
		}
		if (player.getInventory().getChestplate() != null
				&& player.getInventory().getChestplate().getType() != Material.AIR) {
			if (player.getInventory().getChestplate().getItemMeta().hasLore() == true)
				if (removeFormat(player.getInventory().getChestplate().getItemMeta().getLore().get(0))
						.contains("rarity")) {
					ItemStack offHand = player.getInventory().getChestplate();
					armorValue = newArmorValue(armorValue, offHand);
					armorAmount++;
				}
		}
		if (player.getInventory().getLeggings() != null
				&& player.getInventory().getLeggings().getType() != Material.AIR) {
			if (player.getInventory().getLeggings().getItemMeta().hasLore() == true)
				if (removeFormat(player.getInventory().getLeggings().getItemMeta().getLore().get(0))
						.contains("rarity")) {
					ItemStack offHand = player.getInventory().getLeggings();
					armorValue = newArmorValue(armorValue, offHand);
					armorAmount++;
				}
		}
		if (player.getInventory().getItemInOffHand() != null) {
			if (player.getInventory().getItemInOffHand().getType() != Material.AIR) {
				if (player.getInventory().getItemInOffHand().getItemMeta().hasLore()) {
					if (removeFormat(player.getInventory().getItemInOffHand().getItemMeta().getLore().get(0))
							.contains("rarity")) {
						ItemStack offHand = player.getInventory().getItemInOffHand();
						armorValue = newArmorValue(armorValue, offHand);
						armorAmount++;
					}
				}
			}
		}
		player.getAttribute(Attribute.GENERIC_ARMOR)
				.setBaseValue(armorValue * ((double) armors / 25) + (armorAmount * 0.5)); // times armor value and times
		// armor gems
		player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(20);
		devMessage(player, "Current Armor: " + player.getAttribute(Attribute.GENERIC_ARMOR).getBaseValue());
		new BukkitRunnable() {

			@Override
			public void run() {
				devMessage(player, "Current Armor: " + player.getAttribute(Attribute.GENERIC_ARMOR).getBaseValue());
			}
		}.runTaskLater(plugin, 10);
		// Bukkit.broadcastMessage(armorValue + " " + armors + " " + armorAmount + " " +
		// (armorValue * ((double) armors / 25) + (armorAmount * 0.5)));
	}

	private Double newArmorValue(Double armorValue, ItemStack item) {
		if (removeFormat(item.getItemMeta().getLore().get(0)).contains("common")) {
			armorValue = armorValue + 4.5;
		} else if (removeFormat(item.getItemMeta().getLore().get(0)).contains("rare")) {
			armorValue = armorValue + 5.5;
		} else if (removeFormat(item.getItemMeta().getLore().get(0)).contains("epic")) {
			armorValue = armorValue + 7.5;
		} else if (removeFormat(item.getItemMeta().getLore().get(0)).contains("legendary")) {
			armorValue = armorValue + 9.5;
		} else if (removeFormat(item.getItemMeta().getLore().get(0)).contains("mythic")
				|| removeFormat(item.getItemMeta().getLore().get(0)).contains("event")) {
			armorValue = armorValue + 12;
		}
		return armorValue;
	}

	public void healthUpdate(Player player) {
		player.setMaxHealth((health.get(player) * 1.6) + 20);
	}


	private Integer getSlot(String string) {
		return 1;
	}

	public ItemStack skillPointsItem(Player player) {
		int i = skillspoints.get(player);
		lore = new ArrayList<String>();
		lore.add("&7Click on a skill to spend your points");
		String name = format("&6Skill points");
		return plugin.getItem(Material.EXPERIENCE_BOTTLE, amountAdjust(i), name, lore);
	}

	public ItemStack skillResetItem(Player player) {
		String minimumLevel = grabConfigString("reset.free.level");
		int i = level.get(player);
		lore = new ArrayList<String>();
		String name = format("&cThe level must be: " + minimumLevel + " to reset.");
		if (i > 15) {
			return plugin.getItem(Material.BARRIER, 1, name, lore);
		}
		return plugin.getItem(Material.ENCHANTED_BOOK, 1, name, lore);
	}

	public ItemStack barrierItem() {
		String name = format("&7Invalid Event");
		lore = new ArrayList<String>();
		lore.add("&7You cannot click on this");
		lore.add("&7on this item.");
		return plugin.getItem(Material.BARRIER, 1, name, lore);
	}

	public ItemStack maxSkills() {
		String name = format("&7Invalid Event");
		lore = new ArrayList<String>();
		lore.add("&7You cannot click on this");
		lore.add("&7on this item.");
		return plugin.getItem(Material.BARRIER, 1, name, lore);
	}

	public ItemStack noSkillPoints() {
		String name = format("&7Invalid Event");
		lore = new ArrayList<String>();
		lore.add("&7You cannot click on this");
		lore.add("&7on this item.");
		return plugin.getItem(Material.BARRIER, 1, name, lore);
	}

	public ItemStack traderItem(Player player) {
		int i = trader.get(player);
		ItemStack item = new ItemStack(Material.IRON_SWORD, amountAdjust(i));
		lore = new ArrayList<String>();
		lore.add("&7Level Up: &e" + (getExpFromLevel(i) - traderexp.get(player)) + " XP");
		lore.add("&7Chest Sell &l&6%&7: &e&l" + i + "%");
		lore.add("");
		lore.add("&7Levelup this skill by selling");
		lore.add("&7items in the playershop. A");
		lore.add("&7high &aTrader &7level will give");
		lore.add("&7you better rewards when");
		lore.add("&7chest selling.");
		lore.add("");
		lore.add("&bUse &a/sellchest toggle&b to toggle your sell chest ability");
		String name = plugin
				.format("&aTrader &6&6[" + i + " / " + grabConfigInt("settings.skills." + trader + ".maximum") + "]");
		return plugin.getItem(Material.EMERALD, amountAdjust(i), name, lore);
	}

	public ItemStack minerItem(Player player) {
		int i = miner.get(player);
		lore = new ArrayList<String>();
		lore.add("");
		lore.add("");
		lore.add("");
		lore.add("");
		lore.add("");
		lore.add("");
		lore.add("");
		String name = format("&7Miner &6[" + i + " / 100]");
		return plugin.getItem(Material.DIAMOND_PICKAXE, amountAdjust(i), name, lore);
	}

	public ItemStack alchemyItem(Player player) {
		int i = alchemy.get(player);
		lore = new ArrayList<String>();
		lore.add("&cINACTIVE");
		lore.add("&7Level Up: &e" + (getExpFromLevel(i) - traderexp.get(player)) + " XP");
		lore.add("");
		lore.add("&7Levelup this skill by brewing");
		lore.add("&7potions. A high Alchemy level can earn");
		lore.add("&7you the ability to instantly brew potions");
		String name = format("&5Alchemy &6[" + i + " / 100]");
		return plugin.getItem(Material.BOW, amountAdjust(i), name, lore);
	}

	public ItemStack playerItem(Player player) {
		ItemStack item = getPlayerHead(player);
		ItemMeta meta = item.getItemMeta();
		lore = new ArrayList<String>();
		meta.setDisplayName(format("&b&l" + player.getName().toString()));
		lore.add(format("&7Level: &6" + level.get(player)));
		lore.add(format("&7Level up: &b" + exp.get(player) + "&f/&e" + getExpFromLevel(level.get(player)) + " &f(&e"
				+ (int) (100 * exp.get(player) / getExpFromLevel(level.get(player))) + "&6%&f) &6XP"));
		lore.add(format(
				"&7Strength: &6" + strength.get(player) + " &7(&e+" + (strength.get(player) * 5) + "%&7 Damage)"));
		lore.add(format(
				"&7Archery: &6" + archery.get(player) + " &7(&e+" + (archery.get(player) * 7) + "%&7 Arrow Damage)"));
		lore.add(plugin
				.format("&7Health: &6" + health.get(player) + " &7(&e+" + (health.get(player) * 0.8) + "%&7 Damage)"));
		lore.add(format(
				"&7Strength: &6" + strength.get(player) + " &7(&e+" + (strength.get(player) * 5) + "%&7 Damage)"));
		lore.add(format(""));
		lore.add(format(""));
		lore.add(format(""));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return removeNBT(item);
	}

	public void setPlayerLevel(Player player, Integer newlevel, Integer experience) {
		level.replace(player, newlevel);
		exp.replace(player, experience);
		levelUpdate(player);
	}

	public void setLevelCommand(String string) {
		String[] split = string.split(" ");
		Player player = Bukkit.getPlayer(split[0]);

	}

	public ItemStack getPlayerHead(Player player) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta itemm = (SkullMeta) item.getItemMeta();
		itemm.setOwner(player.getName());
		item.setItemMeta(itemm);
		return item;
	}

	public Integer amountAdjust(Integer inte) {
		if (inte == 0) {
			return 1;
		}
		return inte;
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

	// Skill Reset
	public ItemStack skillResetItem() {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta meta = item.getItemMeta();
		lore = new ArrayList<String>();
		meta.setDisplayName(format("&aSkill Reset"));
		lore.add(format("&7Click to Respec / Reset"));
		lore.add(format("&7your skill points"));
		lore.add(format("&a9.99$ Value"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return removeNBT(item);
	}

	public void resetSkills(Player player) {
		strength.put(player, 0);
		attackspeed.put(player, 0);
		critchance.put(player, 0);
		magic.put(player, 0);
		archery.put(player, 0);
		health.put(player, 0);
		armor.put(player, 0);
		int expectedSkills = (level.get(player) * 2) + plugin.prestige.playerGrabPrestige(player);
		skillspoints.replace(player, expectedSkills);
	}

	// Exp Bottles
	public void expBottleGive(Integer int1, Integer int2, Player p) {
		p.getInventory().addItem(experienceBottle(int1, int2));
	}

	@EventHandler
	public void wandShoot(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_AIR) {
			if (((CraftPlayer) event.getPlayer()).getHandle().getAttackCooldown(0) == 1) {
				Player player = event.getPlayer();
				ItemStack mainHand = player.getInventory().getItemInMainHand();
				if (removeFormat(mainHand.getItemMeta().getDisplayName()).contains("wand")) {
					Double newdamage = 0.0;
					Double speed = 0.0;
					lore = (ArrayList<String>) mainHand.getItemMeta().getLore();
					for (String string : lore) {
						if (string.contains("Attack Speed")) {
							double atspdsk = (double) attackspeed.get(player) / 50;
							String newstring = removeFormat(string).replaceAll("attack speed: ", "");
							Double ints = Double.parseDouble(newstring);
							speed = ((atspdsk / 100) * 2 * ints) + ints;
							player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(speed);
						} else if (string.contains("Attack Damage")) {
							double magicdmg = (double) magic.get(player) + 1;
							String newstring = removeFormat(string).replaceAll("attack damage: ", "");
							Double ints = Double.parseDouble(newstring);
							newdamage = (magicdmg * 6 * ints) + (ints);
						}
					}
					if (newdamage != 0.0 && speed != 0.0) {
						Double mdmg = newdamage;
						Random rand = new Random();
						Player p = event.getPlayer();

						p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 10, 1);
						//p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 10, 10);
						Location loc = p.getLocation();
						Vector direction = loc.getDirection().add(new Vector(0.0D + 0.0D * rand.nextDouble(),
								0.0D + 0.0D * rand.nextDouble(), 0.0D + 0.0D * rand.nextDouble())
										.subtract(new Vector(0.0D + 0.0D * rand.nextDouble(),
												0.0D + 0.0D * rand.nextDouble(), 0.0D + 0.0D * rand.nextDouble())));
						for (double t = 0; t <= 16.1; t = t + .7) {
							double x = direction.getX() * t;
							double y = direction.getY() * t + 1.5;
							double z = direction.getZ() * t;

							loc.add(x, y, z);
							loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 1,
									new Particle.DustOptions(Color.AQUA, 1));
							loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 1,
									new Particle.DustOptions(Color.WHITE, 2));
							loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 1,
									new Particle.DustOptions(Color.BLACK, 1));
							
							//checks for entity to damage
							for (Entity es : loc.getWorld().getNearbyEntities(loc, .5, .5, .5)) {
								if (es instanceof LivingEntity) {
									if (es != p) {
										if (es.getType() != EntityType.ARMOR_STAND) {
											wandDamage(es, p, mdmg);
											((LivingEntity) es)
													.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 0));

											//p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 10, 10);
											p.playSound(p.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 10, 1);
											loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 9, .5, .5, .5, 1,
													new Particle.DustOptions(Color.WHITE, 2));
											return;
										}
									}
								}
							}
							loc.subtract(x, y, z);
						}
					}
				}
			}
		}
	}

	public void wandDamage(Entity entity, Player attacker, Double damage) {
		new BukkitRunnable() {
			public void run() {
				((LivingEntity) entity).damage(damage, attacker);
			}

		}.runTaskLater(plugin, 2);
	}

//gives exp to players upon damaging entities
	@EventHandler
	public void bowShoot(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Player shooter = (Player) event.getEntity();
			if (event.getProjectile() instanceof CraftTippedArrow) {
				CraftTippedArrow arrow = (CraftTippedArrow) event.getProjectile();
				ItemStack bow = event.getBow();
				Double archerys = (double) (archery.get(shooter) * 7 / 100) + 1;
				if (bow.getItemMeta() == null) {
					// vanilla damage
					arrow.setDamage(arrow.getDamage() * archerys);
				} else if (!bow.getItemMeta().getDisplayName().contains("Lvl")) {
					// lore could be set, but not with displayname
					arrow.setDamage(arrow.getDamage() * archerys);
				} else {
					lore = (ArrayList<String>) bow.getItemMeta().getLore();
					for (String string : lore) {
						if (string.contains("Attack Damage")) {
							String newstring = removeFormat(string).replaceAll("attack damage: ", "");
							Double bowdamage = Double.parseDouble(newstring);
							arrow.setDamage(arrow.getDamage() * archerys * bowdamage / 7);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void damageExpGain(EntityDamageByEntityEvent event) {
		if (event.getCause().equals(DamageCause.PROJECTILE)) {
			if (event.getDamager() instanceof CraftTippedArrow) {
				CraftTippedArrow arrow = (CraftTippedArrow) event.getDamager();
				Player attacker = (Player) arrow.getShooter();
				if (attacker.getWorld() == Bukkit.getWorld("spawn")) {
					RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
					RegionManager regions = container.get(WorldGuardPlugin.inst().wrapPlayer(attacker).getWorld());
					ProtectedRegion region = regions.getRegion("spawn");
					if (!region.contains(attacker.getLocation().getBlockX(), attacker.getLocation().getBlockY(),
							attacker.getLocation().getBlockZ())) {
						double damageAmount = event.getFinalDamage();
						addExp((int) damageAmount, attacker);
					}
				} else {
					double damageAmount = event.getFinalDamage();
					addExp((int) damageAmount, attacker);
				}
			}
		} else if (event.getDamager() instanceof Player) {
			Player attacker = (Player) event.getDamager();
			if (event.getCause().toString().equals("ENTITY_ATTACK")) {
				double percent = Math.random() * 100;
				if (percent < critchance.get(attacker)) {
					event.setDamage(event.getDamage() * 2);
				}
				Double strengths = (double) (strength.get(attacker) / 20) + 1;
				event.setDamage(event.getDamage() * strengths);
			}
			// XP GAIN
			if (attacker.getWorld() == Bukkit.getWorld("spawn")) {
				RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
				RegionManager regions = container.get(WorldGuardPlugin.inst().wrapPlayer(attacker).getWorld());
				ProtectedRegion region = regions.getRegion("spawn");
				if (!region.contains(attacker.getLocation().getBlockX(), attacker.getLocation().getBlockY(),
						attacker.getLocation().getBlockZ())) {
					double damageAmount = event.getFinalDamage();
					addExp((int) damageAmount, attacker);
				}
			} else {
				double damageAmount = event.getFinalDamage();
				addExp((int) damageAmount, attacker);
			}
		}
	}

	@EventHandler
	public void expBottleUse(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (event.getItem() == null) {
			return;
		}
		if (event.getItem().getType() == Material.DRAGON_BREATH) {
			String displayname = event.getItem().getItemMeta().getDisplayName().replaceAll("§6", "");
			String[] split = displayname.split(" ");
			int amount = Integer.parseInt(split[0]);
			if (p.isSneaking()) {
				int itemAmount = event.getItem().getAmount();
				addExp(amount * itemAmount, event.getPlayer());
				event.getItem().setAmount(event.getItem().getAmount() - itemAmount);
			} else {
				addExp(amount, event.getPlayer());
				event.getItem().setAmount(event.getItem().getAmount() - 1);
			}
			event.setCancelled(true);
		}
	}

}
