package me.kyllian.captcha.handlers;

import me.kyllian.captcha.CaptchaPlugin;
import me.kyllian.captcha.captchas.Captcha;
import me.kyllian.captcha.captchas.CaptchaFactory;
import me.kyllian.captcha.captchas.SolveState;
import me.kyllian.captcha.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CaptchaHandler {

    private CaptchaPlugin plugin;
    private CaptchaFactory captchaFactory;

    public CaptchaHandler(CaptchaPlugin plugin) {
        this.plugin = plugin;
        captchaFactory = new CaptchaFactory(plugin);
    }

    public void assignCaptcha(Player player) throws IllegalStateException {
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        if (playerData.hasAssignedCaptcha()) throw new IllegalStateException("The player is already solving a captcha");
        //if (player.isOp()) throw new IllegalStateException("The player has override permissions for Captcha");
        Captcha captcha = captchaFactory.getCaptcha(player);
        playerData.setAssignedCaptcha(captcha);
        playerData.setBackupItem(player.getInventory().getItemInMainHand());
        captcha.send();
        player.sendMessage(plugin.getMessageHandler().getMessage("join"));
        playerData.setDelayedTask(new BukkitRunnable() {
            @Override
            public void run() {
                if (isCancelled()) return;
                removeAssignedCaptcha(player, SolveState.FAIL);
            }
        }.runTaskLater(plugin, plugin.getConfig().getLong("captcha-settings.time")));
    }

    public void removeAssignedCaptcha(Player player, SolveState solveState) {
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        plugin.getMapHandler().resetMap(player.getInventory().getItemInMainHand());
        player.getInventory().setItemInMainHand(playerData.getBackupItem());
        playerData.removeAssignedCaptcha();
        playerData.cancel();
        if (solveState == SolveState.LEAVE) return;
        player.sendMessage(plugin.getMessageHandler().getMessage(solveState == SolveState.OK ? "success" : "fail"));
        if (solveState == SolveState.FAIL) {
            playerData.fail();
            if (playerData.getFails() >= plugin.getConfig().getInt("captcha-settings.attempts")) {
                player.kickPlayer(plugin.getMessageHandler().getMessage("kick"));
                return;
            }
            this.assignCaptcha(player);
        }
    }
}
