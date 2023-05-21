package me.deathmercenary.Novis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.deathmercenary.Main.Main;

public class NovisMain implements Listener {

	private Main main = Main.getPlugin(Main.class);
	private ArrayList<String> commonItems = new ArrayList<String>();
	private ArrayList<String> rareItems = new ArrayList<String>();
	private ArrayList<String> epicItems = new ArrayList<String>();
	private ArrayList<String> legendaryItems = new ArrayList<String>();
	private ArrayList<String> mythicItems = new ArrayList<String>();
	private ArrayList<String> eventItems = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> rarityText = new HashMap<String, ArrayList<String>>();
	private String[] rarityItems = { "common", "rare", "epic", "legendary", "mythic", "event" };
	public Random random = new Random();
	public Inventory equipGui = main.getServer().createInventory(null, 27, format("Select an Equipment Type"));
	public Inventory armorRarityGui = main.getServer().createInventory(null, 27, format("Armor: Select a Rarity"));
	public Inventory meleeRarityGui = main.getServer().createInventory(null, 27, format("Melee: Select a Rarity"));
	public Inventory bowRarityGui = main.getServer().createInventory(null, 27, format("Bow: Select a Rarity"));
	public Inventory shieldRarityGui = main.getServer().createInventory(null, 27, format("Shield: Select a Rarity"));
	public Inventory wandRarityGui = main.getServer().createInventory(null, 27, format("Wand: Select a Rarity"));

	private String format(String string) {
		return main.format(string);
	}

	private FileConfiguration grabEquipConfig() {
		return main.customitems.grabEquipmentConfig();
	}

	@EventHandler
	public void novisClickEvent(PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		Player player = event.getPlayer();
		if (removeFormat(entity.getName()).contains("Novis")) {
			if (event.getHand() == EquipmentSlot.HAND) {
				novisSpinEvent(player);
			}
		}
	}

	private void newInv(String string) {

	}

