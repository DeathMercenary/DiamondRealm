package me.deathmercenary.Main;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {
	private Main plugin = Main.getPlugin(Main.class);
	private ArrayList<String> lore;
	

	
	public ItemStack crystal(int amount) {
		ItemStack item = new ItemStack(Material.NETHER_STAR, amount);
		ItemMeta meta = item.getItemMeta();
		lore = new ArrayList<String>();

		meta.setDisplayName(plugin.format("&7Common Crystal"));
		lore.add(plugin.format("&7&nRarity&f: &7Common"));
		lore.add(plugin.format("&eBring your &6&lCrystals"));
		lore.add(plugin.format("&eto &c&lNovis&e, who is"));
		lore.add(plugin.format("&efound at spawn."));

		
		meta.setLore(lore);
		item.setItemMeta(meta);

		return item;
	}
	public String rarityFormat(String rarity) {
		String rarityColor;
		if (rarity.equalsIgnoreCase("common")) {
			rarityColor = "&7Common";
		} else if (rarity.equalsIgnoreCase("rare")) {
			rarityColor = "&aRare";
		} else if (rarity.equalsIgnoreCase("epic")) {
			rarityColor = "&5Epic";
		} else if (rarity.equalsIgnoreCase("legendary")) {
			rarityColor = "&cLegendary";
		} else if (rarity.equalsIgnoreCase("mythic")) {
			rarityColor = "&4&k.&bMythic&4&k.&b";
		} else if (rarity.equalsIgnoreCase("event")) {
			rarityColor = "&6Event";
		} else {
			return "-";
		}
		return ChatColor.translateAlternateColorCodes('&', rarityColor);
	}

	public ItemStack crystals(String crystal) {
		ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();

		meta.setDisplayName(rarityFormat(crystal) + " Crystal");
		lore.add(format("&7&nRarity&f: " + rarityFormat(crystal)));
		lore.add(format("&eBring your &6&lCrystals"));
		lore.add(format("&eto &c&lNovis&e, who is"));
		lore.add(format("&efound at spawn."));

		meta.setLore(lore);
		item.setItemMeta(meta);

		return item;
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

	public ItemStack customItem(String type, String rarity, String itemType, Integer level, Integer progress,
			String gemsS, Integer amount) { // customItem("Armor", "Common", "WOODEN_SWORD", 1, 2, "", 1)
		String[] gems = { "&7[✦] Empty", "&7[✦] Empty", "&7[✦] Empty" };
		Material ITEM = Material.getMaterial(itemType.toUpperCase());
		ItemStack item = new ItemStack(ITEM, amount);
		Integer gemAmount = gemAmount(type, rarity);

		return item;
	}

	public Integer gemAmount(String type, String rarity) {
		Integer gemAmount;
		if (rarity.equalsIgnoreCase("common")) {
			gemAmount = 0;
		} else if (rarity.equalsIgnoreCase("rare") || rarity.equalsIgnoreCase("epic")) {
			gemAmount = 1;
		} else if (rarity.equalsIgnoreCase("legendary")) {
			if (type.equalsIgnoreCase("shield") || type.equalsIgnoreCase("armor")) {
				gemAmount = 1;
			} else {
				gemAmount = 2;
			}
		} else {
			if (type.equalsIgnoreCase("shield")) {
				gemAmount = 2;
			} else if (type.equalsIgnoreCase("armor")) {
				gemAmount = 1;
			} else {
				gemAmount = 3;
			}
		}
		return gemAmount;
	}
	
	public String format(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

}
