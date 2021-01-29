package me.kyllian.captcha.handlers;

import me.kyllian.captcha.CaptchaPlugin;
import me.kyllian.captcha.captchas.Captcha;
import me.kyllian.captcha.captchas.CaptchaFactory;
import me.kyllian.captcha.captchas.SolveState;
import me.kyllian.captcha.player.PlayerData;
import me.kyllian.captcha.utilities.Mode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class CaptchaHandler {

    private CaptchaPlugin plugin;
    private CaptchaFactory captchaFactory;
    private long bootTime;

    public CaptchaHandler(CaptchaPlugin plugin) {
        this.plugin = plugin;
        captchaFactory = new CaptchaFactory(plugin);
        bootTime = System.currentTimeMillis();
    }

    public void login(Player player) {
        if (player.hasPermission("captcha.update")) plugin.getUpdateHandler().handleUpdateMessage(player);
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        Mode mode = Mode.valueOf(plugin.getConfig().getString("captcha-settings.mode"));
        if (mode == Mode.NONE) return;
        if (mode == Mode.FIRSTJOIN && (player.hasPlayedBefore() && playerData.hasPassed())) return;
        if (mode == Mode.RESTART && bootTime < playerData.getLastPass()) return;
        if (mode == Mode.AFTER && System.currentTimeMillis() - playerData.getLastPass() < plugin.getConfig().getInt("captcha-settings.after")) return;
        try {
            assignCaptcha(player);
        } catch (IllegalStateException exception) {
            exception.printStackTrace();
        }
    }

    public void assignCaptcha(Player player) throws IllegalStateException {
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        if (playerData.hasAssignedCaptcha()) throw new IllegalStateException("The player is already solving a captcha");
        if ((player.isOp() && plugin.getConfig().getBoolean("captcha-settings.op-override"))
                || player.hasPermission("captcha.override") && plugin.getConfig().getBoolean("captcha-settings.permission-override")) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 255));
        Captcha captcha = captchaFactory.getCaptcha(player);
        playerData.setAssignedCaptcha(captcha);
        playerData.setBackupItem(player.getInventory().getItemInMainHand());
        playerData.setBackupLocation(player.getLocation());
        if (plugin.getSafeArea().getLocation() != null) player.teleport(plugin.getSafeArea().getLocation());
        captcha.send();
        new BukkitRunnable() {
            public void run() {
                player.sendMessage(plugin.getMessageHandler().getMessage("join"));
            }
        }.runTaskLater(plugin, 20);
        playerData.setDelayedTask(new BukkitRunnable() {
            @Override
            public void run() {
                if (isCancelled()) return;
                removeAssignedCaptcha(player, SolveState.FAIL);
            }
        }.runTaskLater(plugin, plugin.getConfig().getLong("captcha-settings.time")));
    }

    public void removeAssignedCaptcha(Player player, SolveState solveState) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        plugin.getMapHandler().resetMap(player.getInventory().getItemInMainHand());
        player.getInventory().setItemInMainHand(playerData.getBackupItem());
        player.teleport(playerData.getBackupLocation());
        playerData.removeAssignedCaptcha();
        playerData.cancel();
        playerData.handleSolveState(solveState);
        if (solveState == SolveState.LEAVE) return;
        player.sendMessage(plugin.getMessageHandler().getMessage(solveState == SolveState.OK ? "success" : "fail"));
        if (solveState == SolveState.FAIL) {
            plugin.getConfig().getStringList("captcha-settings.commands-on-fail").stream().forEach(command -> execute(command, player));
            if (playerData.getFails() >= plugin.getConfig().getInt("captcha-settings.attempts")) {
                player.kickPlayer(plugin.getMessageHandler().getMessage("kick"));
                return;
            }
            this.assignCaptcha(player);
            return;
        }
        plugin.getConfig().getStringList("captcha-settings.commands-on-success").stream().forEach(command -> execute(command, player));
        playerData.setForced(false);
    }

    public void removeAllCaptchas() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(onlinePlayer);
            if (playerData.hasAssignedCaptcha()) {
                removeAssignedCaptcha(onlinePlayer, SolveState.LEAVE);
                onlinePlayer.kickPlayer(plugin.getMessageHandler().getMessage("reload-kick"));
            }
        }
    }

    public void execute(String command, Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
    }
}
