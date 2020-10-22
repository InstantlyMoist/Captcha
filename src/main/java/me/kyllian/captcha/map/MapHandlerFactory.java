package me.kyllian.captcha.map;

import me.kyllian.captcha.CaptchaPlugin;
import me.kyllian.captcha.handlers.MapHandler;
import org.bukkit.Bukkit;

public class MapHandlerFactory {

    private CaptchaPlugin plugin;

    public MapHandlerFactory(CaptchaPlugin plugin) {
        this.plugin = plugin;
    }

    public MapHandler getMapHandler() {
        String version = Bukkit.getVersion();
        if (version.contains("1.16") || version.contains("1.15") || version.contains("1.14") || version.contains("1.13")) return new MapHandlerNew(plugin);
        else return new MapHandlerOld(plugin);
    }
}
