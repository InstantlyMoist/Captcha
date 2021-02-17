package me.kyllian.captcha.spigot.listeners;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import me.kyllian.captcha.spigot.captchas.SolveState;
import me.kyllian.captcha.spigot.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDropItemListener implements Listener {

    private CaptchaPlugin plugin;

    public PlayerDropItemListener(CaptchaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        if (playerData.hasAssignedCaptcha()) {
            event.setCancelled(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getCaptchaHandler().removeAssignedCaptcha(player, SolveState.FAIL);
                }
            }.runTaskLater(plugin, 1);
        }
    }
}
