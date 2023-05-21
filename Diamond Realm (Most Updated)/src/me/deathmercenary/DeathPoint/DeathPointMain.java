package me.deathmercenary.DeathPoint;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.deathmercenary.Main.Main;

public class DeathPointMain implements Listener {

	private Main main = Main.getPlugin(Main.class);

	private Location previousDeath;
	private int dpCooldownTime = 300;// seconds
	private HashMap<UUID, Long> dpCoolDown = new HashMap<UUID, Long>();
	public HashMap<UUID, Location> playerdp = new HashMap<UUID, Location>();

	@EventHandler
	public void deathEvent(PlayerDeathEvent event) {
		UUID uuid = event.getEntity().getPlayer().getUniqueId();
		if (Bukkit.getServer().getPlayer(uuid).hasPermission("prismforge.deathpoint")) {
			previousDeath = event.getEntity().getPlayer().getLocation();
			playerdp.put(uuid, previousDeath);
		}
	}

	public void dpTeleport(Player player) {
		if (playerdp.containsKey(player.getUniqueId())) {
			if(dpCoolDown.containsKey(player.getUniqueId())) {
				long secondsleft = ((dpCoolDown.get(player.getUniqueId()) / 1000) + dpCooldownTime)	- (System.currentTimeMillis() / 1000);
				if (secondsleft <= 0) {
					dpCoolDown.put(player.getUniqueId(), System.currentTimeMillis());
					dpTele(player);
				} else {
					player.sendMessage(format(main.servererror + "This command is on &6cooldown &7for another &b" + secondsleft + " seconds&f."));
				}
			} else {
				dpCoolDown.put(player.getUniqueId(), System.currentTimeMillis());
				dpTele(player);
			}
		} else {
			player.sendMessage(errorMessage());
		}
	}
	
	private void dpTele(Player player) {
		player.teleport(playerdp.get(player.getUniqueId()));
	}

	private String errorMessage() {
		return format(main.servererror + "You do not have a deathpoint saved&f!");
	}

	private String format(String string) {
		return main.format(string);
	}
}
