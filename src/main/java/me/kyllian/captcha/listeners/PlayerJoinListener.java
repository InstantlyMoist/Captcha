package me.kyllian.captcha.listeners;

import me.kyllian.captcha.CaptchaPlugin;
import me.kyllian.captcha.player.PlayerData;
import me.kyllian.captcha.utilities.Mode;
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
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        Mode mode = Mode.valueOf(plugin.getConfig().getString("captcha-settings.mode"));
        if (mode == Mode.NONE) return;
        if (mode == Mode.FIRSTJOIN && (event.getPlayer().hasPlayedBefore() && playerData.hasPassed())) return;
        try {
            plugin.getCaptchaHandler().assignCaptcha(player);
        } catch (IllegalStateException exception) {
            exception.printStackTrace();
        }
    }
}
