package me.kyllian.captcha.handlers;

import me.kyllian.captcha.CaptchaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateHandler {

    private CaptchaPlugin plugin;
    private final int RESOURCE_ID = 75891;

    public UpdateHandler(CaptchaPlugin plugin) {
        this.plugin = plugin;
    }

    public void getLatestVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + RESOURCE_ID).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) consumer.accept(scanner.next());
            } catch (IOException exception) {
                Bukkit.getLogger().info("[Captcha] Unable to check for updates, as an error occured. Please report the following error:");
                exception.printStackTrace();
            }
        });
    }

    public void handleUpdateMessage(Player player) {
        if (plugin.getConfig().getBoolean("run-update-checker")) {
            getLatestVersion(version -> {
                if (!version.equalsIgnoreCase(plugin.getDescription().getVersion())) {
                    player.sendMessage(plugin.getMessageHandler().getMessage("update"));
                }
            });
        }
    }
}
