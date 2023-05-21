package me.deathmercenary.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Events implements Listener {
	private Main plugin = Main.getPlugin(Main.class);
	private static HashMap<String, String> playerDrop = new HashMap<String, String>();

	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event) {
		// Bukkit.broadcastMessage(event.getPlayer().toString());
		/**
		 * if (event.getTo().getBlockX() != event.getFrom().getBlockX()) { Player player
		 * = event.getPlayer(); if (checkForHologram(player) == true) { Hologram
		 * hologram = plugin.holograms.get(player); hologramLocationUpdate(player,
		 * hologram); } } else if (event.getTo().getBlockZ() !=
		 * event.getFrom().getBlockZ()) { Player player = event.getPlayer(); if
		 * (checkForHologram(player) == true) { Hologram hologram =
		 * plugin.holograms.get(player); hologramLocationUpdate(player, hologram); } }
		 **/
	}

	@EventHandler
	public void playerCraftEvent(CraftItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void interact(PlayerInteractEvent event) {
		Player player = event.getPlayer();

	}
	
	@EventHandler
	public void ItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();
		ItemMeta meta = item.getItemMeta();
		if(meta.getDisplayName().contains("§")) {
			if(checkIfCanDrop(player) == false) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("WarnMessage")));
				event.setCancelled(true);
				new BukkitRunnable() {
					public void run() {
						if(playerDrop.get(player.toString()) == null || playerDrop.get(player.toString()) == "false") {
							this.cancel();
						}
						playerDrop.put(player.toString(), "false");
					}
				}.runTaskLater(plugin, 20);
			}
		}
	}
	
	public boolean checkIfCanDrop(Player player) {
		if(playerDrop.get(player.toString()) == null || playerDrop.get(player.toString()) == "false")  {
			playerDrop.put(player.toString(), "true");
			return false;
		} else if (playerDrop.get(player.toString()) == "true") {
			playerDrop.put(player.toString(), "false");
			return true;
		}
		return true;
	}
	
	@EventHandler
	public void joinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		// player.
		// event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new
		// TextComponent(plugin.format(("&cHealth &f[&a||||||||||&f] &a100&f/&c100
		// &9Mana &f[&b||||||||||&f] &b1&f/&91"))));
		// hologram.appendTextLine(plugin.format("&cHealth &f[&a||||||||||&f]
		// &a100&f/&c100"));
		// hologram.appendTextLine(plugin.format("&9Mana &f[&b||||||||||&f]
		// &b1&f/&91"));
		plugin.skills.loadSkills(player);
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
				new TextComponent(plugin.format(("&cHealth &f[&a||||||||||&f] &a" + (int) (player.getHealth() * 5)
						+ "&f/&c" + (int) (player.getMaxHealth() * 5) + " &9Mana &f[&b||||||||||&f] &b1&f/&91"))));
	}

	@EventHandler
	public void quitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		plugin.skills.saveSkills(player);
		plugin.prestige.savePrestigeFile();
	}

	@EventHandler
	public void PlayerClick(BlockBreakEvent event) {
		List<Player> list = new ArrayList<Player>();
		Player player = event.getPlayer();
		int newPrestige = plugin.prestige.playerGrabPrestige(player) + 1;
		plugin.prestige.playerPrestigeSet(player,
				newPrestige);/**
								 * Material material = event.getBlock().getBlockData().getMaterial(); if
								 * (material == Material.CRAFTING_TABLE) { event.setCancelled(true); } Location
								 * source = player.getLocation(); Location loc = event.getBlock().getLocation();
								 * Location loc2 = new Location(loc.getWorld(), loc.getX() + 3, loc.getY(),
								 * loc.getZ() + 3); Location loc3 = new Location(loc.getWorld(), loc.getX() - 3,
								 * loc.getY(), loc.getZ() - 3);
								 * player.sendMessage(player.getLocation().toString());
								 * plugin.removeBlockEffect(loc3, loc2, 1);
								 **/
	}

	@EventHandler
	public void inventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		Inventory inv = event.getInventory();
		Inventory inv2 = event.getClickedInventory();
		ItemStack slotItem = event.getCurrentItem();
		ItemStack cursorItem = event.getCursor();

		if (event.getCursor() != new ItemStack(Material.AIR)) {
			if (event.getCurrentItem() != new ItemStack(Material.AIR)) {
				int y = event.getCursor().getAmount();
			}
//GlobalChatSpell Stacking
		}
	}

	@EventHandler
	public void BlockExpSpawn(BlockExpEvent e) {
		e.setExpToDrop(0);
	}

	@EventHandler
	public void EntityExpSpawn(EntityDeathEvent e) {
		e.setDroppedExp(0);
	}

	@EventHandler
	public void ExpBottleEvent(ExpBottleEvent e) {
		e.setExperience(0);
	}

	@EventHandler
	public void PlayerFishEvent(PlayerFishEvent e) {
		e.setExpToDrop(0);
	}

	@EventHandler
	public void bottleEvent(EntitySpawnEvent event) {
		if (event.getEntityType().name().equals("THROWN_EXP_BOTTLE")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void ChatEvent(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		event.setFormat("%1$s: %2$s");
		Player player = event.getPlayer();
		Location source = player.getLocation();
		// HolographicDisplays
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getLocation().distance(source) < 100) {
				if (player == p) {
					// createHolo(player, "test");
				}
				p.sendMessage(player.getDisplayName() + plugin.format("&f: ") + event.getMessage());
			}
		}
	}

	@EventHandler
	public void chatEvent(PlayerChatEvent event) {
		Player player = event.getPlayer();
		createHolo(player, event.getMessage());
	}

	public void randomHolo(Player player, Location location, String string) {
		final Hologram hologram = HologramsAPI.createHologram(plugin, location);
		hologram.appendTextLine(plugin.format(string));
		new BukkitRunnable() {
			public void run() {
				hologram.delete();
			}
		}.runTaskLater(plugin, 200);
		// hologram.appendTextLine(plugin.format("&cHealth &f[&a||||||||||&f]
		// &a100&f/&c100"));
		// hologram.appendTextLine(plugin.format("&9Mana &f[&b||||||||||&f]
		// &b1&f/&91"));
	}

	public void createHolo(Player player, String string) {
		final Hologram hologram = HologramsAPI.createHologram(plugin, player.getLocation().add(0, 2.4, 0));
		if (string.length() < 50) {
			hologram.appendTextLine(plugin.format("&7") + string);
		} else if (string.length() < 100) {
			hologram.appendTextLine(plugin.format("&7") + string.substring(0, 50));
			hologram.appendTextLine(plugin.format("&7") + string.substring(50, string.length()));
		} else if (string.length() < 150) {
			hologram.appendTextLine(plugin.format("&7") + string.substring(0, 50));
			hologram.appendTextLine(plugin.format("&7") + string.substring(50, 100));
			hologram.appendTextLine(plugin.format("&7") + string.substring(100, string.length()));
		} else if (string.length() < 200) {
			hologram.appendTextLine(plugin.format("&7") + string.substring(0, 50));
			hologram.appendTextLine(plugin.format("&7") + string.substring(50, 100));
			hologram.appendTextLine(plugin.format("&7") + string.substring(100, 150));
			hologram.appendTextLine(plugin.format("&7") + string.substring(150, string.length()));
		} else {
			hologram.appendTextLine(plugin.format("&7") + string.substring(0, 50));
			hologram.appendTextLine(plugin.format("&7") + string.substring(50, 100));
			hologram.appendTextLine(plugin.format("&7") + string.substring(100, 150));
			hologram.appendTextLine(plugin.format("&7") + string.substring(150, 200));
			hologram.appendTextLine(plugin.format("&7") + string.substring(200, string.length()));
		}
		new BukkitRunnable() {
			public void run() {
				hologram.delete();
			}
		}.runTaskLater(plugin, 200);
	}

}
