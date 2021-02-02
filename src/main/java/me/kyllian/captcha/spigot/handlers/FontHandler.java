package me.kyllian.captcha.spigot.handlers;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import org.bukkit.Bukkit;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FontHandler {

    private CaptchaPlugin plugin;

    private List<Font> fonts;

    public FontHandler(CaptchaPlugin plugin) {
        this.plugin = plugin;

        fonts = new ArrayList<>();

        File fontFolder = new File(plugin.getDataFolder(), "fonts");
        if (!fontFolder.exists()) fontFolder.mkdir();

        Arrays.stream(fontFolder.listFiles()).forEach(this::loadFont);
    }

    public void loadFont(File fontFile) {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            customFont = customFont.deriveFont(Font.PLAIN, 34);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
            fonts.add(customFont);
        } catch (Exception exception) {
            Bukkit.getLogger().warning("Loading the font '" + fontFile.getName() + "' has failed...");
        }
    }

    public Font getFont() {
        boolean random = plugin.getConfig().getBoolean("captcha-settings.random-font");
        if (random && fonts.size() == 0) {
            Bukkit.getLogger().warning("Your own fonts are required in order to be randomized!");
            return new Font("Arial", Font.PLAIN, 34);
        }
        return random ? fonts.get(ThreadLocalRandom.current().nextInt(0, fonts.size())) : new Font("Arial", Font.PLAIN, 34);
    }
}
