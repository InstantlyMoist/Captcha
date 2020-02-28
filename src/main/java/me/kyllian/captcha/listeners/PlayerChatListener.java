package me.kyllian.captcha.listeners;

import me.kyllian.captcha.CaptchaPlugin;
import me.kyllian.captcha.captchas.SolveState;
import me.kyllian.captcha.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerChatListener implements Listener {

    private CaptchaPlugin plugin;

    public PlayerChatListener(CaptchaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        if (!playerData.hasAssignedCaptcha()) return;
        event.setCancelled(true);
        switch (playerData.getAssignedCaptcha().getType()) {
            case TEXTCAPTCHA:
                if (event.getMessage().equals(playerData.getAssignedCaptcha().getAnswer())) {
                    removeCaptcha(player, SolveState.OK);
                    break;
                }
            default:
                removeCaptcha(player, SolveState.FAIL);
                break;
        }
    }

    public void removeCaptcha(Player player, SolveState solveState) {
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getCaptchaHandler().removeAssignedCaptcha(player, solveState);
            }
        }.runTask(plugin);
    }
}