	@EventHandler
	public void inventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getCurrentItem() == null) {
			return;
		}
		Player player = (Player) event.getWhoClicked();
		if (event.getView().getTitle() == null) {
			return;
		} else if (event.getView().getTitle().contains("Select an Equipment Type")) {
			event.setCancelled(true);
			if (event.getCurrentItem().getItemMeta().getDisplayName().contains("shield")) {
				player.openInventory(shieldRarityGui);
			} else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("melee")) {
				player.openInventory(meleeRarityGui);
			} else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("wand")) {
				player.openInventory(wandRarityGui);
			} else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("armor")) {
				player.openInventory(armorRarityGui);
			} else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("bow")) {
				player.openInventory(bowRarityGui);
			}

		} else if (event.getView().getTitle().contains("Select a rarity")) {
			event.setCancelled(true);
			if (event.getCurrentItem().getItemMeta().getDisplayName().contains("common")) {

			} else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("rare")) {

			} else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("epic")) {

			} else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("legendary")) {

			} else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("mythic")) {

			} else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("event")) {

			}
		} else if (event.getView().getTitle().contains("Decrypting")) {
			event.setCancelled(true);
		}
	}

	public void lootSetup() {
		rarityText.put("common", commonItems);
		rarityText.put("epic", epicItems);
		rarityText.put("event", eventItems);
		rarityText.put("legendary", legendaryItems);
		rarityText.put("rare", rareItems);
		rarityText.put("mythic", mythicItems);
		Set<String> set = grabEquipConfig().getKeys(true);
		for (String node : set) {
			if (!node.contains("itemtype") && !node.contains("speed") && !node.contains("damage")
					&& !node.contains("name") && !node.contains("enchants") && !node.contains("ability")) {
				if (node.contains("common.")) {
					if (!commonItems.contains(node)) {
						commonItems.add(node);
					}
				} else if (node.contains("rare.")) {
					if (!rareItems.contains(node)) {
						rareItems.add(node);
					}
				} else if (node.contains("epic.")) {
					if (!epicItems.contains(node)) {
						epicItems.add(node);
					}
				} else if (node.contains("legendary.")) {
					if (!legendaryItems.contains(node)) {
						legendaryItems.add(node);
					}
				} else if (node.contains("event.")) {
					if (!eventItems.contains(node)) {
						eventItems.add(node);
					}
				} else if (node.contains("mythic.")) {
					if (!mythicItems.contains(node)) {
						mythicItems.add(node);
					}
				}
			}
		}
	}

	public void novisSpinEvent(Player player) {
		if (player.getInventory().getItemInMainHand().getType() != Material.NETHER_STAR) {
			novisCrystalMessage(player);
		} else {
			ItemStack helditem = player.getInventory().getItemInMainHand();
			if (helditem.getItemMeta().getDisplayName().toLowerCase().contains("crystal")) {
				String[] args = removeFormat(helditem.getItemMeta().getDisplayName().toLowerCase()).split(" ");
				String rarity = args[0].toLowerCase();
				spin(player, rarityText.get(args[0]));
				player.getInventory().getItemInMainHand()
						.setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);

			}
		}
	}

	private ItemStack grabCustomItem(String name, String rarity, String equipment, Integer level) {
		return main.customitems.createCustomItem(name, rarity, equipment, level, 5);
	}

	public ItemStack itemGrabRarity(String rarity) {
		return randomItems(rarityText.get(rarity));
	}

	private void spin(Player player, ArrayList<String> string) {
		new BukkitRunnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				player.getInventory().addItem(randomItems(string));
			}
			
		}.runTaskLater(main, 1);
		// giveItem(player, randomItems(string));
		// player.openInventory(novisGui());
		/**
		 * new BukkitRunnable() { public void run() { if
		 * (player.getOpenInventory().getTitle().contains("Decrypting")) {
		 * player.getOpenInventory().setItem(16, randomItems(string)); } else { if
		 * (veracity != false) { player.getInventory().addItem(randomItems(string)); }
		 * else { cancel(); return; } } player.playSound(player.getLocation(),
		 * Sound.BLOCK_DISPENSER_DISPENSE, 1, 1); } }.runTaskTimer(main, 20, 3);
		 **/
	}

	private ItemStack randomItems(ArrayList<String> list) {
		String[] args = list.get(random.nextInt(list.size())).split("\\.");
		return main.customitems.createCustomItem(args[2], args[1], args[0], 1, 0);
	}

	private ItemStack randomGlass() {
		ArrayList<Material> randomitems = new ArrayList<Material>();
		Material[] items = { Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE,
				Material.ORANGE_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE,
				Material.MAGENTA_STAINED_GLASS_PANE };
		for (Material item : items) {
			randomitems.add(item);
		}

		int rewardItem = random.nextInt(randomitems.size());
		return new ItemStack(randomitems.get(rewardItem));
	}

	private void giveItem(Player player, ItemStack item) {
		if (player.getCanPickupItems()) {
			player.getInventory().addItem(item);
		} else {
			player.getWorld().dropItem(player.getLocation(), item);
			player.sendMessage(format(main.servererror + "You did not have enough space for these items."));
		}

	}

	private Inventory novisGui() {
		return main.getServer().createInventory(null, 27, format("Decrypting"));
	}

	private void novisCrystalMessage(Player player) {
		player.sendMessage(format(
				"&6&lNovis&f: &7Hey&f! &7I can&f'&7t open that&f, &7maybe you can get a crystal from the warzone&f."));
	}

	private String removeFormat(String string) {
		return main.grabFunctions().removeFormat(string);
	}
	/**
	 * for (int i = 0; i != 40; i++) { new BukkitRunnable() { public void run() { if
	 * (player.getOpenInventory().getTitle().contains("Decrypting")) { for (int x =
	 * 10; x != 16; x++) { player.getOpenInventory().setItem(x,
	 * player.getOpenInventory().getItem(x + 1)); }
	 * 
	 * player.getOpenInventory().setItem(16, randomItems(string));
	 * 
	 * for (int x = 0; x != 10; x++) { player.getOpenInventory().setItem(x,
	 * randomGlass()); player.getOpenInventory().setItem(x + 17, randomGlass()); }
	 * player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 1);
	 * } else { player.getInventory().addItem(randomGlass()); return; }
	 * player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 1);
	 * } }.runTaskLater(main, 10 * i); }
	 **/
	/**
	 * long i = System.currentTimeMillis(); for (i = 0; i != 10000; i++) {//Or any
	 * Loops while(System.currentTimeMillis() < i){ //Empty Loop } i +=
	 * 1000;//Sample expectedtime += 1000; 1 second sleep
	 * player.getInventory().addItem(new ItemStack(Material.APPLE)); }
	 **/
	/**
	 * for (int i = 0; i != 20; i++) { x++; new BukkitRunnable() { public void run()
	 * { if (player.getOpenInventory().getTitle().contains("Novis")) {
	 * //giveItem(player, item); if (x == 1) {
	 * 
	 * } else if (x == 2) {
	 * 
	 * } } } }.runTaskLater(main, 20 * i); }
	 **/
}
