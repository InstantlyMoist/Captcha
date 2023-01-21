package me.kyllian.captcha.spigot.player;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import me.kyllian.captcha.spigot.captchas.Captcha;
import me.kyllian.captcha.spigot.captchas.SolveState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public class PlayerData {
    private Captcha assignedCaptcha;
    private boolean moved;
    private boolean forced;
    private ItemStack backupItem;
    private Location backupLocation;
    private int fails;
    private BukkitTask delayedTask;
    private String executeAfterFinish;

    private File file;
    private FileConfiguration fileConfiguration;

    public PlayerData(CaptchaPlugin plugin, Player player) {
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
        if (delayedTask == null) return;
        delayedTask.cancel();
    }

    public boolean hasPassed() {
        return fileConfiguration.getBoolean("passed");
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public Location getBackupLocation() {
        return backupLocation;
    }

    public void setBackupLocation(Location backupLocation) {
        this.backupLocation = backupLocation;
    }

    public boolean isForced() {
        return forced;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }

    public String getExecuteAfterFinish() {
        return executeAfterFinish;
    }

    public void setExecuteAfterFinish(String executeAfterFinish) {
        this.executeAfterFinish = executeAfterFinish;
    }
}
