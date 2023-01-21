package me.kyllian.captcha.spigot.handlers;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class MessageHandler {

    private File file;
    private FileConfiguration fileConfiguration;

    public MessageHandler(CaptchaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) plugin.saveResource("messages.yml", false);
        reload();
    }

    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public String getMessage(String path) {
        Object object = fileConfiguration.get(path);
        String finalString = "";
        if (object instanceof List) finalString = String.join("\n",(List<String>) object);
        else finalString += ((String) object).replace("\\n", "\n");
        return translateColor(finalString);
    }

    public static String translateColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
