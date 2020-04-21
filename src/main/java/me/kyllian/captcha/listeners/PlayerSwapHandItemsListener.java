package me.kyllian.captcha.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.kyllian.captcha.CaptchaPlugin;
import me.kyllian.captcha.player.PlayerData;

public class PlayerSwapHandItemsListener implements Listener {

	private CaptchaPlugin plugin;
	
	public PlayerSwapHandItemsListener(CaptchaPlugin plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
		Player player = event.getPlayer();
		PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
		if (playerData.hasAssignedCaptcha()) event.setCancelled(true);
	}
}
