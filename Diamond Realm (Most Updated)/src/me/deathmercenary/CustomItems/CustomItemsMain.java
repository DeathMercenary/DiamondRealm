package me.deathmercenary.CustomItems;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.deathmercenary.Main.Main;

public class CustomItemsMain implements Listener {
	private Main plugin = Main.getPlugin(Main.class);
	private HashMap<File, FileConfiguration> filesConfigs = plugin.files;
	private ArrayList<String> lore;
	private HashMap<String, String> config = new HashMap<>();
	private File itemsFile = new File(plugin.getDataFolder(), "/customitems/items.yml");
	private File configFile = new File(plugin.getDataFolder(), "/customitems/config.yml");
	private File equipmentFile = new File(plugin.getDataFolder(), "/customitems/equipment.yml");

	private void loadFile(File file) {
		plugin.filesFunctions.loadFile(file);
	}

	private void saveFile(File file) {
		plugin.filesFunctions.saveFile(file);
	}

	private FileConfiguration grabFileConfig(File file) {
		return plugin.filesFunctions.getFileConfig(file);
	}
	// Default

	public FileConfiguration grabConfigConfig() {
		return plugin.filesFunctions.getFileConfig(configFile);
	}

	public FileConfiguration grabItemsConfig() {
		return plugin.filesFunctions.getFileConfig(itemsFile);
	}

	public FileConfiguration grabEquipmentConfig() {
		return plugin.filesFunctions.getFileConfig(equipmentFile);
	}

	public void loadFiles() {
		loadFile(itemsFile);
		loadFile(equipmentFile);
		loadFile(configFile);
		saveConfigToMemory();
	}

	public void saveFiles() {
		saveFile(itemsFile);
		loadFile(equipmentFile);
		saveFile(configFile);
	}

	public void saveConfigToMemory() {
		config.put("items.lore.defaultbreak", grabFileConfig(configFile).getString("items.lore.defaultbreak"));
	}


	public ArrayList<String> getEnchants(String name, Integer level) {
		String string = grabFileConfig(equipmentFile).getString(name + "." + level.toString());
		ArrayList<String> list = new ArrayList<String>();
		if (string.contains("||")) {
			String[] splits = string.split("\\|\\|");
			for (String split : splits) {
				list.add(split);
			}
		} else if (!string.contains("||")) {
			list.add(string);
		}
		return list;
	}


	private String progressBar(Integer level, Integer progress) {
		int per = progress * 100 / itemMax(level);
		ArrayList<String> prog = new ArrayList<String>();
		int percent = per / 3;
		for (int i = 0; i != 33; i++) {
			if (percent == 0) {
				prog.add("&7");
			} else if (percent == i) {
				prog.add("&2");
			} else if (percent > i) {
				prog.add("&a");
			} else if (percent < i) {
				prog.add("&7");
			}
		}
		String string = String.join("|", prog);
		return String.join("|", prog);
	}

	private Integer itemMax(Integer level) {
		return grabFileConfig(configFile).getInt("items.upgrade." + level);
	}

	private ArrayList<String> getLore(String name, String rarity, String type, Integer level, Integer progress) {
		String getstring = type.toLowerCase() + "." + rarity.toLowerCase() + "." + name.toLowerCase();
		lore = new ArrayList<String>();
		double atkdmg = grabFileConfig(equipmentFile).getDouble(getstring + ".damage." + level);
		double atkspd = grabFileConfig(equipmentFile).getDouble(getstring + ".speed." + level);
		lore.add(format(
				"&7Rarity: " + rarityColor(rarity) + rarity.substring(0, 1).toUpperCase() + rarity.substring(1)));
		for (String enchants : getEnchants(getstring + ".enchants", level)) {
			lore.add(format(enchants));
		}
		if (type.toLowerCase().contains("weapon")) {
			lore.add(format(config.get("items.lore.defaultbreak")));
			lore.add(format("&eAttack Damage: &a" + atkdmg));
			lore.add(format("&eAttack Speed: &a" + atkspd));
		} else if (type.toLowerCase().contains("bow")) {
			lore.add(format(config.get("items.lore.defaultbreak")));
			lore.add(format("&eAttack Damage: &a" + atkdmg));
		}

		lore.add(format(config.get("items.lore.defaultbreak")));
		lore.add(format("&7Gem Stones &a[0/1]"));
		lore.add(format("&7[✦] Empty"));
		lore.add(format(grabFileConfig(configFile).getString("items.lore.defaultbreak")));
		lore.add(format("&8[" + progressBar(level, progress) + "&8] &6"));
		lore.add(format("&7Upgrade Progress: " + progress + "/" + itemMax(level)));
		lore.add(format("&7"));
		lore.add(format("&eRequires Level 40"));

		return lore;
	}

