package me.kyllian.captcha.player;

import me.kyllian.captcha.captchas.Captcha;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerData {

    private Captcha assignedCaptcha;
    private ItemStack backupItem;
    private int fails;
    private BukkitTask delayedTask;

    public PlayerData() {
        // TODO: Load in data from files, might be needing a main class instance.
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

    public void fail() {
        this.fails++;
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
}
