package me.kyllian.captcha.spigot.listeners;

import me.kyllian.captcha.spigot.CaptchaPlugin;
import me.kyllian.captcha.spigot.captchas.SolveState;
import me.kyllian.captcha.spigot.player.PlayerData;
import me.kyllian.captcha.spigot.utilities.MapUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

import java.util.Iterator;

public class PlayerDeathListener implements Listener {

    private CaptchaPlugin plugin;

    public PlayerDeathListener(CaptchaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.hasMetadata("NPC")) return;
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        if (!playerData.hasAssignedCaptcha()) return;
        player.getInventory().setItemInMainHand(playerData.getBackupItem());
        event.getDrops().add(playerData.getBackupItem());
        Iterator iterator = event.getDrops().iterator();
        while (iterator.hasNext()) {
            ItemStack drop = (ItemStack) iterator.next();
            if (drop.getItemMeta() instanceof MapMeta) {
                MapMeta mapMeta = (MapMeta) drop.getItemMeta();
                if (plugin.getMapHandler().getMaps().getIntegerList("maps").contains(MapUtils.getMapId(mapMeta.getMapView()))) iterator.remove();
            }
        }
        playerData.setForced(true);
        plugin.getCaptchaHandler().removeAssignedCaptcha(player, SolveState.LEAVE);
    }
}
