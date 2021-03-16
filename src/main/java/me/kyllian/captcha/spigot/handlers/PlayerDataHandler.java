package me.kyllian.captcha.spigot.handlers;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import me.kyllian.captcha.spigot.player.PlayerData;
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

        playerFolder = new File(plugin.getDataFolder(), "players");
        if (!playerFolder.exists()) playerFolder.mkdir();
    }

    public void reloadPlayerData() {
        playerDataMap.entrySet().forEach(playerData -> {
            playerData.getValue().reloadData();
        });
    }

    public void loadPlayerDataFromPlayer(Player player) {
        playerDataMap.put(player.getUniqueId(), new PlayerData(plugin, player));
    }

    public PlayerData getPlayerDataFromUUID(UUID uuid) {
        return playerDataMap.computeIfAbsent(uuid, f -> new PlayerData(plugin, Bukkit.getPlayer(uuid)));
    }

    public PlayerData getPlayerDataFromPlayer(Player player) {
        return getPlayerDataFromUUID(player.getUniqueId());
    }

    public File getPlayerFolder() {
        return playerFolder;
    }
}
