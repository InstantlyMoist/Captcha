package me.kyllian.captcha.player;

import me.kyllian.captcha.captchas.Captcha;
import org.bukkit.inventory.ItemStack;

public class PlayerData {

    private Captcha assignedCaptcha;
    private ItemStack backupItem;
    private int fails;

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
        //TODO: Maybe use #setAssignedCaptcha for this, depending on my code structure this might finish the handling of the data...
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
}
