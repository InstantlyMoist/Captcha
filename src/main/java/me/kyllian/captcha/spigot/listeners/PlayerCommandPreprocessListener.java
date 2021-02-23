package me.kyllian.captcha.spigot.listeners;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import me.kyllian.captcha.spigot.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {

    private CaptchaPlugin plugin;

    public PlayerCommandPreprocessListener(CaptchaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        if (playerData.hasAssignedCaptcha()) event.setCancelled(true);
        String command = event.getMessage().replace("/", "");
        if (plugin.getConfig().getStringList("captcha-settings.captcha-protected-commands").contains(command.split(" ")[0])) {
            plugin.getCaptchaHandler().assignCaptcha(player);
            playerData.setExecuteAfterFinish(command);
            event.setCancelled(true);
        }
    }
}
