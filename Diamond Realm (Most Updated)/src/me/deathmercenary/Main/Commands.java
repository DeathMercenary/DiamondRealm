package me.deathmercenary.Main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Commands implements CommandExecutor, Listener {
	public String[] commands = { "skill", "skills", "gm", "gamemode", "diamond", "xpbottle", "prestige", "broadcast",
			"bc", "b", "setlevel", "crystal", "rename", "newlore", "customitem", "getuuid", "novisspin",
			"developertoggle", "spawn", "fly", "heal", "feed", "eat", "dp", "deathpoint", "f", "faction", "fac",
			"friend", "tpa", "tpyes", "tpaccept" };

	private Main plugin = Main.getPlugin(Main.class);

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			// ===========================================
			// Skills Command
			if (cmd.getName().equalsIgnoreCase("skills") || cmd.getName().equalsIgnoreCase("skill")) {
				if (player.hasPermission("prismforge.skills")) {
					if (args.length != 0) {
						if (getPlayerFromString(args[0]) != null) {
							plugin.skills.skillsMessage(getPlayerFromString(args[0]), player);
						} else {
							playerNotOnline(player, args[0]);
						}
					} else {
						plugin.skills.skillsGui(player);
					}
				}
				return true;
				// ===========================================
				// Gamemode Commands
			} else if (cmd.getName().equalsIgnoreCase("developertoggle")) {
				if (player.hasPermission("prismforge.dev.devmsg")) {
					plugin.devClass.developerToggle(player);
				} else
					noPerms(player);
				return true;
			} else if (cmd.getName().equalsIgnoreCase("spawn")) {
				if (args.length != 0) {
					if (player.hasPermission("prismforge.admin.spawn.others")) {
						if (getPlayerFromString(args[0]) != null) {
							plugin.spawn.forceSpawn(getPlayerFromString(args[0]));
						} else {
							playerNotOnline(player, args[0]);
						}
					} else {
						player.sendMessage(format(plugin.servererror + "Correct use is&f: /&6spawn"));
					}
				} else {
					plugin.spawn.selfSpawn(player);
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("novisspin")) {
				if (player.hasPermission("prismforge.novis.quickspin")) {
					plugin.novis.novisSpinEvent(player);
				} else
					noPerms(player);
				return true;
			} else if (cmd.getName().equalsIgnoreCase("faction") || cmd.getName().equalsIgnoreCase("f")
					|| cmd.getName().equalsIgnoreCase("fac")) {
				plugin.fac.factionCommand(player, args);
				return true;
			} else if (cmd.getName().equalsIgnoreCase("getuuid")) {
				if (player.hasPermission("prismforge.dev.getuuid")) {
					if (args.length != 1) {
						player.sendMessage("Use command /getuuid (player)");
					} else {
						if (Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
							String uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
							TextComponent message = new TextComponent(format("&7Click to copy &fUUID: " + uuid));
							message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuid.toString()));
							message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("Click to copy").create()));
							player.getServer().spigot().broadcast(message);
						} else {
							player.sendMessage("Never play");
						}
					}
				} else
					noPerms(player);
				return true;
				/**
				 * TextComponent message = new TextComponent("Click this!");
				 * message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ""));
				 * message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new
				 * ComponentBuilder("youtube").create()));
				 * 
				 * TextComponent message2 = new
				 * TextComponent(ChatColor.translateAlternateColorCodes('&', "&6Good &cjob?"));
				 * message2.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,
				 * "b36c86f1-b538-4ea2-946e-e2cac9176523")); message2.setHoverEvent(new
				 * HoverEvent(HoverEvent.Action.SHOW_TEXT, new
				 * ComponentBuilder("youtube").create()));
				 * 
				 * message.addExtra(message2);
				 * 
				 * player.getServer().spigot().broadcast(message);
				 **/
			} else if (cmd.getName().equalsIgnoreCase("gm") || cmd.getName().equalsIgnoreCase("gamemode")) {
				if (player.hasPermission("prismforge.gamemode")) {
					if (args.length != 0) {
						if (args.length == 1) {
							plugin.functions.gamemodeUpdatePlayer(player, player, args[0], true);
						} else if (args.length == 2) {
							if (getPlayerFromString(args[1]) != null) {
								if (Bukkit.getServer().getPlayer(args[1]).isOnline() == false) {
									playerNotOnline(player, args[1]);
								}
								plugin.functions.gamemodeUpdatePlayer(Bukkit.getServer().getPlayer(args[1]), player,
										args[0], true);
							}
							playerNotOnline(player, args[1]);
						}
					} else {
						player.sendMessage(plugin.format(plugin.servererror
								+ "Gamemode Options&f: &6survival &f|&6 creative &f|&6 spectator &f|&6 adventure"));
					}
				} else {
					noPerms(player);
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("diamond")) {
				if (player.hasPermission("prismforge.dev.plugin")) {
					if (args.length != 0) {
						if (args.length != 2) {
							player.sendMessage(
									"Correct use is /diamond (plugin branch) (subcommand) example: /diamond skills reload");
						} else if (args.length == 2) {
							if (args[0] == "skills" || args[0] == "skill") {
								if (args[1] == "r" || args[1] == "reload") {
									plugin.skills.SkillsConfigReload();
								}
							}
						}
					} else {
						player.sendMessage(
								"Correct use is /diamond (plugin branch) (subcommand) example: /diamond skills reload");
					}
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("xpbottle")) {
				if (player.hasPermission("prismforge.admin.xpbottle")) {
					if (args.length != 0) {
						if (args.length != 2) {

						} else if (args.length == 2) {
							if (Integer.parseInt(args[0]) >= 1) {
								if (Integer.parseInt(args[1]) >= 1) {
									plugin.skills.expBottleGive(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
											player);
								} else {

								}
							} else {

							}
						}
					}
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("prestige")) {
				if (args.length != 0) {
					if (player.hasPermission("prismforge.admin.prestige.set")) {
						Bukkit.broadcastMessage(args[0] + Integer.parseInt(args[2]) + "");
						if (args[0].equalsIgnoreCase("set")) {
							if (Bukkit.getServer().getPlayer(args[1]).hasPlayedBefore() == false) {
								player.sendMessage(plugin.format(
										plugin.servererror + "Player has never played on the server before&f."));
								return true;
							} else {
								if (Integer.parseInt(args[2]) > 0) {
									plugin.prestige.prestigeSet(player, Bukkit.getPlayer(args[1]),
											Integer.parseInt(args[2]));
									player.sendMessage("more than 0");
									return true;
								} else {
									player.sendMessage("Has to be more than 0");
									return true;
								}
							}
						} else {
							player.sendMessage(plugin.format(plugin.servererror
									+ "Command usage&f: &e/prestige &7or &e/prestige set &f<&bplayer&f> <&6integer&f>"));
						}
					} else {
						player.sendMessage(plugin.format(plugin.servererror + "Command usage&f: &e/prestige"));
						return true;
					}
				} else {
					plugin.prestige.prestigeCommand(player);
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("customitem")) {
				if (player.hasPermission("prismforge.admin.createitem")) {
					player.getInventory().addItem(plugin.customitems.createCustomItem(args[0], args[1], args[2],
							Integer.parseInt(args[3]), Integer.parseInt(args[4])));
				}
			} else if (cmd.getName().equalsIgnoreCase("prestigeset")) {
				if (player.hasPermission("prismforge.admin.prestige.set")) {

				}
			} else if (cmd.getName().equalsIgnoreCase("newlore")) {
				if (player.hasPermission("prismforge.admin.newlore")) {
					if (args.length != 0) {
						player.getInventory().getItemInMainHand().getItemMeta().setDisplayName(plugin.format("&7test"));
					} else {

					}
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("dp") || cmd.getName().equalsIgnoreCase("deathpoint")) {
				if (player.hasPermission("prismforge.deathpoint")) {
					plugin.dp.dpTeleport(player);
				} else
					noPerms(player);
				return true;
			} else if (cmd.getName().equalsIgnoreCase("fly")) {
				if (player.hasPermission("prismforge.fly")) {
					plugin.fly.flyToggle(player);
				} else
					noPerms(player);
				return true;
			} else if (cmd.getName().equalsIgnoreCase("rename")) {
				return true;
			} else if (cmd.getName().equalsIgnoreCase("setlevel") || cmd.getName().equalsIgnoreCase("levelset")) {
				if (player.hasPermission("prismforge.admin.level.set")) {
					String errormessage = plugin.format(
							plugin.servererror + "Command usage&f: &e/setlevel &f<&bplayer&f> <&6level&f> <&6exp&f>");
					if (args.length != 0) {
						if (args.length == 3) {
							plugin.skills.setPlayerLevel(Bukkit.getPlayer(args[0]), Integer.parseInt(args[1]),
									Integer.parseInt(args[2]));
						} else if (args.length == 2) {
							plugin.skills.setPlayerLevel(Bukkit.getPlayer(args[0]), Integer.parseInt(args[1]), 0);
						} else {
							player.sendMessage(errormessage);
						}
					} else {
						player.sendMessage(errormessage);
					}
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("broadcast") || cmd.getName().equalsIgnoreCase("b")
					|| cmd.getName().equalsIgnoreCase("bc")) {
				if (player.hasPermission("prismforge.admin.broadcast")) {
					String errormessage = plugin
							.format(plugin.servererror + "Command usage&f: &e/broadcast &f<&6string&f>");
					if (args.length != 0) {
						StringBuilder sb = new StringBuilder();
						int i = 0;
						while (i < args.length - 1) {
							sb.append(args[i]);
							sb.append(" ");
							i++;
						}
						sb.append(args[i]);
						Bukkit.broadcastMessage(plugin.format("&f&l[&bBroadCast&f&l] &e&l» &7" + sb.toString()));
					} else {
						player.sendMessage(errormessage);
					}
					return true;
				}
			} else if (cmd.getName().equalsIgnoreCase("tpahere")) {
				if (player.hasPermission("prismforge.tpahere")) {
					if (args.length != 0) {
						if (getPlayerFromString(args[0]) != null) {
							plugin.tpa.tpahereRequest(player, getPlayerFromString(args[0]));
						} else {
							playerNotOnline(player, args[0]);
						}
					} else
						player.sendMessage(format(plugin.servererror + "Command usage&f: &e/tpahere &f(&bplayer&f)"));
				} else
					noPerms(player);
				return true;
			} else if (cmd.getName().equalsIgnoreCase("tpa")) {
				if (args.length != 0) {
					if (getPlayerFromString(args[0]) != null) {
						plugin.tpa.tpaRequest(player, getPlayerFromString(args[0]));
					} else {
						playerNotOnline(player, args[0]);
					}
				} else
					player.sendMessage(format(plugin.servererror + "Command usage&f: &e/tpa &f(&bplayer&f)"));
				return true;
			} else if (cmd.getName().equalsIgnoreCase("heal")) {
				if (player.hasPermission("prismforge.heal")) {
					if (args.length != 0) {
						if (player.hasPermission("prismforge.heal.others")) {
							if (getPlayerFromString(args[0]) != null) {
								getPlayerFromString(args[0]).setHealth(getPlayerFromString(args[0]).getMaxHealth());
								for (PotionEffect effect : getPlayerFromString(args[0]).getActivePotionEffects()) {
									getPlayerFromString(args[0]).removePotionEffect(effect.getType());
								}
								getPlayerFromString(args[0])
										.sendMessage(format(plugin.servername + "You have been healed&f."));
								getPlayerFromString(args[0]).setFoodLevel(20);
								player.sendMessage(format(plugin.servername + "You have healed the player &b"
										+ getPlayerFromString(args[0]).getName().toString() + "&f."));
							} else {
								playerNotOnline(player, args[0]);
							}
						} else {
							player.sendMessage(format(plugin.servererror + "Command usage&f: &e/heal"));
						}
					} else {
						player.setHealth(player.getMaxHealth());
						for (PotionEffect effect : player.getActivePotionEffects()) {
							player.removePotionEffect(effect.getType());
						}
						player.setFoodLevel(20);
						player.sendMessage(format(plugin.servername + "You have been healed&f."));
					}
				} else
					noPerms(player);
				return true;
			} else if (cmd.getName().equalsIgnoreCase("feed") || cmd.getName().equalsIgnoreCase("eat")) {
				if (player.hasPermission("prismforge.feed")) {
					if (args.length != 0) {
						if (player.hasPermission("prismforge.feed.others")) {
							if (getPlayerFromString(args[0]) != null) {
								getPlayerFromString(args[0]).setFoodLevel(20);
								getPlayerFromString(args[0])
										.sendMessage(format(plugin.servername + "You have satisfied your hunger&f."));
								player.sendMessage(format(plugin.servername + "You have satisfied the player &b"
										+ getPlayerFromString(args[0]).getName().toString() + "&f."));
							} else {
								playerNotOnline(player, args[0]);
							}
						} else {
							player.sendMessage(format(plugin.servererror + "Command usage&f: &e/feed"));
						}
					} else {
						player.setFoodLevel(20);
						player.sendMessage(format(plugin.servername + "You have satisfied your hunger&f."));
					}
				} else
					noPerms(player);
				return true;
			}

		} else {

		}
		return false;
	}

	public void noPerms(Player message) {
		message.sendMessage(plugin.format(plugin.servererror + "You do not have permission for this command&f."));
	}

	public void playerNotOnline(Player message, String string) {
		message.sendMessage(plugin.format(plugin.servererror + "User &6" + string + "&7 is not online&f."));
	}

	private String format(String string) {
		return plugin.format(string);
	}

	public Player getPlayerFromString(String string) {
		return Bukkit.getPlayer(string);
	}
}
