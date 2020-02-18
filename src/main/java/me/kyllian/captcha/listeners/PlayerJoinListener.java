package me.kyllian.captcha.listeners;

import me.kyllian.captcha.CaptchaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private CaptchaPlugin plugin;

    public PlayerJoinListener(CaptchaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //TODO: Give captcha if needed...
        Player player = event.getPlayer();
        plugin.getPlayerDataHandler().loadPlayerDataFromPlayer(player);
    }
}
