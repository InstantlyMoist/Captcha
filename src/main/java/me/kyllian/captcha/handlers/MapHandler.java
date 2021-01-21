package me.kyllian.captcha.handlers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.image.BufferedImage;

public interface MapHandler {

    void loadData();
    void sendMap(Player player, BufferedImage image);
    void resetMap(ItemStack map);
    FileConfiguration getMaps();

}
