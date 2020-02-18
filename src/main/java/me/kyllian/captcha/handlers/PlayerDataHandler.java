package me.kyllian.captcha.handlers;

import me.kyllian.captcha.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataHandler {

    private Map<UUID, PlayerData> playerDataMap;

    public PlayerDataHandler() { //TODO: Make sure it gets loaded on player join.
        playerDataMap = new HashMap<>();
        Bukkit.getOnlinePlayers().forEach(player -> loadPlayerDataFromPlayer(player));
        //TODO: Handle reloading data from players by loading their files
        //TODO: Consider using a config file per-player. Causing for more open configuration to be possible.
        //TODO: Consider reading data real-time, for having better functionality but slower performance... (A player reload command might be nessecary)
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
