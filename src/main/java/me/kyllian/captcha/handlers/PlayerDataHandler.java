package me.kyllian.captcha.handlers;

import me.kyllian.captcha.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataHandler {

    private Map<UUID, PlayerData> playerDataMap;

    public PlayerDataHandler() {
        playerDataMap = new HashMap<>();
        Bukkit.getOnlinePlayers().forEach(player -> loadPlayerDataFromPlayer(player));
    }

    public void loadPlayerDataFromPlayer(Player player) {
        playerDataMap.put(player.getUniqueId(), new PlayerData());
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
}
