package me.kyllian.captcha.spigot.utilities;

import me.kyllian.captcha.spigot.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HandUtils {

    private static boolean old;

    static {
        old = Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.7");
    }

    public static ItemStack getItemInHand(Player player) {
        return old ? player.getInventory().getItemInHand() : player.getInventory().getItemInMainHand();
    }

    public static void setItemInHand(Player player, ItemStack item) {
        if (old) player.getInventory().setItemInHand(item);
        else player.getInventory().setItemInMainHand(item);
    }
}
