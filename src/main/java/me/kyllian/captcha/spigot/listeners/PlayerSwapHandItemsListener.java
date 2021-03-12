package me.kyllian.captcha.spigot.listeners;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import me.kyllian.captcha.spigot.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHandItemsListener implements Listener {

	private CaptchaPlugin plugin;
	
	public PlayerSwapHandItemsListener(CaptchaPlugin plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void on(PlayerSwapHandItemsEvent event) {
		Player player = event.getPlayer();
		PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
		if (playerData.hasAssignedCaptcha()) event.setCancelled(true);
	}
}
