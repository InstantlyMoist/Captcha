package me.kyllian.captcha.player;

import me.kyllian.captcha.CaptchaPlugin;
import me.kyllian.captcha.captchas.Captcha;
import me.kyllian.captcha.captchas.SolveState;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public class PlayerData {

    private CaptchaPlugin plugin;

    private Captcha assignedCaptcha;
    private ItemStack backupItem;
    private int fails;
    private BukkitTask delayedTask;

    private File file;
    private FileConfiguration fileConfiguration;

    public PlayerData(CaptchaPlugin plugin, Player player) {
        this.plugin = plugin;
        file = new File(
                plugin.getPlayerDataHandler().getPlayerFolder(),
                player.getUniqueId().toString() + ".yml");
        try {
            if (!file.exists()) {
                file.createNewFile();
                fileConfiguration = YamlConfiguration.loadConfiguration(file);
                fileConfiguration.set("passed", false);
                fileConfiguration.set("total-fails", 0);
                fileConfiguration.set("last-pass", 0);
                saveData();
            } else {
                fileConfiguration = YamlConfiguration.loadConfiguration(file);
            }
        } catch (IOException exception) {
            Bukkit.getLogger().info("[Captcha] An error occured, please report the following error:");
            exception.printStackTrace();
        }
    }

    public void reloadData() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void saveData() {
        try {
            fileConfiguration.save(file);
        } catch (IOException exception) {
            Bukkit.getLogger().info("[Captcha] An error occured, please report the following error:");
            exception.printStackTrace();
        }
    }

    public boolean hasAssignedCaptcha() {
        return assignedCaptcha != null;
    }

    public void setAssignedCaptcha(Captcha assignedCaptcha) {
        this.assignedCaptcha = assignedCaptcha;
    }

    public void removeAssignedCaptcha() {
        this.assignedCaptcha = null;
    }

    public Captcha getAssignedCaptcha() {
        return assignedCaptcha;
    }

    public int getFails() {
        return fails;
    }

    public void handleSolveState(SolveState solveState) {
        if (solveState == SolveState.FAIL || solveState == SolveState.LEAVE) {
            fail();
        } else if (solveState == SolveState.OK) {
            fileConfiguration.set("last-pass", System.currentTimeMillis());
            fileConfiguration.set("passed", true);
            saveData();
        }
    }

    public void fail() {
        fileConfiguration.set("total-fails", fileConfiguration.getInt("total-fails") + 1);
        fileConfiguration.set("passed", false);
        saveData();
        this.fails++;
    }

    public long getLastPass() {
        return fileConfiguration.getLong("last-pass");
    }

    public void setBackupItem(ItemStack backupItem) {
        this.backupItem = backupItem;
    }

    public ItemStack getBackupItem() {
        return backupItem;
    }


    public void setDelayedTask(BukkitTask delayedTask) {
        this.delayedTask = delayedTask;
    }

    public void cancel() {
        delayedTask.cancel();
    }

    public boolean hasPassed() {
        return fileConfiguration.getBoolean("passed");
    }
}
