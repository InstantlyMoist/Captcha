package me.kyllian.captcha.listeners;

import me.kyllian.captcha.CaptchaPlugin;
import me.kyllian.captcha.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private CaptchaPlugin plugin;

    public PlayerChatListener(CaptchaPlugin plugin) {
        this.plugin = plugin;
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
                    //TODO: Reset stuffz, captcha has been completed succesfully.
                    Bukkit.broadcastMessage("correctos");
                    break;
                }
            default:
                Bukkit.broadcastMessage("failos");
                playerData.fail();
                break;
                //fail captcha
        }
    }
}