package me.kyllian.captcha.spigot.map;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import me.kyllian.captcha.spigot.handlers.MapHandler;
import org.bukkit.Bukkit;

public class MapHandlerFactory {

    private CaptchaPlugin plugin;

    public MapHandlerFactory(CaptchaPlugin plugin) {
        this.plugin = plugin;
    }

    public MapHandler getMapHandler() {
        String minecraftVersion = Bukkit.getBukkitVersion();
        int mainVer = Integer.parseInt(minecraftVersion.split("\\.")[1]);
        return mainVer >= 13 ? new MapHandlerNew(plugin) : new MapHandlerOld(plugin);
    }
}
