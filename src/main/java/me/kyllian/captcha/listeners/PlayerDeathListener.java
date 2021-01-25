package me.kyllian.captcha.listeners;

import me.kyllian.captcha.CaptchaPlugin;
import me.kyllian.captcha.captchas.SolveState;
import me.kyllian.captcha.player.PlayerData;
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
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerDataFromPlayer(player);
        if (!playerData.hasAssignedCaptcha()) return;
        player.getInventory().setItemInMainHand(playerData.getBackupItem());
        event.getDrops().add(playerData.getBackupItem());
        Iterator iterator = event.getDrops().iterator();
        while (iterator.hasNext()) {
            ItemStack drop = (ItemStack) iterator.next();
            if (drop.getItemMeta() instanceof MapMeta) {
                MapMeta mapMeta = (MapMeta) drop.getItemMeta();
                if (plugin.getMapHandler().getMaps().getIntegerList("maps").contains(mapMeta.getMapId())) iterator.remove();
            }
        }
        playerData.setForced(true);
        plugin.getCaptchaHandler().removeAssignedCaptcha(player, SolveState.LEAVE);
    }
}
