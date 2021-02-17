package me.kyllian.captcha.spigot.listeners.login;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private CaptchaPlugin plugin;

    public PlayerJoinListener(CaptchaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerDataHandler().loadPlayerDataFromPlayer(player);
        plugin.getCaptchaHandler().login(player);
    }
}
