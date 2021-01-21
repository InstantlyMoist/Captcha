package me.kyllian.captcha.utilities;

import me.kyllian.captcha.CaptchaPlugin;
import org.bukkit.Location;

public class SafeArea {

    private CaptchaPlugin plugin;
    private Location safeLocation;

    public SafeArea(CaptchaPlugin plugin) {
        this.plugin = plugin;

        safeLocation = (Location) plugin.getConfig().get("captcha-settings.safe-area");
    }

    public Location getLocation() {
        return safeLocation;
    }

    public void setSafeLocation(Location safeLocation) {
        this.safeLocation = safeLocation;
        plugin.getConfig().set("captcha-settings.safe-area", safeLocation);
        plugin.saveConfig();
    }
}