	private String rarityColor(String string) {
		return grabFileConfig(configFile).getString("items.format.raritycolor." + string.toLowerCase());
	}

	private String rarityFormat(String string) {
		return rarityColor(string) + string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	private Material getMat(String name) {
		if (name.toLowerCase().contains("bow")) {
			return Material.BOW;
		} else if (name.toLowerCase().contains("shield")) {
			return Material.SHIELD;
		} else {
			return Material.getMaterial(grabFileConfig(equipmentFile).getString(name).toUpperCase());
		}
	}

	// weapon.common.dummy_sword.enchants
	public ItemStack createCustomItem(String name, String rarity, String type, Integer level, Integer progress) {
		String getstring = type.toLowerCase() + "." + rarity.toLowerCase() + "." + name.toLowerCase();
		ItemStack item = new ItemStack(getMat(getstring + ".itemtype"), 1);
		ItemMeta meta = item.getItemMeta();
		lore = getLore(name, rarity, type, level, progress);
		meta.setDisplayName(format(rarityColor(rarity) + grabFileConfig(equipmentFile).getString(getstring + ".name")
				+ " &e[Lvl " + level.toString() + "]"));
		meta.setLore(lore);
		if (type.contains("weapon")) {
			double AttackDamage = grabFileConfig(equipmentFile).getDouble(getstring + ".damage." + level);
			double AttackSpeed = grabFileConfig(equipmentFile).getDouble(getstring + ".speed." + level);
			AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", AttackDamage,
					AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
			meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
			AttributeModifier speed = new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", AttackSpeed,
					AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
			meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speed);
		} else if (type == "armor") {

			AttributeModifier helmt = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 0,
					AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
			meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, helmt);
			AttributeModifier chestt = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 0,
					AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
			meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, chestt);
			AttributeModifier leggingst = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 0,
					AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
			meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, leggingst);
			AttributeModifier bootst = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 0,
					AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
			meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, bootst);
			AttributeModifier helm = new AttributeModifier(UUID.randomUUID(), "generic.armor", 0,
					AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
			meta.addAttributeModifier(Attribute.GENERIC_ARMOR, helm);
			AttributeModifier chest = new AttributeModifier(UUID.randomUUID(), "generic.armor", 0,
					AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
			meta.addAttributeModifier(Attribute.GENERIC_ARMOR, chest);
			AttributeModifier leggings = new AttributeModifier(UUID.randomUUID(), "generic.armor", 0,
					AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
			meta.addAttributeModifier(Attribute.GENERIC_ARMOR, leggings);
			AttributeModifier boots = new AttributeModifier(UUID.randomUUID(), "generic.armor", 0,
					AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
			meta.addAttributeModifier(Attribute.GENERIC_ARMOR, boots);
		} else if (type == "bow") {
			double AttackDamage = grabFileConfig(equipmentFile).getDouble(getstring + ".damage." + level);
			AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", AttackDamage,
					AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
			meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
			AttributeModifier speed = new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", 0.01,
					AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
			meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speed);
		}
		meta.setUnbreakable(true);
		item.setItemMeta(meta);
		// return item;
		return removeNBT(item);
	}

	private ItemStack enchantItem(ItemStack item) {

		item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		return item;
	}

	public ItemStack getItemFromFile(String string) {
		Material mat = Material.getMaterial(grabFileConfig(equipmentFile).getString(string).toUpperCase());
		return new ItemStack(mat, 1);
	}

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
		return item;
		// return removeNBT(item);
	}

	private ArrayList<String> grabConfigList(String string) {
		return new ArrayList<>(grabFileConfig(itemsFile).getStringList(string));
	}

	public ItemStack createCrystal(String rarity) {
		ItemStack item = new ItemStack(
				Material.getMaterial(grabFileConfig(itemsFile).getString("items.crystal.material").toUpperCase()), 1);
		ItemMeta meta = item.getItemMeta();
		lore = new ArrayList<String>();
		meta.setDisplayName(format(grabFileConfig(itemsFile).getString("items.crystal.name").replaceAll("%rarity%",
				rarityFormat(rarity))));
		for (String list : grabConfigList("items.crystal.lore")) {
			list = list.replace("%rarity%", rarityFormat(rarity));
			lore.add(format(list));
		}

		meta.setLore(lore);
		item.setItemMeta(meta);

		return removeNBT(item);
	}

	private String removeFormat(String string) {
		return plugin.functions.removeFormat(string).toLowerCase();
	}

	private ItemStack removeNBT(ItemStack item) {
		return plugin.removeNBT(item);
	}

	private String format(String string) {
		return plugin.format(string);
	}
	// plugin.filesFunctions.load
}
