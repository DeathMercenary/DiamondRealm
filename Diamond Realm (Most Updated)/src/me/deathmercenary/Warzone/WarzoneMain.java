package me.deathmercenary.Warzone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.deathmercenary.Main.Main;

public class WarzoneMain implements Listener {

	private Main plugin = Main.getPlugin(Main.class);

	public HashMap<String, ItemStack> commonChest = new HashMap<>();
	public ArrayList<ItemStack> basicChests = new ArrayList<>();
	private Material[] basic = { Material.COOKED_BEEF, Material.ENDER_PEARL, Material.TNT, Material.DIAMOND,
			Material.IRON_INGOT, Material.GOLD_INGOT, Material.CARROT, Material.BREAD, Material.NETHER_WART,
			Material.POTATO, Material.OBSIDIAN, Material.LAVA_BUCKET };

	private Material[] common = {};

	private Material[] rare = { Material.GOLDEN_CARROT };

	private Material[] legendary = { Material.GOLDEN_APPLE };

	private Material[] mythic = { Material.CREEPER_SPAWN_EGG };

	public ArrayList<ItemStack> commonChests = new ArrayList<>();
	public ArrayList<ItemStack> rareChests = new ArrayList<>();
	public ArrayList<ItemStack> epicChests = new ArrayList<>();
	public ArrayList<ItemStack> legendaryChests = new ArrayList<>();
	public ArrayList<ItemStack> mythicChests = new ArrayList<>();
	public ArrayList<ItemStack> eventChests = new ArrayList<>();

	private HashMap<Location, Long> chestCooldown = new HashMap<Location, Long>();
	private int chestCoolDownTime = 120;
	private HashMap<Material, Integer> itemsAmount = new HashMap<Material, Integer>();
	public Random random = new Random();

	public void loadFile() {

	}

	public void saveLoot() {

	}

	public void resetWarzoneRegions(Player player) {

		for (int i = 0; i != 70; i++) {
			BlockVector3 corner1 = BlockVector3.at(510, 0, 796 - (i * 25));
			BlockVector3 corner2 = BlockVector3.at(-510, 0, 772 - (i * 25));
			ProtectedRegion region = new ProtectedCuboidRegion("", corner1, corner2);
		}
		BlockVector3 min = BlockVector3.at(-10, 5, -4);
		BlockVector3 max = BlockVector3.at(5, -8, 10);
		ProtectedRegion region = new ProtectedCuboidRegion("spawn", min, max);
	}

	public void setupChestLoot() {
		basicChests.clear();
		commonChests.clear();
		rareChests.clear();
		epicChests.clear();
		legendaryChests.clear();
		mythicChests.clear();
		eventChests.clear();

		for (Material item : basic) {
			basicChests.add(new ItemStack(item));

			if (item.equals(Material.COOKED_BEEF) || item.equals(Material.TNT) || item.equals(Material.OBSIDIAN)
					|| item.equals(Material.BREAD) || item.equals(Material.NETHER_WART) || item.equals(Material.POTATO)
					|| item.equals(Material.CARROT)) {
				itemsAmount.put(item, 32);
			} else if (item.equals(Material.ENDER_PEARL) || item.equals(Material.GOLD_INGOT)
					|| item.equals(Material.DIAMOND) || item.equals(Material.IRON_INGOT)
					|| item.equals(Material.GOLDEN_CARROT)) {
				itemsAmount.put(item, 4);
			}
		}

		commonChests.add(expBottle(25));
		commonChests.add(crystal("common"));

		rareChests.add(expBottle(50));
		rareChests.add(crystal("rare"));

		epicChests.add(expBottle(100));
		epicChests.add(crystal("epic"));

		legendaryChests.add(expBottle(250));
		legendaryChests.add(crystal("legendary"));

		mythicChests.add(expBottle(500));
		mythicChests.add(crystal("mythic"));
	}

	@EventHandler
	public void pvpEvent(EntityDamageByEntityEvent event) {
		if (event.getDamager().getType() != EntityType.PLAYER) {
			// return;
		} else if (event.getDamager().getType() == EntityType.ARROW) {
			Arrow arrow = (Arrow) event.getDamager();
			Bukkit.broadcastMessage(arrow.getShooter().toString());
		} else if (event.getEntityType() != EntityType.PLAYER) {
			// return;
		}
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			Player attacker = (Player) event.getDamager();
			Player victim = (Player) event.getEntity();
			if (victim == attacker) {
				event.setCancelled(true);
			} else {

			}
		}
	}

	@EventHandler
	public void chestEvent(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getPlayer().getWorld().toString().contains("spawn")) {
				if (event.getClickedBlock().getType() == Material.CHEST) {
					Chest chest = (Chest) event.getClickedBlock().getState();
					if (chestCooldown.containsKey(chest.getLocation())) {
						long secondsleft = ((chestCooldown.get(chest.getLocation()) / 1000) + chestCoolDownTime)
								- (System.currentTimeMillis() / 1000);
						if (secondsleft <= 0) {
							chestFill(chest);
							chestCooldown.put(chest.getLocation(), System.currentTimeMillis());
						}
					} else {
						chestCooldown.put(chest.getLocation(), System.currentTimeMillis());
						chestFill(chest);
					}
					// chest.getLocation();
					// chest.getBlockInventory().setItem(0, new ItemStack(randomItem(basicChests)));
					// Bukkit.broadcastMessage("" + System.nanoTime());
					// Bukkit.broadcastMessage("" + System.currentTimeMillis());
					// System.nanoTime();
					// System.nanoTime();
					// sound
					// Chest chest = (Chest) event.getClickedBlock();
					// chest.getBlockInventory().setItem(0, null);
					// chest.getBlockInventory()chest;
				} else if (event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {

				}
			}
		}
	}

	public void chestFill(Chest chest) {
		int amount = random.nextInt(3) + 2;
		chest.getBlockInventory().clear();
		while (amount != 0) {
			int slot = random.nextInt(27);
			double percent = Math.random();
			Bukkit.broadcastMessage(percent + "");
			ItemStack item = randomItem(basicChests);
			if (percent < 0.005) {
				item = randomItem(mythicChests);
			} else if (percent < 0.1) {
				item = randomItem(legendaryChests);
			} else if (percent < 0.2) {
				item = randomItem(epicChests);
			} else if (percent < 0.25) {
				item = randomItem(rareChests);
			} else if (percent < 0.3) {
				item = randomItem(commonChests);
			}
			chest.getBlockInventory().setItem(slot, item);
			amount--;
		}
		// chest.getBlockInventory().setItem(0, new ItemStack(randomItem(basicChests)));
	}

	private ItemStack randomItem(ArrayList<ItemStack> list) {
		ItemStack item = list.get(random.nextInt(list.size()));
		if (itemsAmount.containsKey(item.getType())) {
			Integer amount = itemsAmount.get(item.getType());
			item.setAmount(random.nextInt(amount));
		}
		return item;
	}

	private void worldSound(Player player) {
		// player.pl
	}

	private void compare(ItemStack item) {
	}

	private ItemStack crystal(String rarity) {
		return plugin.customitems.createCrystal(rarity);
	}

	private ItemStack expBottle(Integer integer) {
		return plugin.skills.experienceBottle(integer, 1);
	}
}
