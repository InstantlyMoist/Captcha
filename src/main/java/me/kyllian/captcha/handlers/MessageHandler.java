package me.kyllian.captcha.handlers;

import me.kyllian.captcha.CaptchaPlugin;
import me.kyllian.captcha.captchas.Captcha;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageHandler {

    private CaptchaPlugin plugin;

    private FileConfiguration fileConfiguration;
    private File file;

    public MessageHandler(CaptchaPlugin plugin) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) plugin.saveResource("messages.yml", false);
        reload();
    }

    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public String getMessage(String path) {
        return translateColor(fileConfiguration.getString(path).replace("\\n", "\n"));
    }

    public static String translateColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
