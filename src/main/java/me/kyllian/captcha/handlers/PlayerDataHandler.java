package me.kyllian.captcha.handlers;

import me.kyllian.captcha.CaptchaPlugin;
import me.kyllian.captcha.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataHandler {

    private CaptchaPlugin plugin;
    private Map<UUID, PlayerData> playerDataMap;
    private File playerFolder;

    public PlayerDataHandler(CaptchaPlugin plugin) {
        this.plugin = plugin;
        playerDataMap = new HashMap<>();
        Bukkit.getOnlinePlayers().forEach(player -> loadPlayerDataFromPlayer(player));

        playerFolder = new File(plugin.getDataFolder(), "players");
        if (!playerFolder.exists()) playerFolder.mkdir();
    }

    public void loadPlayerDataFromPlayer(Player player) {
        playerDataMap.put(player.getUniqueId(), new PlayerData(plugin, player));
    }

    public PlayerData getPlayerDataFromUUID(UUID uuid) {
        return playerDataMap.get(uuid);
    }

    public PlayerData getPlayerDataFromPlayer(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public void unloadPlayerDataFromUUID(UUID uuid) {
        playerDataMap.remove(uuid);
    }

    public void unloadPlayerDataFromPlayer(Player player) {
        playerDataMap.remove(player.getUniqueId());
    }

    public File getPlayerFolder() {
        return playerFolder;
    }
}
