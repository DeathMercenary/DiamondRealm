package me.deathmercenary.CustomItems;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.deathmercenary.Main.Main;

public class CustomItemsUpgrading implements Listener {
	private Main main = Main.getPlugin(Main.class);
	private ArrayList<String> lore;
	
	@EventHandler
	public void inventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		Inventory inv = event.getInventory();
		Inventory inv2 = event.getClickedInventory();
		ItemStack slotItem = event.getCurrentItem();
		ItemStack cursorItem = event.getCursor();
		if (cursorItem.getType() != Material.AIR && cursorItem != null) {
			if (slotItem != null && slotItem.getType() != Material.AIR) {
				if (slotItem.hasItemMeta() && slotItem.getItemMeta().hasDisplayName() && cursorItem.hasItemMeta()
						&& cursorItem.getItemMeta().hasDisplayName()) {
					String cursorName = removeFormat(cursorItem.getItemMeta().getDisplayName());
					String slotName = removeFormat(slotItem.getItemMeta().getDisplayName());
					if (cursorName.contains("dust")) {

					} else if (cursorName.contains("gemstone")) {

					} else if (cursorName.contains("gembreaker")) {

					} else if (cursorName.contains("[lvl")) {
						if (slotName.contains("[lvl")) {
							String[] splits1 = slotName.split("lvl ");
							if (cursorName.contains(splits1[0])) {
								Integer prog1 = 0;
								Integer prog2 = 0;
								String rarity = "";
								lore = (ArrayList<String>) slotItem.getItemMeta().getLore();
								for (String string : lore) {
									if (string.contains("Upgrade Progress:")) {
										String newstring = removeFormat(string).replaceAll("upgrade progress: ", "");
										String[] newstrings = newstring.split("/");
										prog1 = Integer.parseInt(newstrings[0]);
									} else if (string.contains("Rarity")) {
										rarity = removeFormat(string).replaceAll("rarity: ", "");
									}
								}
								lore = (ArrayList<String>) cursorItem.getItemMeta().getLore();
								for (String string : lore) {
									if (string.contains("Upgrade Progress:")) {
										String newstring = removeFormat(string).replaceAll("upgrade progress: ", "");
										String[] newstrings = newstring.split("/");
										prog2 = Integer.parseInt(newstrings[0]);
									}
								}
								String[] splits2 = cursorName.split("lvl ");

								Integer level1 = Integer.parseInt(splits1[1].replace("]", ""));
								Integer level2 = Integer.parseInt(splits2[1].replace("]", ""));
								Integer totalItem = prog1 + prog2 + returnAmount(level2) + returnAmount(level1);
								String itemName = splits1[0].replace(" ", "_");
								itemName = itemName.replace("_[", "");
								event.setCurrentItem(main.customitems.createCustomItem(itemName, rarity, returnType(slotName), returnLevel(totalItem),
										totalItem - returnAmount(returnLevel(totalItem))));
								event.setCursor(new ItemStack(Material.AIR));
								event.setCancelled(true);
							}
						}
					}

				}
			}
		}
	}
	
	private String returnType(String name) {
		if (name.contains("boots") || name.contains("helmet") || name.contains("chestplate")
				|| name.contains("leggings")) {
			return "armor";
		} else if (name.contains("shield")) {
			return "shield";
		} else if (name.contains("bow")) {
			return "bow";
		} else {
			return "weapon";
		}
	}
	private Integer returnAmount(Integer level) {
		if (level == 1) {
			return 1;
		} else if (level == 2) {
			return 3;
		} else if (level == 3) {
			return 7;
		} else if (level == 4) {
			return 17;
		} else if (level == 5) {
			return 37;
		} else if (level == 6) {
			return 87;
		} else if (level == 7) {
			return 187;
		} else {
			return 0;
		}
	}
	private Integer returnLevel(Integer amount) {
		if (amount >= 387) {
			return 8;
		} else if (amount >= 187) {
			return 7;
		} else if (amount >= 87) {
			return 6;
		} else if (amount >= 37) {
			return 5;
		} else if (amount >= 17) {
			return 4;
		} else if (amount >= 7) {
			return 3;
		} else if (amount >= 3) {
			return 2;
		} else if (amount >= 1) {
			return 1;
		} else {
			return 0;
		}
	}
	
	private String removeFormat(String string) {
		return main.functions.removeFormat(string).toLowerCase();
	}

	private ItemStack removeNBT(ItemStack item) {
		return main.removeNBT(item);
	}

	private String format(String string) {
		return main.format(string);
	}
}
