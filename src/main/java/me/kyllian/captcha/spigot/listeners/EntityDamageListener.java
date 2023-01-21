package me.kyllian.captcha.spigot.listeners;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import me.kyllian.captcha.spigot.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    private CaptchaPlugin plugin;

    public EntityDamageListener(CaptchaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!plugin.getConfig().getBoolean("captcha-settings.disable-damage")) return;
        Player player = (Player) event.getEntity();
        if (player == null) return;
        if (player.hasMetadata("NPC")) return;
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        if (playerData == null) return;
        if (playerData.hasAssignedCaptcha()) event.setCancelled(true);
    }
}
