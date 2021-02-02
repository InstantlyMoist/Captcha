package me.kyllian.captcha.spigot.listeners;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import me.kyllian.captcha.spigot.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener  implements Listener {

    private CaptchaPlugin plugin;

    public PlayerMoveListener(CaptchaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        playerData.setMoved(true);
        if (!playerData.hasAssignedCaptcha()) return;
        if (event.getFrom().getBlockX() != event.getTo().getBlockX()
        || event.getFrom().getBlockY() != event.getTo().getBlockY()
        || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) event.setCancelled(true);
    }
}
