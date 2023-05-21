package me.deathmercvenary.Spawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.deathmercenary.Main.Main;

public class SpawnMain implements Listener {

	private Main main = Main.getPlugin(Main.class);

	public Location spawnDefault = new Location(Bukkit.getWorld("spawn"), -9.5, 69, 328.5, 90.0f, 0);

	@EventHandler
	public void joinEvent(PlayerJoinEvent event) {
		spawnPlayer(event.getPlayer());
	}

	@EventHandler
	public void respawnEvent(PlayerRespawnEvent event) {
		new BukkitRunnable() {

			@Override
			public void run() {
				spawnPlayer(event.getPlayer());

			}
		}.runTaskLater(main, 1);
	}

	public void selfSpawn(Player player) {
		spawnPlayer(player);
	}

	public void forceSpawn(Player player) {
		spawnPlayer(player);
	}

	private void spawnPlayer(Player player) {
		player.teleport(spawnDefault);
	}

}
