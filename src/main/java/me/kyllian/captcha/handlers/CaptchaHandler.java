package me.kyllian.captcha.handlers;

import me.kyllian.captcha.CaptchaPlugin;
import me.kyllian.captcha.captchas.Captcha;
import me.kyllian.captcha.captchas.CaptchaFactory;
import me.kyllian.captcha.captchas.CaptchaType;
import me.kyllian.captcha.player.PlayerData;
import org.bukkit.entity.Player;

public class CaptchaHandler {

    private CaptchaPlugin plugin;
    private CaptchaFactory captchaFactory;

    public CaptchaHandler(CaptchaPlugin plugin) {
        this.plugin = plugin;
        captchaFactory = new CaptchaFactory(plugin);
    }

    public void assignCaptcha(Player player) throws IllegalStateException {
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        //if (playerData.hasAssignedCaptcha()) throw new IllegalStateException("The player is already solving a captcha");
        if (player.isOp()) throw new IllegalStateException("The player has override permissions for Captcha");
        Captcha captcha = captchaFactory.getCaptcha(player);
        playerData.setAssignedCaptcha(captcha);
        playerData.setBackupItem(player.getInventory().getItemInMainHand());
        captcha.send();
    }

    public void removeAssignedCaptcha(Player player) {
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        player.getInventory().setItemInMainHand(playerData.getBackupItem());
    }
}
